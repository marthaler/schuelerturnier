/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.core;

import com.googlecode.madschuelerturnier.business.integration.IntegrationControllerImpl;
import com.googlecode.madschuelerturnier.business.integration.util.MessageSenderUtil;
import com.googlecode.madschuelerturnier.model.enums.MessageTyp;
import com.googlecode.madschuelerturnier.model.integration.MessageWrapperToSend;
import org.apache.log4j.Logger;

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
    private boolean running = true;

    private LinkedList<MessageWrapperToSend> messagesOut = new LinkedList<MessageWrapperToSend>();


    private IntegrationControllerImpl controller;

    public TransportEndpointSender(String remoteConnectionString, String ownConnectionString, IntegrationControllerImpl controller) {
        this.controller = controller;
        this.ownConnectionString = ownConnectionString;
        this.remoteConnectionString = remoteConnectionString;
        this.startuptime = System.currentTimeMillis();

        start();
        LOG.info("TransportEndpointSender ("+remoteConnectionString+"): sender registriert: "+this.ownConnectionString + "-->" +remoteConnectionString);
    }

    public void run(){
        while (running){
            try {

                MessageWrapperToSend wrapper = new MessageWrapperToSend();
                wrapper.setSource(ownConnectionString);
                wrapper.setDestination(remoteConnectionString);
                wrapper.setTyp(MessageTyp.PULLREQUEST);
                wrapper.setFilter(this.controller.getMessagefilter());

                MessageWrapperToSend send = getMessageToSendOutRequest();
                if(send != null){
                      wrapper = send;
                    LOG.info("TransportEndpointSender ("+remoteConnectionString+") sende nachricht: " + wrapper);
                }

                MessageWrapperToSend response = MessageSenderUtil.send(remoteConnectionString, wrapper);

                if(response != null && response.getTyp() != MessageTyp.ACK){
                online = true;
                LOG.info("TransportEndpointSender ("+remoteConnectionString+") nachricht erhalten: " + response);
                this.controller.messageReceivedFromRemote(response);
                }

                if(response == null){
                    online = false;
                    Thread.sleep(5000);
                } else

                if(response.getTyp() == MessageTyp.ACK){
                    online = true;
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                LOG.error("TransportEndpointSender ("+remoteConnectionString+") fehler:"+e.getMessage(),e);
                online = false;
            }
        }
    }

     public synchronized  MessageWrapperToSend getMessageToSendOutRequest(){
         if(this.messagesOut.size() > 0){
             MessageWrapperToSend out = this.messagesOut.removeFirst();

                 return out;

         }
          return null;
     }

    public void sendMessage(MessageWrapperToSend wr) {
        // nur schicken wenn nicht bereits schon mal geschickt
        if(!wr.getNodesThatReceivedThisMessage().contains(this.ownConnectionString) && !wr.getSource().equals(this.remoteConnectionString)){
            wr.getNodesThatReceivedThisMessage().add(this.ownConnectionString);
            messagesOut.add(wr);
        }
    }

    public void teardown() {
        this.running = false;
       LOG.info("TransportEndpointSender ("+remoteConnectionString+") down: " + this.remoteConnectionString);
    }

    public long getStartuptime() {
        return startuptime;
    }

    public String getOwnConnectionString() {
        return ownConnectionString;
    }

    public boolean isOnline() {
        return online;
    }

    public int countMessagesToSend(){
        return this.messagesOut.size();
    }

}