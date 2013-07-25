/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.dataloader;

import com.googlecode.mad_schuelerturnier.business.xls.FromXLS;
import com.googlecode.mad_schuelerturnier.business.xls.ToXLS;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielEinstellungenRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
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

import static org.junit.Assert.assertEquals;

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

    @Autowired
    MannschaftRepository mRepo;

    @Autowired
    SpielEinstellungenRepository eRepo;

    @Autowired
    SpielRepository sRepo;

    @Autowired
    private ToXLS toXls = null;

    @Autowired
    private FromXLS fromXls = null;


    @BeforeClass
    public static void dbInit(){

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
            e.printStackTrace();
        }

    }

    @Test
    public void xlsTest() {

        Assert.assertTrue(mRepo.findAll().size() > 10);

        Assert.assertTrue(eRepo.findAll().size() == 1);

        Assert.assertTrue(sRepo.findAll().size() > 10);


        toXls.dumpMOdelToXLSFile(mRepo.findAll(),sRepo.findAll(),new File(System.getProperty("java.io.tmpdir") + "out.xls"));

        System.out.println("File dumped: " + System.getProperty("java.io.tmpdir") + "out.xls");


        ProcessBuilder pb = new ProcessBuilder("open", System.getProperty("java.io.tmpdir") + "out.xls");

        try {
            Process p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}