/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.messages;

import com.googlecode.madschuelerturnier.model.Persistent;
import org.springframework.context.ApplicationEvent;

import javax.persistence.Entity;
import javax.persistence.OrderColumn;
import java.io.Serializable;

/**
 * Ein File zum transportieren
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class File implements Serializable{

    private static final long serialVersionUID = 1L;

    private byte[] content = null;

    private String name = null;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
