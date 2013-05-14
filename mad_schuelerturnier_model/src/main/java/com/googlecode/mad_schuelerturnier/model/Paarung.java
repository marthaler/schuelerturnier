/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model;

import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Paarung extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1L;

    @OneToOne
    private Mannschaft mannschaftA;

    @OneToOne
    private Mannschaft mannschaftB;

    @OneToOne
    private Spiel spiel;

    @OneToOne
    private Gruppe gruppe;

    private String spielId = null;

    // getter & setter

    public Mannschaft getMannschaftA() {
        return this.spiel.getMannschaftA();
    }

    public void setMannschaftA(final Mannschaft mannschaftA) {
        this.mannschaftA = mannschaftA;
    }

    public Mannschaft getMannschaftB() {
        return this.spiel.getMannschaftB();
    }

    public void setMannschaftB(final Mannschaft mannschaftB) {
        this.mannschaftB = mannschaftB;
    }

    public Spiel getSpiel() {
        return this.spiel;
    }

    public void setSpiel(final Spiel spiel) {
        this.spiel = spiel;
    }

    public Gruppe getGruppe() {
        return gruppe;
    }

    public void setGruppe(Gruppe gruppe) {
        this.gruppe = gruppe;
    }

    public String getSpielId() {
        return spielId;
    }

    public void setSpielId(String spielId) {
        this.spielId = spielId;
    }

    @Override
    public String toString() {
        return "Paarung [mannschaftA=" + this.mannschaftA + ", mannschaftB=" + this.mannschaftB + ", spiel=" + this.spiel + "]";
    }

}
