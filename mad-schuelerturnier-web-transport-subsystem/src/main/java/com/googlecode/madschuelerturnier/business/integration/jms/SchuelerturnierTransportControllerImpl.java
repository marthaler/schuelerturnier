/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.model.enums.MessageTyp;
import com.googlecode.madschuelerturnier.model.messages.IncommingMessage;
import com.googlecode.madschuelerturnier.model.messages.MessageWrapperToSend;
import com.googlecode.madschuelerturnier.model.messages.OutgoingMessage;
import com.googlecode.madschuelerturnier.model.messages.state.MasterState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
public class SchuelerturnierTransportControllerImpl extends Thread implements ApplicationEventPublisherAware, ApplicationListener<OutgoingMessage>, SchuelerturnierTransportController {

    private final static long WAIT_IF_NO_MESSAGE = 1000;
    private static final Logger LOG = Logger.getLogger(SchuelerturnierTransportControllerImpl.class);
    private boolean master;
    private LinkedList<MessageWrapperToSend> incommingMessages = new LinkedList<MessageWrapperToSend>();
    //private Set<String> allOutgoingMessagesIDs = new HashSet<String>();
    private LinkedList<MessageWrapperToSend> outgoingMessages = new LinkedList<MessageWrapperToSend>();
    private Set<String> allIncommingMessagesIDs = new HashSet<String>();
    private MessageWrapperToSend registration;
    private Map<String, TransportEndpointSender> senderEndpoints = new HashMap<String, TransportEndpointSender>();
    private List<String> messagefilter = new ArrayList<String>();
    @Value("${transport.remote.address:http://localhost:8080/app/transport}")
    private String remoteConnectionString = "http://localhost:8080/app/transport";
    @Value("${transport.local.address:http://localhost:8080/app/transport}")
    private String ownConnectionString = "http://localhost:8080/app/transport";
    private MessageWrapperToSend latest = null;
    private ApplicationEventPublisher applicationEventPublisher = null;
    private Map<String, Serializable> list = new LinkedHashMap<String, Serializable>();

    private boolean running = true;

    public SchuelerturnierTransportControllerImpl() {

    }

    public SchuelerturnierTransportControllerImpl(String ownAddress, String remoteAddress) {
        this.ownConnectionString = ownAddress;
        this.remoteConnectionString = remoteAddress;
    }

    public MessageWrapperToSend pullMessageForNode(String id) {
        TransportEndpointSender sender = this.senderEndpoints.get(id);
        if (sender == null) {
            return null;
        }
        return this.senderEndpoints.get(id).getMessageToSendOutRequest();
    }

    @PostConstruct
    private void init() {
        MessageWrapperToSend wr = new MessageWrapperToSend();
        wr.setSource(this.ownConnectionString);
        // registration request senden
        wr.setTyp(MessageTyp.REGISTRATION_REQUEST);

        this.registration = wr;
        if (remoteConnectionString != null && !"".equals(remoteConnectionString)) {

            if (remoteConnectionString.split(",").length > 1) {
                String[] arr = remoteConnectionString.split(",");
                for (String ep : arr) {
                    createSender(ep);
                }
            } else {
                createSender(remoteConnectionString);
            }
        }
        this.start();
    }

    public MessageWrapperToSend createAckMessage(String id) {
        MessageWrapperToSend wrapper = new MessageWrapperToSend();
        wrapper.setSource(ownConnectionString);
        wrapper.setTyp(MessageTyp.ACK);
        wrapper.setPayload(id);
        return wrapper;
    }

    // von onApplication event
    private void sendMessage(String id, Serializable object, MessageTyp typ, boolean trans) {
        for (TransportEndpointSender send : senderEndpoints.values()) {

            MessageWrapperToSend wr = new MessageWrapperToSend();
            wr.setPayload(object);
            wr.setId(id);
            wr.setTyp(typ);
            wr.setTrans(trans);
            wr.setSource(ownConnectionString);

            send.sendMessage(wr);
        }
        list.put(id, object);
    }

    private synchronized void sendMessage(MessageWrapperToSend wr) {
        this.outgoingMessages.add(wr);
    }

