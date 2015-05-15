package ch.emad.model.schuetu.integration; /**
 * Apache License 2.0
 */

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Integrationstest fuer die Transportschicht
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
public class TransportIntegrationTest {

    private List<TransportTestTomcat> embeddedServerList = new ArrayList<TransportTestTomcat>();

    private static final Logger LOG = Logger.getLogger(TransportIntegrationTest.class);


    @After
    public void stopServer() {
        for (TransportTestTomcat transportTestTomcat : embeddedServerList) {
            transportTestTomcat.stop();
        }
    }

    @Test
    public void testSuite() throws Exception {
        int portEins = getFreePort();

        System.setProperty("ownConnectionString", "http://localhost:" + portEins + "/app/transport");

        TransportTestTomcat embeddedServer = new TransportTestTomcat(portEins, "/", "1");
        embeddedServer.start();
        embeddedServerList.add(embeddedServer);

        while (!this.isUp("" + portEins)) {
            Thread.sleep(250);
            LOG.info("tomcat auf port: " + portEins + " startversuch.");
        }

        LOG.info("tomcat auf port: " + portEins + " gestartet.");

        int portZwei = getFreePort();

        System.setProperty("ownConnectionString", "http://localhost:" + portZwei + "/app/transport");
        System.setProperty("remoteConnectionString", "http://localhost:" + portEins + "/app/transport");
        embeddedServer = new TransportTestTomcat(portZwei, "/", "2");
        embeddedServer.start();
        embeddedServerList.add(embeddedServer);

        while (!this.isUp("" + portZwei)) {
            Thread.sleep(250);
            LOG.info("tomcat auf port: " + portZwei + " startversuch.");
        }

        LOG.info("tomcat auf port: " + portZwei + " gestartet.");

        int portDrei = getFreePort();

        System.setProperty("ownConnectionString", "http://localhost:" + portDrei + "/app/transport");
        System.setProperty("remoteConnectionString", "http://localhost:" + portZwei + "/app/transport");
        embeddedServer = new TransportTestTomcat(portDrei, "/", "3");
        embeddedServer.start();
        embeddedServerList.add(embeddedServer);

        while (!this.isUp("" + portDrei)) {
            Thread.sleep(250);
            LOG.info("tomcat auf port: " + portDrei + " startversuch.");
        }

        LOG.info("tomcat auf port: " + portDrei + " gestartet.");

        int portVier = getFreePort();

        System.setProperty("ownConnectionString", "http://localhost:" + portVier + "/app/transport");
        System.setProperty("remoteConnectionString", "http://localhost:" + portDrei + "/app/transport");
        embeddedServer = new TransportTestTomcat(portVier, "/", "4");
        embeddedServer.start();
        embeddedServerList.add(embeddedServer);

        while (!this.isUp("" + portDrei)) {
            Thread.sleep(250);
            LOG.info("tomcat auf port: " + portVier + " startversuch.");
        }

        LOG.info("tomcat auf port: " + portVier + " gestartet.");
    }

    private int getFreePort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            serverSocket.close();
            return port;
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return 0;
    }

    private boolean isUp(String port) {
        try {
            URL url;
            url = new URL("http://localhost:" + port + "/app/transport");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                LOG.info(inputLine);
            in.close();
        } catch (IOException e) {
            if (e.getMessage().contains("Server returned HTTP response code: 405 for URL:")) {
                return true;
            }

            return false;
        }
        return false;
    }
}