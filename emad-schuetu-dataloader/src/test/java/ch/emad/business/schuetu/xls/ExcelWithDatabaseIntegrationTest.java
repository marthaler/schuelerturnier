/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.xls;

import ch.emad.business.schuetu.DataLoaderImpl;
import ch.emad.model.schuetu.model.*;
import ch.emad.model.common.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Testet den XLS Export aus der Datenbank
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
public class ExcelWithDatabaseIntegrationTest {

    private static final Logger LOG = Logger.getLogger(ExcelWithDatabaseIntegrationTest.class);

    private FromXLSLoader2 xls = null;

    private File file = null;

    @Before
    public void before() {

        xls = new FromXLSLoader2();

        try {
            file = File.createTempFile("tempX", "");
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @After
    public void after() {
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testParseMannschaften() {
        List<Mannschaft> m = xls.convertXLSToMannschaften(DataLoaderImpl.readFile("schuetu-2013.xls"));
        Assert.assertTrue(m.size() > 10);
    }

    @Test
    public void testParseSpiele() {
        // lokale ausfuehrung auf imac
        Assume.assumeTrue(System.getProperty("user.name").contains("dama"));
        List<Spiel> s = xls.convertXLSToSpiele(DataLoaderImpl.readFile("schuetu-2013.xls"));
        Assert.assertTrue(s.size() > 10);
    }

    @Test
    public void testParseKorrekturen() {
        Assume.assumeTrue(System.getProperty("user.name").contains("dama"));
        List<Korrektur> korrektur = xls.convertXLSToKorrektur(DataLoaderImpl.readFile("schuetu-2013.xls"));
        Assert.assertNotNull(korrektur);
        Assert.assertEquals("nicht genau eine Korrektur", 15, korrektur.size());

        Assert.assertEquals("Inhalt nicht richtig", "spielzeile", korrektur.get(0).getTyp());
        Assert.assertEquals("Inhalt nicht richtig", "4", korrektur.get(0).getWert());
        Assert.assertEquals("Inhalt nicht richtig", 1, korrektur.get(0).getReihenfolge());
    }


    @Test
    public void testParseText() {
        Assume.assumeTrue(System.getProperty("user.name").contains("dama"));
        List<Text> text2 = xls.convertXLSToTexte(DataLoaderImpl.readFile("schuetu-2013.xls"));
        Assert.assertNotNull(text2);
        Assert.assertEquals("nicht genau eine Text", 1, text2.size());

    }

    @Test
    public void testParsePenalty() {
        Assume.assumeTrue(System.getProperty("user.name").contains("dama"));
        List<Penalty> penalty = xls.convertXLSToPenalty(DataLoaderImpl.readFile("schuetu-2013.xls"));
        Assert.assertNotNull(penalty);
        Assert.assertEquals("nicht genau 0 Penalty", 0, penalty.size());

    }





}
