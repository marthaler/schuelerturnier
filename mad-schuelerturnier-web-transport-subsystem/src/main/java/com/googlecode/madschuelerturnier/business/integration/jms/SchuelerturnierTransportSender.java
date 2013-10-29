/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.business.zeit.MessageWrapper;
import com.googlecode.madschuelerturnier.model.util.XstreamUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Ist eine ausgehende JMS Verbindung
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class SchuelerturnierTransportSender {

    private static final Logger LOG = Logger.getLogger(SchuelerturnierTransportSender.class);

    private String connString = "";
    private String ownConnection = "";


    private boolean online = false;

    private int messageCount = 0;

    public SchuelerturnierTransportSender(String connection, String ownConnection) {
        this.ownConnection = ownConnection;
        connString = connection;
        createConnection();
        LOG.info("SchuelerturnierTransportSender: sender registriert: "+connection);
    }

    public void sendMessage(String id, Serializable object) {
        sendMessage(id, object, object.getClass().toString());
    }

    private void sendMessage(String id, Serializable object, String typ) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(connString);

        MessageWrapper wr = new MessageWrapper();
        wr.setPayload(object);
        wr.setId(id);
        wr.setTyp(typ);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("payload", XstreamUtil.serializeToString(wr)));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
             StringBuffer buff = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                buff.append(line);
            }
            LOG.info("SchuelerturnierTransportSender response: " + buff.toString());
            LOG.info("SchuelerturnierTransportSender send to: " + connString + " " + wr);
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
    }

    private void createConnection() {
        try {

            this.sendMessage("00", ownConnection, "schuelerturnier-anmeldung");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            online = false;
        }
    }

    public void teardown() {
       LOG.info("SchuelerturnierTransportSender: down: " + this.connString);
    }

}