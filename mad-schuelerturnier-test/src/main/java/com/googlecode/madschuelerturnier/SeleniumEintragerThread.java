package com.googlecode.madschuelerturnier;

import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumEintragerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(SeleniumEintragerThread.class);

    private SeleniumDriverWrapper util;

    private String url;
    private String password;
    private String user;
    private boolean running = true;

    public SeleniumEintragerThread(String user, String password, String url) {
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

        Thread.currentThread().setName("O_EINTR");

        while (running) {

            this.util.clickByLink("Abmelden");
            this.util.sleepAMoment();

            this.util.login("tester1915eintrager", "1234");
            this.util.sleepAMoment();

            this.util.clickById("form:ac:weiter");
            this.util.sleepAMoment();

            // penalty eintragen
            if (this.util.getSourceAsString().contains("form1:dataTablePen:0:j_idt32")) {
                this.util.sendByName("form1:dataTablePen:0:j_idt28", "irgendwas");
                this.util.clickById("form1:dataTablePen:0:j_idt32");
                this.util.sleepAMoment();
            }

            String id = findFirstID(this.util.getSourceAsString());

            if (SeleniumWebcamThread.getIdExt() == null || SeleniumWebcamThread.getIdExt().isEmpty()) {
                SeleniumWebcamThread.setIdExt(id);
            }

            this.util.sleepAMoment(10);

        }
    }

    private String findFirstID(String str) {
        String ret = StringUtils.substringBetween(str, "class=\"ui-dialog-title\">Schirizettel: ", "</span><a");
        if (ret != null && ret.length() == 2) {
            return ret;
        }
        return null;
    }

}
