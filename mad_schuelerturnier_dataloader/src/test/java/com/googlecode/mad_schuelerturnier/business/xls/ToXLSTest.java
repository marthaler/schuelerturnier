package com.googlecode.mad_schuelerturnier.business.xls;

import com.googlecode.mad_schuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.*;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 12.01.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
@Ignore
public class ToXLSTest {

    private static final Logger LOG = Logger.getLogger(ToXLSTest.class);

    private CVSMannschaftParser parser = null;
    private ToXLS xls = null;

    private static String FILE = "/test.xls";

    @Before
    public void before() {
        parser = new CVSMannschaftParser();
        xls = new ToXLS();
    }

    @After
    public void after() {
        FileUtils.deleteQuietly(new File(FILE));
    }

    @Test
    public void testParseLine() {
        List<Mannschaft> mannschaften = parser.parseFileContent(parser.loadCSVFile("2013"));
        xls.dumpMannschaftenToXLSFile(mannschaften, FILE);
        Assert.assertTrue(new File(FILE).exists());
    }

}
