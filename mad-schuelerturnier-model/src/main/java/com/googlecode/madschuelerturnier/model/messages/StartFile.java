/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.messages;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Wrapper fuer das Startfile
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class StartFile implements Serializable{

    private static final long serialVersionUID = 1L;

    private byte[] content = null;


    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
