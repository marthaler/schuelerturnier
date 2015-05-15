/**
 * Apache License 2.0
 */
package ftp;

import com.googlecode.madschuelerturnier.exceptions.TurnierException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class OutToWebsiteSender extends Thread {

    private static final Logger LOG = Logger.getLogger(OutToWebsiteSender.class);
    private static final int MAX_RETRY = 5;
    private String folder = "";
    private String name = "";
    private String content = "";
    private boolean ok = false;

    public static boolean down = false; // NOSONAR

    private static final long START = System.currentTimeMillis();

    private String ftpServer;
    private int ftpPort;
    private String ftpUser;
    private String ftpPassword;

    public OutToWebsiteSender(final String file, final String content, final String folder, String ftpServer, int ftpPort, String ftpUser, String ftpPassword) {

        this.name = file;
        this.content = content;
        this.folder = folder;
        this.ftpServer = ftpServer;
        this.ftpPort = ftpPort;
        this.ftpUser = ftpUser;
        this.ftpPassword = ftpPassword;

        this.start();
    }


    public void run() {

        int i = 1;
        this.ok = false;
        while (!this.ok) {

            if (this.down) {
                LOG.info("senden nicht moeglich, keine verbindung zum host: " + this.ftpServer);
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
        return 10 * 1000 > (System.currentTimeMillis() - OutToWebsiteSender.START);
    }

    public boolean isOk() {
        return this.ok;
    }

    private boolean sendFile() {

        FTPClient client;

        OutToWebsiteSender.LOG.info("upload START: " + this.name);
        try {
            client = connect();
        } catch (final Exception e) {
            OutToWebsiteSender.LOG.error(e.getMessage(), e);
            down = true;
            return false;
        }
        try {

            client.makeDirectory(this.folder);

            InputStream fis;

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

    private FTPClient connect() throws TurnierException {
        try {
            final FTPClient client = new FTPClient();
            // client . setTrustManager (TrustManagerUtils . getAcceptAllTrustManager ( ) )
            client.setConnectTimeout(10000);
            client.connect(ftpServer, ftpPort);
            client.login(ftpUser, ftpPassword);
            return client;
        } catch (Exception e) {
            throw new TurnierException(e);
        }
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
