package com.googlecode.madschuelerturnier;

import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper;
import org.apache.log4j.Logger;

//

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumSpeakerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(SeleniumEintragerThread.class);

    private SeleniumDriverWrapper util;

    private String url;
    private String password;
    private String user;
    private boolean running = true;

    private int errorcount =0;

    public SeleniumSpeakerThread(String user, String password, String url) {
        this.url = url;
        this.password = password;
        this.user = user;
        util = new SeleniumDriverWrapper(url);
    }

    public void shutDown() {
        Thread.currentThread().interrupt();
        running = false;
        util.destroy();
    }

    @Override
    public void run() {
try{
        Thread.currentThread().setName("SPEAK");

        this.util.login("tester1915speaker", "1234");
        this.util.sleepAMoment();

        this.util.clickById("form:ac:weiter");
        this.util.sleepAMoment();

        this.util.clickByLink("speaker");

        while (running) {

            this.util.sleepAMoment();

            try {
                this.util.clickById("formular:panel:play_table:0:play");
            } catch (final Exception e) {
                this.util.sleepAMoment();
                SeleniumSpeakerThread.LOG.error("not found: formular:panel:play_table:0:play");
            }

            this.util.sleepAMoment(10);
        }
    } catch (Exception e) {
        LOG.error("!!! " + e.getMessage());
        errorcount++;

        if (errorcount > 5) {
            this.running = false;
            SeleniumSpeakerThread th = new SeleniumSpeakerThread(user, password, url);
            th.run();
        }
    }
    }
}