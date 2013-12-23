/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.messages.state;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * Signalisiert einen gesetzten Master
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class MasterState implements Serializable {

    private static final long serialVersionUID = 1;

    private String nameOfMaster;

    public String getNameOfMaster() {
        return nameOfMaster;
    }

    public void setNameOfMaster(String nameOfMaster) {
        this.nameOfMaster = nameOfMaster;
    }
}