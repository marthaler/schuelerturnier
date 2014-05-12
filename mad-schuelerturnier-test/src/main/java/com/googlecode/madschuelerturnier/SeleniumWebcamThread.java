package com.googlecode.madschuelerturnier;

import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumWebcamThread extends Thread {

    int i = 0;

    public synchronized static String getIdExt() {
        return idExt;
    }

    public synchronized static void setIdExt(String idExt) {
        SeleniumWebcamThread.idExt = idExt;
    }

    private static String idExt = "";

    private static final Logger LOG = Logger.getLogger(SeleniumEintragerThread.class);

    private SeleniumDriverWrapper util;

    private String url;
    private boolean running = true;

    public SeleniumWebcamThread(String url) {
        this.url = url;
        util = new SeleniumDriverWrapper(url);
    }

    public void shutDown() {
        Thread.currentThread().interrupt();
        running = false;
        util.destroy();
    }

    @Override
    public void run() {

        Thread.currentThread().setName("WEBCAM");

        util.getBaseURL();

        while (running) {
            try {
                if (idExt == null || idExt.isEmpty()) {
                    this.util.sleepAMoment(5);
                    continue;
                }

                LOG.info("EINTRAGEN: " + idExt);
                this.util.sleepAMoment();
                this.util.clickById("form1:j_idt25:up");
                this.util.sleepAMoment();

                this.util.sendById("form1:j_idt25:j_idt35", idExt);
                this.util.sleepAMoment();

                this.util.clickById("form1:j_idt25:suchen");
                this.util.sleepAMoment(3);

                final String str = this.util.getSourceAsString();
                final Pattern pattern = Pattern.compile("([M]|[K])[0-9]{3}");
                final Matcher matcher = pattern.matcher(str);
                final List<String> mannschaften = new ArrayList<String>();
                while (matcher.find()) {
                    mannschaften.add(matcher.group());
                }
                final String mansch = mannschaften.get(0);
                final String m = mansch.substring(2, 4);
                this.util.sleepAMoment();
                this.util.sendById("form1:j_idt25:ToreA", m);
                this.util.sleepAMoment();
                final String mansch2 = mannschaften.get(1);
                final String m2 = mansch2.substring(2, 4);
                this.util.sendById("form1:j_idt25:ToreB", m2);
                this.util.sleepAMoment();

                this.util.clickById("form1:j_idt25:save");

                this.util.sleepAMoment();

            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

            this.setIdExt("");
        }
    }
}
