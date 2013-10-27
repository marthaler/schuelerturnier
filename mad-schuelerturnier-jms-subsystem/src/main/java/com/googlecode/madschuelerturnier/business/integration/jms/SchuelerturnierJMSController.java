/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.business.zeit.IncommingMessage;
import com.googlecode.madschuelerturnier.business.zeit.OutgoingMessage;
import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;

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
public class SchuelerturnierJMSController extends Thread implements ApplicationEventPublisherAware, ApplicationListener<OutgoingMessage> {

    private final static long WAIT_IF_NO_MESSAGE = 1000;

    private String connString = "tcp://localhost:6666";

    private ObjectMessage latest = null;

    private ApplicationEventPublisher applicationEventPublisher = null;

    private static final Logger LOG = Logger.getLogger(SchuelerturnierJMSController.class);

    private List<SchuelerturnierJMSReceiver> receivers = new ArrayList<SchuelerturnierJMSReceiver>();

    private Map<String, SchuelerturnierJMSSender> sender = new HashMap<String, SchuelerturnierJMSSender>();

    private Map<String, Serializable> list = new LinkedHashMap<String, Serializable>();

    private boolean running = true;

    private BrokerService broker;

    private void sendMessage(String id, Serializable object) {
        for (SchuelerturnierJMSSender send : sender.values()) {
            send.sendMessage(id, object);
        }
        list.put(id, object);
    }

    public ObjectMessage getLatest() {
        return this.latest;
    }

    SchuelerturnierJMSController(String urlToConnectLocaly, String urlToConnectRemotely) {

        if (urlToConnectLocaly != null && !urlToConnectLocaly.equals("") && !urlToConnectLocaly.contains("default")) {
            this.connString = urlToConnectLocaly;
        }
        init();

        SchuelerturnierJMSSender se = new SchuelerturnierJMSSender(connString);
        sender.put(connString, se);

        SchuelerturnierJMSReceiver rec = new SchuelerturnierJMSReceiver(connString);
        receivers.add(rec);

        if (urlToConnectRemotely != null && !urlToConnectRemotely.equals("") && !urlToConnectRemotely.contains("default")) {
            SchuelerturnierJMSSender se2 = new SchuelerturnierJMSSender(urlToConnectRemotely);
            sender.put(urlToConnectRemotely, se2);

            SchuelerturnierJMSReceiver rec2 = new SchuelerturnierJMSReceiver(urlToConnectRemotely);
            receivers.add(rec2);
        }
        this.start();
    }

    private void init() {
        // startet den lokalen broker
        try {
            broker = new BrokerService();
            broker.addConnector(this.connString);
            broker.setBrokerName("Broker" + this.connString);
            broker.setUseJmx(false);
            broker.start();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    void sendMessage(Serializable object) {
        sendMessage(UUID.randomUUID().toString(), object);
    }

    public void run() {

        while (running) {

            boolean received = false;

            for (SchuelerturnierJMSReceiver receiver : receivers) {

                ObjectMessage message = receiver.receiveMessage();

                if (message != null) {
                    received = true;

                    try {
                        if (!list.keySet().contains(message.getStringProperty("id"))) {

                            if (message.getStringProperty("typ").equalsIgnoreCase("schuelerturnier-anmeldung")) {
                                String url = (String) message.getObject();

                                if (!sender.containsKey(url)) {
                                    sender.put(url, new SchuelerturnierJMSSender(this.connString));
                                }
                            } else {
                                latest = message;
                                IncommingMessage in = new IncommingMessage(this);
                                in.setPayload(message.getObject());
                                if (applicationEventPublisher != null) {
                                    this.applicationEventPublisher.publishEvent(in);
                                }
                                sendMessage(message.getStringProperty("id"), message.getObject());
                            }
                        }
                    } catch (JMSException e) {
                        LOG.error(e.getMessage(), e);
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
    }

    @PreDestroy
    public void shutdown() {

        for (SchuelerturnierJMSReceiver receiver : receivers) {
            receiver.teardown();
        }

        for (SchuelerturnierJMSSender send : sender.values()) {
            send.teardown();
        }
        try {
            this.broker.stop();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
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