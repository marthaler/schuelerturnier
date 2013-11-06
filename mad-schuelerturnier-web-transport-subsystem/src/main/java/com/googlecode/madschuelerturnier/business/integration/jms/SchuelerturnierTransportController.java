/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.model.enums.MessageTyp;
import com.googlecode.madschuelerturnier.model.messages.IncommingMessage;
import com.googlecode.madschuelerturnier.model.messages.MessageWrapper;
import com.googlecode.madschuelerturnier.model.messages.OutgoingMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.*;

/**
 * Kontrolliert die eingehenden und ausgehenden JMS Verbindungen und verbindet diese mit dem ApplicationEventPublisher
 * Diese Klasse startet den eingebetteten JMS Server
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class SchuelerturnierTransportController extends Thread implements ApplicationEventPublisherAware, ApplicationListener<OutgoingMessage> {

    private LinkedList<MessageWrapper> incommingMessages = new LinkedList<MessageWrapper>();
    private LinkedList<MessageWrapper> outgoingMessages = new LinkedList<MessageWrapper>();

    private Set<String> allIncommingMessagesIDs = new HashSet<String>();
    //private Set<String> allOutgoingMessagesIDs = new HashSet<String>();

    private MessageWrapper registration;

    private Map<String, TransportEndpointSender> senderEndpoints = new HashMap<String, TransportEndpointSender>();

    private final static long WAIT_IF_NO_MESSAGE = 1000;

    @Value("${transport.remote.address:http://localhost:8080/app/transport}")
    private String remoteConnectionString = "http://localhost:8080/app/transport";

    @Value("${transport.local.address:http://localhost:8080/app/transport}")
    private String ownConnectionString = "http://localhost:8080/app/transport";

    private MessageWrapper latest = null;

    private ApplicationEventPublisher applicationEventPublisher = null;

    private static final Logger LOG = Logger.getLogger(SchuelerturnierTransportController.class);

    private Map<String, Serializable> list = new LinkedHashMap<String, Serializable>();

    private boolean running = true;

    public MessageWrapper pullMesageForNode(String id){
         TransportEndpointSender sender = this.senderEndpoints.get(id);
        if(sender == null){
             return null;
        }
        return this.senderEndpoints.get(id).getMessage4Pull();
    }


    public SchuelerturnierTransportController(String ownConnectionString, String remoteConnectionString){
           this.ownConnectionString = ownConnectionString;
           this.remoteConnectionString = remoteConnectionString;
        MessageWrapper wr = new MessageWrapper();
        wr.setSource(this.ownConnectionString);
        // registration request senden
        wr.setTyp(MessageTyp.REGISTRATION_REQUEST);

        this.registration = wr;
        if (remoteConnectionString != null && !"".equals(remoteConnectionString)) {

            if(remoteConnectionString.split(",").length >1){
                String[] arr = remoteConnectionString.split(",");
                for(String ep : arr){
                    createSender(ep);
                }
            } else{
                createSender(remoteConnectionString);
            }



        }




        this.start();
    }



    public MessageWrapper createAckMessage(String id){
        MessageWrapper wrapper = new MessageWrapper();
        wrapper.setSource(ownConnectionString);
        wrapper.setTyp(MessageTyp.ACK);
        wrapper.setPayload(id);
        return wrapper;
    }
                 // von onApplication event
    private void sendMessage(String id, Serializable object, MessageTyp typ) {
        for (TransportEndpointSender send : senderEndpoints.values()) {

            MessageWrapper wr = new MessageWrapper();
            wr.setPayload(object);
            wr.setId(id);
            wr.setTyp(typ);
            wr.setSource(ownConnectionString);

            send.sendMessage(wr);
        }
        list.put(id, object);
    }

    private synchronized void  sendMessage(MessageWrapper wr) {
         this.outgoingMessages.add(wr);
    }

    public MessageWrapper getLatest() {
        return this.latest;
    }

   public  SchuelerturnierTransportController() {
        this.start();
    }



    public void messageFromServlet(MessageWrapper wr){
        if(!allIncommingMessagesIDs.contains(wr.getId()) && wr.getTyp() != MessageTyp.PULLREQUEST){
            this.incommingMessages.add(wr);
            this.allIncommingMessagesIDs.add(wr.getId());
        }
    }

    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }


        while (running) {

            boolean action = false;

            // 1) versenden
           while(outgoingMessages.size()>0){
               action = true;
           MessageWrapper wr = outgoingMessages.removeFirst() ;

                for (TransportEndpointSender send : senderEndpoints.values()) {
                    send.sendMessage(wr);
                }

               if(wr != null && wr.getTyp() == MessageTyp.PAYLOAD){
                   list.put(wr.getId(), wr);
               }
            }



            MessageWrapper wr = null;

             // 3) empfangen
            while(!this.incommingMessages.isEmpty()){
                action = true;
                     wr = this.incommingMessages.removeFirst();


                if (wr != null) {
                        LOG.debug("SchuelerturnierTransportController - Nachricht angekommen: " + wr);

                    if (!list.keySet().contains(wr.getId())) {

                        // registration request behandeln falls noetig
                        if(handleRegistrationRequest(wr)){
                            continue;
                        }


                                latest = wr;
                                IncommingMessage in = new IncommingMessage(this);
                                in.setPayload(wr.getPayload());
                                if (applicationEventPublisher != null) {
                                    this.applicationEventPublisher.publishEvent(in);
                                }
                                sendMessage(wr);
                            }

                }



            }
            if (!action) {
                try {
                    Thread.sleep(WAIT_IF_NO_MESSAGE);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }


    private boolean handleRegistrationRequest(MessageWrapper obj){
        if (obj.getTyp().equals(MessageTyp.REGISTRATION_REQUEST)) {



            TransportEndpointSender sender = null;
            String url = obj.getSource();
            if (!senderEndpoints.containsKey(url)) {
               createSender(url);




                return true;
            }
            this.sendMessage(this.registration);

        }
        return false;
    }

    private synchronized TransportEndpointSender createSender(String url){

        if(this.senderEndpoints.containsKey(url)){
           LOG.info("versuche einen neuen senderEndpoints zu kreieren, den es bereits gibt: "+ url);
            return this.senderEndpoints.get(url);
        }


        TransportEndpointSender send = new TransportEndpointSender(url,this.ownConnectionString,this);

        senderEndpoints.put(url, send);
        this.sendMessage(this.registration);
         return send;
    }


    @PreDestroy
    public void shutdown() {

        for (TransportEndpointSender send : senderEndpoints.values()) {
            send.teardown();
        }


        this.running = false;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(OutgoingMessage event) {
        Serializable obj = event.getPayload();
        this.sendMessage(UUID.randomUUID().toString(), obj, MessageTyp.PAYLOAD);
        }


}