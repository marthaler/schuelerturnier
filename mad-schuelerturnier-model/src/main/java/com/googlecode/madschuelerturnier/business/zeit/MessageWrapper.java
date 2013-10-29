/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.zeit;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * Transportbehaelter fuer Messages
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class MessageWrapper  implements Serializable {

    private static final long serialVersionUID = 1;

    private String id = "";

    private String typ = "";

    private Serializable payload;

    public MessageWrapper() {

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

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    @Override
    public String toString() {
        return "MessageWrapper{" +
                "id='" + id + '\'' +
                ", typ='" + typ + '\'' +
                ", payload=" + payload +
                '}';
    }
}