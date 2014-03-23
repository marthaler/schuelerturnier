/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.integration;

import com.googlecode.madschuelerturnier.model.enums.MessageTyp;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Transportbehaelter fuer Messages
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class MessageWrapperToSend implements Serializable {

    private static final long serialVersionUID = 1;

    private String id = UUID.randomUUID().toString();

    private String destination = "";

    private String source = "";

    private MessageTyp typ = MessageTyp.PAYLOAD;

    private Serializable payload;

    private List<String> filter;

    private boolean resend = false;

    private boolean trans = false;

    private Set<String> nodesThatReceivedThisMessage = new HashSet<String>();

    public MessageWrapperToSend() {

    }

    public Serializable getPayload() {
        return payload;
    }

    public void setPayload(Serializable payload) {
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "MessageWrapperToSend{" +
                "id='" + id.substring(0, 8) + '\'' +
                ", source='" + source.replace("http://", "").replace("/app/transport", "") + '\'' +
                ", dest='" + destination.replace("http://", "").replace("/app/transport", "") + '\'' +
                ", typ=" + typ +
                ", payload=" + payload +
                '}';
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MessageTyp getTyp() {
        return typ;
    }

    public void setTyp(MessageTyp typ) {
        this.typ = typ;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isResend() {
        return resend;
    }

    public void setResend(boolean resend) {
        this.resend = resend;
    }

    public List<String> getFilter() {
        return filter;
    }

    public void setFilter(List<String> filter) {
        this.filter = filter;
    }

    public boolean isTrans() {
        return trans;
    }

    public void setTrans(boolean trans) {
        this.trans = trans;
    }

    public Set<String> getNodesThatReceivedThisMessage() {
        return nodesThatReceivedThisMessage;
    }

    public void setNodesThatReceivedThisMessage(Set<String> nodesThatReceivedThisMessage) {
        this.nodesThatReceivedThisMessage = nodesThatReceivedThisMessage;
    }
}