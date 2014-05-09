package com.googlecode.madschuelerturnier;

import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper;
import org.apache.log4j.Logger;

//

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumSpeakerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(SeleniumSpeakerThread.class);

    private SeleniumDriverWrapper util = new SeleniumDriverWrapper();

    @Override
    public void run() {

        Thread.currentThread().setName("SPEAK");

        this.util.login("tester1915speaker", "1234");
        this.util.sleepAMoment(1);
        this.util.clickById("form:ac:weiter");

        this.util.clickByLink("speaker");

        for (int i = 0; i < 20000; i++) {

            if (MatchOnly.restart) {
                break;
            }

            this.util.sleepAMoment();

            try {

                this.util.sleepAMoment();
                this.util.clickById("formular:panel:play_table:0:play");

            } catch (final Exception e) {
                this.util.sleepAMoment();
                SeleniumSpeakerThread.LOG.error("not found: formular:panel:play_table:0:play");
            }

            this.util.sleepAMoment(10);

        }
        this.util.distroy();
    }

    public static void main(final String[] args) {
        final SeleniumSpeakerThread th = new SeleniumSpeakerThread();
        th.run();
        try {
            th.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}