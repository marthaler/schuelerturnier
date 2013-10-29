/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.business.zeit.IncommingMessage;
import com.googlecode.madschuelerturnier.business.zeit.MessageWrapper;
import com.googlecode.madschuelerturnier.business.zeit.OutgoingMessage;
import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
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

    private LinkedList<MessageWrapper> incomming= new LinkedList<MessageWrapper>();

    private final static long WAIT_IF_NO_MESSAGE = 1000;

    private String connString = "http://localhost:8080/app/transport";
    private String ownConnection = "http://localhost:8080/app/transport";


    @Value("${transport.local.url:localhost}")
    String transportLocalUrl = "localhost";
    @Value("${transport.local.port:8080/app/transport}")
    String transportLocalPort = "8080/app/transport";
    @Value("${transport.remote.url:default}")
    String transportRemoteUrl = "default";
    @Value("${transport.remote.port:8080/app/transport}")
    String transportRemotePort = "8080/app/transport";


    private MessageWrapper latest = null;

    private ApplicationEventPublisher applicationEventPublisher = null;

    private static final Logger LOG = Logger.getLogger(SchuelerturnierTransportController.class);


    private Map<String, SchuelerturnierTransportSender> sender = new HashMap<String, SchuelerturnierTransportSender>();

    private Map<String, Serializable> list = new LinkedHashMap<String, Serializable>();

    private boolean running = true;


    private void sendMessage(String id, Serializable object) {
        for (SchuelerturnierTransportSender send : sender.values()) {
            send.sendMessage(id, object);
        }
        list.put(id, object);
    }

    public MessageWrapper getLatest() {
        return this.latest;
    }

    SchuelerturnierTransportController() {

        this.start();
    }

    public void init(){
        if (transportLocalUrl != null && !transportLocalUrl.equals("") && !transportLocalUrl.contains("default")) {
            this.connString = "http://" + transportLocalUrl + ":" + transportLocalPort;
        }

        if (transportRemoteUrl != null && !transportRemoteUrl.equals("") && !transportRemoteUrl.contains("default")) {
            String remote  = "http://" + transportRemoteUrl + ":" + transportRemotePort;
            SchuelerturnierTransportSender se2 = new SchuelerturnierTransportSender(remote,this.connString);
            sender.put(remote, se2);

        }
    }

    void sendMessage(Serializable object) {
        sendMessage(UUID.randomUUID().toString(), object);
    }

    public void onMessage(MessageWrapper o){
       this.incomming.add(o);
    }

    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        init();

        while (running) {

            boolean received = false;

            MessageWrapper obj = null;
                if(!this.incomming.isEmpty()){
                     obj = this.incomming.removeFirst();
                }

                if (obj != null) {
                        LOG.info("SchuelerturnierTransportController nachricht angekommen " + obj);
                        if (!list.keySet().contains(obj.getId())) {

                            if (obj.getTyp().equalsIgnoreCase("schuelerturnier-anmeldung")) {
                                String url = (String) obj.getPayload();

                                if (!sender.containsKey(url)) {
                                    sender.put(url, new SchuelerturnierTransportSender(this.connString,this.ownConnection));
                                }
                            } else {
                                latest = obj;
                                IncommingMessage in = new IncommingMessage(this);
                                in.setPayload(obj.getPayload());
                                if (applicationEventPublisher != null) {
                                    this.applicationEventPublisher.publishEvent(in);
                                }
                                sendMessage(obj.getId(), obj.getPayload());
                            }
                        }
                }

                if (!received) {
                    try {
                        Thread.sleep(WAIT_IF_NO_MESSAGE);
                    } catch (InterruptedException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }

        }
    }

    @PreDestroy
    public void shutdown() {

        for (SchuelerturnierTransportSender send : sender.values()) {
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
        this.sendMessage(obj);
    }


}