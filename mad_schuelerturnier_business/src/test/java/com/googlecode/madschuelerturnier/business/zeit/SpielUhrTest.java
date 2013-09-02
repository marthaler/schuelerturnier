package com.googlecode.madschuelerturnier.business.zeit;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test fuer die Spiel Uhr
 *
 * @author $Author: marthaler.worb@gmail.com $
 */
public class SpielUhrTest {

    private SpielUhr uhr = null;

    @Before
    public void setUp() throws Exception {
        uhr = new SpielUhr();
    }

    @Test
    public void testGetRichtigeZeit() throws Exception {
        Assert.assertEquals("bereits initialisiert", uhr.getRichtigeZeit(), SpielUhr.NOT_INIT);
    }

    @Test
    public void testGetSpielZeit() throws Exception {
        Assert.assertEquals("bereits initialisiert", uhr.getSpielZeit(), SpielUhr.NOT_INIT);
    }

    @Test
    public void testOnApplicationEvent() throws Exception {
        DateTime time = DateTime.now();
        ZeitPuls puls = new ZeitPuls(this, time, 2, true, 0);

        uhr.onApplicationEvent(puls);

        Assert.assertFalse(puls.toString().contains(SpielUhr.NOT_INIT));
    }

}
