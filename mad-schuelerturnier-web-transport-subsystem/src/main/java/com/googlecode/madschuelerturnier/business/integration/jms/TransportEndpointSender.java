/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.business.MessageSenderUtil;
import com.googlecode.madschuelerturnier.model.enums.MessageTyp;
import com.googlecode.madschuelerturnier.model.messages.MessageWrapperToSend;
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

    private LinkedList<MessageWrapperToSend> messagesOut = new LinkedList<MessageWrapperToSend>();


    private SchuelerturnierTransportControllerImpl controller;

    public TransportEndpointSender(String remoteConnectionString, String ownConnectionString, SchuelerturnierTransportControllerImpl controller) {
        this.controller = controller;
        this.ownConnectionString = ownConnectionString;
        this.remoteConnectionString = remoteConnectionString;
        this.startuptime = System.currentTimeMillis();

        start();
        LOG.info("TransportEndpointSender ("+remoteConnectionString+"): sender registriert: "+this.ownConnectionString + "-->" +remoteConnectionString);
    }

    public void run(){
        while (online){
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
                LOG.info("TransportEndpointSender ("+remoteConnectionString+") nachricht erhalten: " + response);
                this.controller.messageFromServlet(response);
                }

                if(response == null || response.getTyp() == MessageTyp.ACK){
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                LOG.error("TransportEndpointSender ("+remoteConnectionString+") fehler:"+e.getMessage(),e);
                online = false;
            }
        }
    }

     public MessageWrapperToSend getMessageToSendOutRequest(){
         if(this.messagesOut.size() > 0){
             return this.messagesOut.removeFirst();
         }
          return null;
     }

    public void sendMessage(MessageWrapperToSend wr) {
        messagesOut.add(wr);
    }

    public void teardown() {
       LOG.info("TransportEndpointSender ("+remoteConnectionString+") down: " + this.remoteConnectionString);
    }

    public long getStartuptime() {
        return startuptime;
    }

}