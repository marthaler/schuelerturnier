/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.xls;

import com.googlecode.madschuelerturnier.business.DataLoaderImpl;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
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

    private FromXLSLoader xls = null;

    private File file = null;

    @Before
    public void before() {

        xls = new FromXLSLoader();

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
    public void testParseEinstellungen() {
        SpielEinstellungen einst = xls.convertXLSToEinstellung(DataLoaderImpl.readFile("schuetu-2013.xls"));
        Assert.assertNotNull(einst);
    }

}