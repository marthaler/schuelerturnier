/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.zeit;


import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 29.12.12
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */

public class ZeitgeberTest {


    ApplicationEventPublisher applicationEventPublisher;
    ZeitPuls last;
    Zeitgeber zeitgeber = new Zeitgeber();

    @Before
    public void before() {
        applicationEventPublisher = new ApplicationEventPublisher() {

            public void publishEvent(ApplicationEvent eventn) {
                last = (ZeitPuls) eventn;
                System.out.println(eventn);
            }
        };

        zeitgeber.setApplicationEventPublisher(applicationEventPublisher);
    }

    @Test
    public void testRun() throws Exception {

        DateTime time = new DateTime(2011, 6, 4, 7, 55, 3);
        zeitgeber.startClock(time, time, 3);
        Thread.sleep(1000);
        zeitgeber.run();

        Assert.assertTrue(last.isUnterbruch() == false);
        Assert.assertTrue(last.habenWirVerspaetung() == false);
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.stopGame("text");
        zeitgeber.run();

        Assert.assertTrue(last.isUnterbruch() == true);
        Assert.assertTrue(last.habenWirVerspaetung() == false);
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.startGame(0, "text");
        zeitgeber.run();

        Assert.assertTrue(last.isUnterbruch() == false);
        Assert.assertTrue(last.habenWirVerspaetung() == false);
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.stopGame("text");
        zeitgeber.run();

        Assert.assertTrue(last.isUnterbruch() == true);
        Assert.assertTrue(last.habenWirVerspaetung() == false);
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.startGame(0, "text");
        zeitgeber.run();

        Assert.assertTrue(last.isUnterbruch() == false);
        Assert.assertTrue(last.habenWirVerspaetung() == false);
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());


        Thread.sleep(1000);
        zeitgeber.stopGame("text");
        zeitgeber.run();


        Thread.sleep(1000);

        zeitgeber.run();


        Assert.assertTrue(last.isUnterbruch() == true);
        Assert.assertTrue(last.habenWirVerspaetung() == true);
        Assert.assertEquals(-3, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

    }


}
