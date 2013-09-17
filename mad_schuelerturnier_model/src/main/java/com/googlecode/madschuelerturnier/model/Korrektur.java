/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.OrderColumn;

/**
 * Dient dazu, Korrekturen bei der Kategoriezuordnung und bei der Spielzuordnung aufzunehmen und zu speichern
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@Entity
public class Korrektur extends Persistent {

    private static final long serialVersionUID = 1L;

    private String typ = null;

    private String wert = null;

    @OrderColumn
    private long reihenfolge;

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getWert() {
        return wert;
    }

    public void setWert(String wert) {
        this.wert = wert;
    }

    public long getReihenfolge() {
        return reihenfolge;
    }

    public void setReihenfolge(long reihenfolge) {
        this.reihenfolge = reihenfolge;
    }

    // getter und setter fuer xls export und import
    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }
}
