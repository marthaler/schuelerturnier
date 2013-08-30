/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.out;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * mad letzte aenderung: $Date: 2011-12-31 12:59:31 +0100 (Sa, 31 Dez 2011) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 78 $
 * @headurl $HeadURL: https://mad-schuelerturnier.googlecode.com/svn/trunk/mad_schuelereturnier /src/main/java/com/googlecode/mad_schuelerturnier/business/ controller/out/OutToWebsitePublisher.java $
 */
public class OutToWebsiteSender extends Thread {

    private static final Logger LOG = Logger.getLogger(OutToWebsiteSender.class);
    private int MAX_RETRY = 5;
    private String folder = "";
    private String name = "";
    private String content = "";
    private boolean ok = false;
    private boolean isTest = false;

    public static boolean DOWN = false;

    private final long start = System.currentTimeMillis();

    private String ftp_server;
    private int ftp_port;
    private String ftp_user;
    private String ftp_password;

    public OutToWebsiteSender(final String file, final String content, final String folder, String ftp_server, int ftp_port, String ftp_user, String ftp_password, boolean isTest) {

        this.name = file;
        this.content = content;
        this.folder = folder;
        this.ftp_server = ftp_server;
        this.ftp_port = ftp_port;
        this.ftp_user = ftp_user;
        this.ftp_password = ftp_password;
        this.isTest = isTest;

        this.start();
    }


    public void run() {

        int i = 1;
        this.ok = false;
        while (!this.ok) {

            if (this.DOWN) {
                LOG.info("senden nicht moeglich, keine verbindung zum host: " + this.ftp_server);
                return;
            }

            this.ok = this.sendFile();
            if (this.ok) {
                break;
            }

            i++;

            if (i > this.MAX_RETRY) {
                LOG.error("senden nicht moeglich: Abbruch");
                return;
            }

            OutToWebsiteSender.LOG.info("neuer versuch: " + i);

            try {
                Thread.sleep(2000);
            } catch (final InterruptedException e) {
                OutToWebsiteSender.LOG.error(e.getMessage(), e);
            }
        }
    }

    public boolean isTimeUp() {
        if (10 * 1000 > (System.currentTimeMillis() - this.start)) {
            return true;
        }
        return false;
    }

    public boolean isOk() {
        return this.ok;
    }

    private boolean sendFile() {

        FTPClient client = null;

        OutToWebsiteSender.LOG.info("upload start: " + this.name);
        try {
            client = connect();
        } catch (final Exception e) {
            OutToWebsiteSender.LOG.error(e.getMessage(), e);
            DOWN = true;
            return false;
        }
        try {
            if (isTest) {
                client.makeDirectory("testdurchfuehrungen");
                client.changeWorkingDirectory("testdurchfuehrungen");
                client.makeDirectory(this.folder);
            } else {
                client.makeDirectory(this.folder);
            }

            InputStream fis = null;

            fis = new ByteArrayInputStream(this.content.getBytes());
            client.storeFile(this.folder + "/" + this.name, fis);

            fis.close();
            OutToWebsiteSender.LOG.info("upload ok: " + this.name);

        } catch (final Exception e) {
            OutToWebsiteSender.LOG.error(e.getMessage(), e);
            return false;
        } finally {
            disconnect(client);
        }
        return true;
    }

    private FTPClient connect() throws Exception {
        final FTPClient client = new FTPClient();
        // client.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
        client.setConnectTimeout(10000);
        client.connect(ftp_server, ftp_port);
        client.login(ftp_user, ftp_password);
        return client;
    }

    private void disconnect(final FTPClient client) {
        if (client == null) {
            LOG.error("ftp client war null!");
            return;
        }
        try {
            client.logout();
            client.disconnect();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
