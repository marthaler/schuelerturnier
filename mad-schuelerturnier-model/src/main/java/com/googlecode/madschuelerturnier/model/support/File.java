/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.support;


import com.googlecode.madschuelerturnier.model.Persistent;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.persistence.*;
import java.io.IOException;

/**
 * Speichert ein beliebiges File in der DB
 * Es existiert die Moeglichkeit den Inhalt als Base64 String zu
 * holen und zu setzen
 * @author $Author: marthaler.worb@gmail.com $
 *
 * @since 1.2.8
 */
@Entity
public class File extends Persistent {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(File.class);

    @Lob
    private byte[] content;

    private String typ;

    private String dateiName;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getDateiName() {
        return dateiName;
    }

    public void setDateiName(String dateiName) {
        this.dateiName = dateiName;
    }

    // spezielle zugriffsmethoden
    public String getContentBase64(){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(this.content);
    }

    public void setContentBase64(String content){
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            this.content = decoder.decodeBuffer(content);
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
    }
}
