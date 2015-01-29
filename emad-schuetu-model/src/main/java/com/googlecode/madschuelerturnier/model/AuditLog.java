/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Dient zum Loggen der Anmeldungen und fehlgeschlagenen Anmeldeversuchen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Entity
public class AuditLog extends Persistent {

    private Date datum = new Date();
    private String text;
    private String level;

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
