/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import com.googlecode.madschuelerturnier.model.spiel.Spiel;
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


    @Override
    public String toString() {
        return "Paarung [mannschaftA=" + this.mannschaftA + ", mannschaftB=" + this.mannschaftB + ", spiel=" + this.spiel + "]";
    }

}
