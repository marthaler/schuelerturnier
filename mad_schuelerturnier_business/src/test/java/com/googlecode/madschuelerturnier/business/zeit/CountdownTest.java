/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.zeit;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class CountdownTest {

    private static final Logger LOG = Logger.getLogger(CountdownTest.class);

    private Countdown countdown;


    @Test
    public void testRun() throws Exception {

        DateTime time = new DateTime(2011, 6, 4, 7, 55, 3);

        // auf 15 mit Zeit in Sekunden im Konstruktor
        countdown = new Countdown(time, 15);
        testreiheDurchfuehren(time);


        // auf 15 mit 2 * Zeit im Konstruktor
        countdown = new Countdown(time, time.plusSeconds(15));
        testreiheDurchfuehren(time);


    }

    private void testreiheDurchfuehren(DateTime time) {
        countdown.signalTime(time.plusSeconds(5));

        Assert.assertEquals("00:10", countdown.getZeit());

        LOG.info("" + countdown.getZeit());
        Assert.assertFalse(countdown.isFertig());


        countdown.signalTime(time.plusSeconds(10));

        Assert.assertEquals("00:05", countdown.getZeit());

        LOG.info("" + countdown.getZeit());
        Assert.assertFalse(countdown.isFertig());


        countdown.signalTime(time.plusSeconds(14));

        Assert.assertEquals("00:01", countdown.getZeit());

        LOG.info("" + countdown.getZeit());
        Assert.assertFalse(countdown.isFertig());


        countdown.signalTime(time.plusSeconds(50));

        Assert.assertEquals("00:00", countdown.getZeit());

        LOG.info("" + countdown.getZeit());
        Assert.assertTrue(countdown.isFertig());


        countdown.signalTime(time.plusSeconds(5000));

        Assert.assertEquals("00:00", countdown.getZeit());

        LOG.info("" + countdown.getZeit());
        Assert.assertTrue(countdown.isFertig());
    }


}
