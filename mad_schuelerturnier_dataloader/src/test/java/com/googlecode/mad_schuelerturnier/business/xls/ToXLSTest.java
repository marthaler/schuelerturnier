package com.googlecode.mad_schuelerturnier.business.xls;

import com.googlecode.mad_schuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
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
@Ignore
public class ToXLSTest {

    private static final Logger LOG = Logger.getLogger(ToXLSTest.class);

    private CVSMannschaftParser parser = null;
    private ToXLS xls = null;

    private File file = null;

    @Before
    public void before() {
        parser = new CVSMannschaftParser();
        xls = new ToXLS();

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
    public void testParseLine() {
        Assert.assertTrue(file.length() < 500);
        List<Mannschaft> mannschaften = parser.parseFileContent(parser.loadCSVFile("2013"));
        xls.dumpMannschaftenToXLSFile(mannschaften, file);
        Assert.assertTrue(file.length() > 500);
    }

}
