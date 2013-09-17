/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import com.googlecode.madschuelerturnier.exceptions.TurnierException;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import org.apache.log4j.Logger;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Gruppe extends Persistent {

    private static final long serialVersionUID = 1L;

    private static final String GRUPPENKUERZEL = "Gr";
    private static final Logger LOG = Logger.getLogger(Gruppe.class);

    @OneToOne(cascade = CascadeType.ALL)
    private Kategorie kategorie = null;

    private GeschlechtEnum geschlecht = null;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Mannschaft> mannschaften = new ArrayList<Mannschaft>();

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Spiel> spiele = new ArrayList<Spiel>();

    /**
     * Dient dem ausfuellen des Geschlecht Feldes vor dem Speichern, uber den
     * getGeschlecht Aufruf
     */
    @PrePersist //NOSONAR
    private void geschlechtFeldAusfuellen() {
        getGeschlecht();
    }

    /**
     * Gibt das Geschlecht der Gruppe zurueck. Dies geschieht aufgrund des
     * Geschlechs der Mannschaften falls eine Gemischte Gruppe gefunden wird,
     * wird null zurueckgegeben und eine Warning ins Log geschrieben
     *
     * @return geschlecht der gruppe
     */
    public GeschlechtEnum getGeschlecht() {

        if (geschlecht != null) {
            return geschlecht;
        }

        if (this.kategorie != null && this.kategorie.getMannschaften() != null && this.kategorie.getMannschaften().size() > 0) {
            geschlecht = this.kategorie.getMannschaften().get(0).getGeschlecht();
        }

        // fall, dass kategorie noch nicht gesetzt ist aber bereits mannschaften in der gruppe sind
        if (this.mannschaften != null && this.mannschaften.size() > 0) {
            geschlecht = this.mannschaften.get(0).getGeschlecht();
        }

        return geschlecht;
    }


    /**
     * fuegt eine mannschaft der Liste hinzu wenn versucht wird eine mannschaft
     * einzufuegen, die eine gemischte gruppe zur folge haette, fliegt eine
     * TurnierException
     *
     * @param mannschaft die Mannschaft zum adden
     */
    public void addMannschaft(final Mannschaft mannschaft) throws TurnierException {
        GeschlechtEnum basis;
        if (this.mannschaften.size() > 1) {
            basis = this.mannschaften.get(0).getGeschlecht();
        } else {
            this.mannschaften.add(mannschaft);
            return;
        }

        for (final Mannschaft vergleich : this.mannschaften) {
            if (vergleich.getGeschlecht() != basis) {
                throw new TurnierException("!!! achtung, gemischte gruppen sind nicht erlaubt");
            }
        }
        this.getGeschlecht();
        this.mannschaften.add(mannschaft);
    }

    public String getName() {
        String retValue = "" + getGeschlecht() + Gruppe.GRUPPENKUERZEL;
        if (this.kategorie == null) {
            Gruppe.LOG.warn("!!! es wurde versucht einen gruppennamen auszugeben von einer gruppe ohne kategorie: " + this.toString());
            return "" + retValue + "_OHNE_KATEGORIE";
        }

        final String klasse = this.kategorie.getKlassenString();
        retValue += klasse;

        // A oder B?
        if ((this.kategorie.getGruppeA() == this) && (this.kategorie.getGruppeB() != null)) {
            retValue += "A";
        }
        if ((this.kategorie.getGruppeB() == this) && (this.kategorie.getGruppeA() != null)) {
            retValue += "B";
        }

        return retValue;
    }


    // getter & setter normal
    public List<Mannschaft> getMannschaften() {

        if (this.kategorie == null || this.kategorie.getGruppeB() == null || this.kategorie.getGruppeA() == null) {
            return this.mannschaften;
        }

        // workaround weil JPA anscheinend auf der kategorie den gruppennicht die selben Mannschaften zuordnen kann
        if (this == this.kategorie.getGruppeB() && this.kategorie.getGruppeA().mannschaften.size() == 3) {
            return this.kategorie.getGruppeA().getMannschaften();
        }

        return this.mannschaften;
    }

    public void setMannschaften(final List<Mannschaft> mannschaften) {
        this.mannschaften = mannschaften;
        // initialisiere Geschlecht
        this.getGeschlecht();
    }

    public Kategorie getKategorie() {
        return this.kategorie;
    }

    public void setKategorie(final Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public List<Spiel> getSpiele() {
        return this.spiele;
    }

    public void setSpiele(List<Spiel> spiele) {
        this.spiele = spiele;
    }

    public void setGeschlecht(GeschlechtEnum geschlecht) {
        this.geschlecht = geschlecht;
    }

    @Override
    public String toString() {
        return "Gruppe [geschlecht=" + geschlecht + ", mannschaften=" + mannschaften + "]";
    }

}
