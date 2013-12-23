/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.messages;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * Ausgehende Message zum verschicken an anderen Instanzen, die Payload beinhaltet das Nachrichtenobjekt
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class OutgoingMessage extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 1;

    private boolean trans = false;

    private Serializable payload;

    public OutgoingMessage(Object source) {
        super(source);
    }

    public Serializable getPayload() {
        return payload;
    }

    public void setPayload(Serializable payload) {
        this.payload = payload;
    }

    public boolean isTrans() {
        return trans;
    }

    public void setTrans(boolean trans) {
        this.trans = trans;
    }
}