    public MessageWrapperToSend getLatest() {
        return this.latest;
    }


    public void messageFromServlet(MessageWrapperToSend wr) {
        this.incommingMessages.add(wr);
    }

    public void run() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }

        while (running) {

            boolean sleepFlag = true;

            // 1) versenden
            while (outgoingMessages.size() > 0) {

                sleepFlag = false;

                MessageWrapperToSend outgoingMessage = outgoingMessages.removeFirst();

                if (outgoingMessage != null) {

                    for (TransportEndpointSender send : senderEndpoints.values()) {
                        send.sendMessage(outgoingMessage);
                    }

                    if (outgoingMessage.getTyp() == MessageTyp.PAYLOAD) {

                        // setzen der ausgehenden messages in zwischenspeicher
                        if (outgoingMessage.isTrans()) {
                            list.put(outgoingMessage.getId(), null);
                        } else {
                            list.put(outgoingMessage.getId(), outgoingMessage);

                        }
                    }
                }
            }

            MessageWrapperToSend incommingMessage = null;

            // 2) empfangen
            while (!this.incommingMessages.isEmpty()) {
                sleepFlag = false;
                incommingMessage = this.incommingMessages.removeFirst();

                if (incommingMessage != null && !allIncommingMessagesIDs.contains(incommingMessage.getId())) {

                    LOG.debug("SchuelerturnierTransportController: nachricht angekommen: " + incommingMessage);
                    allIncommingMessagesIDs.add(incommingMessage.getId());
                    // registration request behandeln falls noetig
                    if (handleRegistrationRequest(incommingMessage)) {
                        continue;
                    }

                    // master state behandeln
                    if (handleMasterState(incommingMessage)) {
                        continue;
                    }

                    latest = incommingMessage;

                    IncommingMessage in = new IncommingMessage(this);
                    in.setPayload(incommingMessage.getPayload());
                    if (applicationEventPublisher != null) {
                        this.applicationEventPublisher.publishEvent(in);
                    }

                    if (!incommingMessage.isTrans()) {
                        list.put(incommingMessage.getId(), incommingMessage);
                        sendMessage(incommingMessage);
                    }
                }
            }

            if(incommingMessage != null){
                allIncommingMessagesIDs.add(incommingMessage.getId());
            }

            if (sleepFlag) {
                try {
                    Thread.sleep(WAIT_IF_NO_MESSAGE);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    private boolean handleRegistrationRequest(MessageWrapperToSend obj) {
        if (obj.getTyp().equals(MessageTyp.REGISTRATION_REQUEST)) {
            TransportEndpointSender sender = null;
            String url = obj.getSource();
            if (!senderEndpoints.containsKey(url)) {
                createSender(url);
                return true;
            }
            this.sendMessage(registration);
            this.sendMessage(obj);
        }
        return false;
    }

    private boolean handleMasterState(MessageWrapperToSend obj) {

        if (obj.getPayload() instanceof MasterState) {
            this.master = true;
            this.sendMessage(obj);
            return true;
        }

        return false;
    }

    private synchronized TransportEndpointSender createSender(String url) {

        if (this.senderEndpoints.containsKey(url)) {
            LOG.info("versuche einen neuen senderEndpoints zu kreieren, den es bereits gibt: " + url);
            return this.senderEndpoints.get(url);
        }

        TransportEndpointSender send = new TransportEndpointSender(url, this.ownConnectionString, this);
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
        this.sendMessage(UUID.randomUUID().toString(), obj, MessageTyp.PAYLOAD, event.isTrans());
    }

    public List<String> getMessagefilter() {
        return messagefilter;
    }

    public void setMessagefilter(List<String> messagefilter) {
        this.messagefilter = messagefilter;
    }

    @Override
    public boolean isMaster() {
        return this.master;
    }

    @Override
    public void setMaster(boolean master) {
        if (master) {
            this.master = true;
            MasterState ms = new MasterState();
            OutgoingMessage m = new OutgoingMessage(ms);
            m.setPayload(ms);
            m.setTrans(true);
            this.onApplicationEvent(m);
        }
    }
}