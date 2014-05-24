package com.googlecode.madschuelerturnier;

import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumWebcamThread extends Thread {

    private static String idExt = "";
    private static String aExt = "";
    private static String bExt = "";

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
                    this.util.sleepAMoment();
                    continue;
                }

                LOG.info("EINTRAGEN: " + idExt);
                this.util.clickById("form1:j_idt24:up");
                this.util.sleepAMoment();

                this.util.sendById("form1:j_idt24:j_idt32", idExt);
                this.util.sleepAMoment();

                this.util.clickById("form1:j_idt24:suchen");
                this.util.sleepAMoment(2);

                this.util.sendById("form1:j_idt24:ToreA", aExt);
                this.util.sendById("form1:j_idt24:ToreB", bExt);
                this.util.clickById("form1:j_idt24:save");

                if (this.util.getSourceAsString().contains("org.hibernate.exception.ConstraintViolationException")) {
                    util.getBaseURL();
                }
                this.setIdExt("");
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public synchronized static String getIdExt() {
        return idExt;
    }

    public synchronized static void setIdExt(String idExt) {
        SeleniumWebcamThread.idExt = idExt;
    }

    public synchronized static void setaExt(String a) {
        SeleniumWebcamThread.aExt = a;
    }

    public synchronized static void setbExt(String b) {
        SeleniumWebcamThread.bExt = b;
    }

}
