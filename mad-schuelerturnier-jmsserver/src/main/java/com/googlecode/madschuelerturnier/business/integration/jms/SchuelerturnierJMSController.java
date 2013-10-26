package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.business.zeit.IncommingMessage;
import com.googlecode.madschuelerturnier.business.zeit.OutgoingMessage;
import org.apache.log4j.Logger;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.util.*;

@Component
public class SchuelerturnierJMSController implements ApplicationEventPublisherAware, ApplicationListener<OutgoingMessage> {

    private ApplicationEventPublisher applicationEventPublisher = null;

    private static final Logger LOG = Logger.getLogger(SchuelerturnierJMSController.class);

    private List<SchuelerturnierJMSReceiver> receivers = new ArrayList<SchuelerturnierJMSReceiver>();

    private List<SchuelerturnierJMSSender> sender = new ArrayList<SchuelerturnierJMSSender>();

    private Map<String,Serializable> list = new LinkedHashMap<String,Serializable>();

    private boolean running = true;

    private void sendMessage(String id, Serializable object){
        for(SchuelerturnierJMSSender send: sender){
            send.sendMessage(id,object);
        }
        list.put(id,object);
    }

    private void sendMessage(Serializable object){
        sendMessage(UUID.randomUUID().toString(), object);
    }

    @Async
    private void handleMessage(){

        while(running){

        boolean recevied = false;

        for(SchuelerturnierJMSReceiver receiver: receivers){

            ObjectMessage message =  receiver.receiveMessage();

            if(message != null){
                recevied = true;

                try {
                    if(!list.keySet().contains(message.getStringProperty("id"))){

                        if(message.getStringProperty("typ").equalsIgnoreCase("schuelerturnier-anmeldung")){
                            String url=(String) message.getObject();
                            sender.add(new SchuelerturnierJMSSender(url));
                        } else{
                            IncommingMessage in = new IncommingMessage(this);
                            in.setPayload(message.getObject());
                            this.applicationEventPublisher.publishEvent(in);
                            sendMessage(message.getStringProperty("id"), message.getObject());
                        }
                    }
                } catch (JMSException e) {
                    LOG.error(e.getMessage(),e);
                }
            }

            if(!recevied){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(),e);
                }
            }
        }
        }
    }
    @PreDestroy
    public void shutdown(){

        for(SchuelerturnierJMSReceiver receiver: receivers){
            receiver.teardown();
        }

        for(SchuelerturnierJMSSender send: sender){
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
        this.sendMessage(event.getPayload());
    }
}