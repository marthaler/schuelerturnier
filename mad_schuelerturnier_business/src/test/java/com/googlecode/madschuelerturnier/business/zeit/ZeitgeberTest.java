/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.zeit;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class ZeitgeberTest {

    private static final Logger LOG = Logger.getLogger(ZeitgeberTest.class);

    private ApplicationEventPublisher applicationEventPublisher;
    private ZeitPuls last;
    private Zeitgeber zeitgeber = new Zeitgeber();

    @Before
    public void before() {
        applicationEventPublisher = new ApplicationEventPublisher() {

            public void publishEvent(ApplicationEvent eventn) {
                last = (ZeitPuls) eventn;
                LOG.info("" + eventn);
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

        Assert.assertTrue(!last.isUnterbruch());
        Assert.assertTrue(!last.habenWirVerspaetung());
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.stopGame("text");
        zeitgeber.run();

        Assert.assertTrue(last.isUnterbruch());
        Assert.assertTrue(!last.habenWirVerspaetung());
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.startGame(0, "text");
        zeitgeber.run();

        Assert.assertTrue(!last.isUnterbruch());
        Assert.assertTrue(!last.habenWirVerspaetung());
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.stopGame("text");
        zeitgeber.run();

        Assert.assertTrue(last.isUnterbruch());
        Assert.assertTrue(!last.habenWirVerspaetung());
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.startGame(0, "text");
        zeitgeber.run();

        Assert.assertTrue(!last.isUnterbruch());
        Assert.assertTrue(!last.habenWirVerspaetung());
        Assert.assertEquals(0, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

        Thread.sleep(1000);
        zeitgeber.stopGame("text");
        zeitgeber.run();

        Thread.sleep(1000);

        zeitgeber.run();

        Assert.assertTrue(last.isUnterbruch());
        Assert.assertTrue(last.habenWirVerspaetung());
        Assert.assertEquals(-3, last.getSpielzeitAbweichungZuEchteZeitInSekunden());

    }


}
