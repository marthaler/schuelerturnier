/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.messages;

import com.googlecode.madschuelerturnier.model.Persistent;
import org.springframework.context.ApplicationEvent;

import javax.persistence.Entity;
import javax.persistence.OrderColumn;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Ein File zum transportieren und zwischenspeichern
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class File implements Serializable{

    private static final long serialVersionUID = 1L;

    private byte[] content = null;

    private String name = null;

    private String kbyte;


    private String druckzeit;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] contentn) {
         this.content = new byte[contentn.length];
        System.arraycopy(
                contentn, 0,
                this.content, 0,
                content.length
        );

        if( content.length > 0){
            kbyte = "" +content.length / 1000 + " Kb";
        }

        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKbyte() {
        return kbyte;
    }

    public void updateDruckzeit(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        this.druckzeit =   simpleDateFormat.format(new Date());
    }

    public String getDruckzeit() {
        return druckzeit;
    }

}
