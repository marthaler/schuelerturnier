package com.googlecode.mad_schuelerturnier.business.xls;

import com.google.common.io.Resources;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Testet den XLS Export aus der Datenbank
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
public class ExcelWithDatabaseIntegrationTest {

    private static final Logger LOG = Logger.getLogger(ExcelWithDatabaseIntegrationTest.class);

    private FromXLS xls = null;

    private File file = null;

    @Before
    public void before() {

        xls = new FromXLS();

        try {
            file = File.createTempFile("tempX", "");
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @After
    public void after() {
        //FileUtils.deleteQuietly(file);
    }

    @Test
    public void testParseLine() {
        List<Mannschaft> m = xls.convertXLSToMannschaften(readFile("schuetu-test.xls"));
        Assert.assertTrue(m.size() > 10);
    }


    private byte[] readFile(String file) {
        byte[] in = null;
        URL url = Resources.getResource(file);
        try {
            in = Resources.toByteArray(url);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return in;
    }

}
