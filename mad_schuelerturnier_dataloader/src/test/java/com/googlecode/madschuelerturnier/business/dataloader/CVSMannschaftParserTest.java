/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.dataloader;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * CVSMannschaftParserTest
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
public class CVSMannschaftParserTest {

    private static final Logger LOG = Logger.getLogger(CVSMannschaftParserTest.class);

    private CVSMannschaftParser parser = null;

    @Before
    public void before() {
        parser = new CVSMannschaftParser();
    }

    @Test
    public void testParseLine() {

        List<Mannschaft> liste11 = parser.parseFileContent(parser.loadCSVFile("2011"));

        for (Mannschaft mannschaft : liste11) {
            LOG.info("" + mannschaft.toString2());
        }
        Assert.assertEquals(87, liste11.size());

        List<Mannschaft> liste12 = parser.parseFileContent(parser.loadCSVFile("2012"));

        for (Mannschaft mannschaft : liste12) {
            LOG.info("" + mannschaft.toString2());
        }
        Assert.assertEquals(88, liste12.size());

    }

    @Test
    public void testLoadMannschaften4Jahr() {

        List<Mannschaft> liste01 = parser.loadMannschaften4Jahr("2011", null, null);
        Assert.assertEquals(87, liste01.size());

        List<Mannschaft> liste02 = parser.loadMannschaften4Jahr("2011", Boolean.TRUE, null);
        Assert.assertEquals(54, liste02.size());

        List<Mannschaft> liste03 = parser.loadMannschaften4Jahr("2011", null, 6);
        Assert.assertEquals(10, liste03.size());


    }

}
