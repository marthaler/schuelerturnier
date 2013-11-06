/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.business.MessageSenderUtil;
import com.googlecode.madschuelerturnier.model.enums.MessageTyp;
import com.googlecode.madschuelerturnier.model.messages.MessageWrapper;
import com.googlecode.madschuelerturnier.model.util.XstreamUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

/**
 * Ist eine ausgehende HTTP Verbindung
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class TransportEndpointSender extends Thread{

    private static final Logger LOG = Logger.getLogger(TransportEndpointSender.class);

    private long startuptime;

    public String getRemoteConnectionString() {
        return remoteConnectionString;
    }

    private String remoteConnectionString = "";
    private String ownConnectionString = "";


    private boolean online = true;

    private LinkedList<MessageWrapper> messages = new LinkedList<MessageWrapper>();


    private SchuelerturnierTransportController controller;

    public TransportEndpointSender(String remoteConnectionString, String ownConnectionString,SchuelerturnierTransportController controller) {
        this.controller = controller;
        this.ownConnectionString = ownConnectionString;
        this.remoteConnectionString = remoteConnectionString;
        this.startuptime = System.currentTimeMillis();

        start();
        LOG.info("TransportEndpointSender: sender registriert: "+this.ownConnectionString + "-->" +remoteConnectionString);
    }

    public void run(){
        while (online){
            try {

                MessageWrapper wrapper = new MessageWrapper();
                wrapper.setSource(ownConnectionString);
                wrapper.setDestination(remoteConnectionString);
                wrapper.setTyp(MessageTyp.PULLREQUEST);

                MessageWrapper send = getMessage4Pull();
                if(send != null){
                      wrapper = send;
                }

                MessageWrapper response = MessageSenderUtil.send(remoteConnectionString, wrapper);

                if(response == null){
                    online = false;
                    break;
                }
                // ack message falls keine echte n
                if(response.getTyp().equals(MessageTyp.PAYLOAD)){
                    LOG.debug("echte nachricht erhalten: " + response);
                    this.controller.messageFromServlet(response);
                } else{
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
                online = false;
            }
        }
    }

     public MessageWrapper getMessage4Pull(){
         if(this.messages.size() > 0){
             return this.messages.removeFirst();
         }
          return null;
     }

    public void sendMessage(MessageWrapper wr) {
        messages.add(wr);
    }


    public void teardown() {
       LOG.info("TransportEndpointSender: down: " + this.remoteConnectionString);
    }


    public long getStartuptime() {
        return startuptime;
    }




}