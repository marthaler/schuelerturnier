/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.xls;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielEinstellungenRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test welcher die fertiggespielte db vom 2013 im Temp Verzeichnis bereitstellt
 * Diese DB wird anschliessend gestartet und steht im Test zur Verfuegung
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-persistence-test-context.xml")
@Transactional
public class RealDBTest2013 {

    // wird hier benoetigt um immer das gleiche date zu setzen
    private Date date = new Date();

    private static final Logger LOG = Logger.getLogger(RealDBTest2013.class);

    @Autowired
    private MannschaftRepository mRepo;

    @Autowired
    private SpielEinstellungenRepository eRepo;

    @Autowired
    private SpielRepository sRepo;

    @Autowired
    private ToXLSDumper toXlsDumper = null;

    @Autowired
    private FromXLSLoader fromXlsLoader = null;

    @BeforeClass
    public static void dbInit() {

        InputStream lobs = RealDBTest2013.class.getResourceAsStream("/testdb/db.lobs");
        InputStream log = RealDBTest2013.class.getResourceAsStream("/testdb/db.log");
        InputStream properties = RealDBTest2013.class.getResourceAsStream("/testdb/db.properties");
        InputStream script = RealDBTest2013.class.getResourceAsStream("/testdb/db.script");

        try {

            FileOutputStream lobsOut = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/db.lobs");
            FileOutputStream logOut = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/db.log");
            FileOutputStream propertiesOut = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/db.properties");
            FileOutputStream scriptOut = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/db.script");

            org.apache.commons.io.IOUtils.copy(lobs, lobsOut);
            org.apache.commons.io.IOUtils.copy(log, logOut);
            org.apache.commons.io.IOUtils.copy(properties, propertiesOut);
            org.apache.commons.io.IOUtils.copy(script, scriptOut);

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @Test
    public void xlsTest() {

        LOG.info("-->" + System.getProperty("java.io.tmpdir"));

        // Groessen pruefen
        Assert.assertTrue(mRepo.findAll().size() > 10);

        Assert.assertTrue(eRepo.findAll().size() == 1);

        Assert.assertTrue(sRepo.findAll().size() > 10);

        // letztes herausnehemen wegen fehler nach
        List<Mannschaft> mannschaften = mRepo.findAll();
        //mannschaften.remove(mannschaften.size()-1);

        LOG.info("geschrieben aus db to xls: " + mannschaften.size());

        toXlsDumper.dumpMOdelToXLSFile(deleteChangedate(deleteChangedate(mannschaften)), sRepo.findAll(), new File(System.getProperty("java.io.tmpdir") + "out.xls"));

        List<Mannschaft> listeAusXLS = null;
        try {
            listeAusXLS = fromXlsLoader.convertXLSToMannschaften(FileUtils.readFileToByteArray(new File(System.getProperty("java.io.tmpdir") + "out.xls")));
            LOG.info("gelesen aus xls 1: " + listeAusXLS.size());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        XStream xstream = new XStream();
        String str = xstream.toXML(deleteChangedate(listeAusXLS));

        try {
            FileUtils.writeStringToFile(new File(System.getProperty("java.io.tmpdir") + "vorher.xml"), str);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        toXlsDumper.dumpMOdelToXLSFile(deleteChangedate(listeAusXLS), null, new File(System.getProperty("java.io.tmpdir") + "out2.xls"));

        List<Mannschaft> listeAusXLS2 = null;
        try {
            listeAusXLS2 = fromXlsLoader.convertXLSToMannschaften(FileUtils.readFileToByteArray(new File(System.getProperty("java.io.tmpdir") + "out2.xls")));
            LOG.info("gelesen aus xls 2: " + listeAusXLS2.size());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        String xstreamText = xstream.toXML(deleteChangedate(listeAusXLS2));

        try {
            FileUtils.writeStringToFile(new File(System.getProperty("java.io.tmpdir") + "nachher.xml"), xstreamText);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        // pruefen ob die beiden Xstream Files gleich sind
        try {
            Assert.assertEquals("pruefung ob beide xstream files gleich sind", FileUtils.readFileToString(new File(System.getProperty("java.io.tmpdir") + "vorher.xml")), FileUtils.readFileToString(new File(System.getProperty("java.io.tmpdir") + "nachher.xml")));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        LOG.info("File dumped: " + System.getProperty("java.io.tmpdir") + "out.xls");

        ProcessBuilder pb = new ProcessBuilder("open", System.getProperty("java.io.tmpdir") + "out.xls");

        try {
            Process p = pb.start();
            LOG.info("prozess beendet: " + p);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private List<Mannschaft> deleteChangedate(List<Mannschaft> mannschaften) {

        List<Mannschaft> liste = new ArrayList<Mannschaft>();

        for (Mannschaft m : mannschaften) {
            m.setCreationDate(date);

            if (m.getCaptainName() != null && !m.getCaptainName().equals("")) {
                liste.add(m);
            }
        }
        return liste;
    }

}