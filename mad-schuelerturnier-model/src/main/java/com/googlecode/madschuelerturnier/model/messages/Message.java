/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.messages;

import com.googlecode.madschuelerturnier.model.Persistent;
import com.googlecode.madschuelerturnier.model.enums.MessageTyp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.io.Serializable;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
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
