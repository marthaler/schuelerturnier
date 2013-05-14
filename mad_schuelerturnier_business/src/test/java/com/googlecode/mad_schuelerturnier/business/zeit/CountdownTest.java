/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.zeit;


import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 29.12.12
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */

public class CountdownTest {


    Countdown countdown;


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

        System.out.println(countdown.getZeit());
        Assert.assertFalse(countdown.isFertig());


        countdown.signalTime(time.plusSeconds(10));

        Assert.assertEquals("00:05", countdown.getZeit());

        System.out.println(countdown.getZeit());
        Assert.assertFalse(countdown.isFertig());


        countdown.signalTime(time.plusSeconds(14));

        Assert.assertEquals("00:01", countdown.getZeit());

        System.out.println(countdown.getZeit());
        Assert.assertFalse(countdown.isFertig());


        countdown.signalTime(time.plusSeconds(50));

        Assert.assertEquals("00:00", countdown.getZeit());

        System.out.println(countdown.getZeit());
        Assert.assertTrue(countdown.isFertig());


        countdown.signalTime(time.plusSeconds(5000));

        Assert.assertEquals("00:00", countdown.getZeit());

        System.out.println(countdown.getZeit());
        Assert.assertTrue(countdown.isFertig());
    }


}
