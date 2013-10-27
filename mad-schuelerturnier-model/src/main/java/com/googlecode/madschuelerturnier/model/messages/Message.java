/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.messages;

import com.googlecode.madschuelerturnier.model.enums.MessageTyp;

import java.io.Serializable;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private MessageTyp typ;

    public MessageTyp getTyp() {
        return typ;
    }

    public void setTyp(MessageTyp typ) {
        this.typ = typ;
    }


}
