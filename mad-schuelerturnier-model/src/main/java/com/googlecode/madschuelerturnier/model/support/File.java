/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.support;


import com.googlecode.madschuelerturnier.model.Persistent;
import org.apache.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Speichert ein beliebiges File in der DB
 * Es existiert die Moeglichkeit den Inhalt als Base64 String zu
 * holen und zu setzen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Entity
public class File extends Persistent {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(File.class);

    @Lob
    private byte[] content;

    // typ des dokuments
    private String typ;

    // id des zugehoerigen datensatzes im restlichen model
    private Long pearID;

    private String mimeType;

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
    public String getContentBase64() {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(this.content);
    }

    public void setContentBase64(String content) {
        this.content = org.apache.commons.codec.binary.Base64.decodeBase64(content);
    }

    public Long getPearID() {
        return pearID;
    }

    public void setPearID(Long pearID) {
        this.pearID = pearID;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSuffix() {
        String[] arr = this.dateiName.split("\\.");
        if (arr.length < 2) {
            return "bin";
        }

        return arr[1];
    }

    // getter und setter fuer xls export und import
    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }

}
