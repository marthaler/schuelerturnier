/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.model.comperators.SpielZeitComperator;
import com.googlecode.madschuelerturnier.model.enums.SpielTageszeit;
import com.googlecode.madschuelerturnier.model.spiel.Penalty;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.*;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Kategorie extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1L;
    private static final String KATEGORIEKUERZEL = "Kl";
    private static final Logger LOG = Logger.getLogger(Kategorie.class);

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Spiel kleineFinal = null;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Spiel grosserFinal = null;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Gruppe gruppeA;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Gruppe gruppeB;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Penalty penaltyA = null;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Penalty penaltyB = null;

    private SpielTageszeit spielwunsch = SpielTageszeit.EGAL;

    public Kategorie() {

    }


    public String getName() {

        if (this.getGruppeA() == null) {
            return "invalide";
        }

        String retValue = "" + this.gruppeA.getGeschlecht()
                + Kategorie.KATEGORIEKUERZEL;
        if (this.gruppeA == null) {
            Kategorie.LOG
                    .warn("!!! es wurde versucht einen kategorienamen auszugeben von einer kategorie ohne gruppeA: "
                            + this.toString());
            return "" + retValue + "_OHNE_GRUPPE_A";
        }
        // klassenteil
        retValue += this.getKlassenString();
        return retValue;
    }

    public String getKlassenString() {
        // klassenteil
        String klasse = "";

        final List<Integer> klassen = getKlassen();
        for (final Integer klasseEnum : klassen) {
            klasse = klasse + klasseEnum + "&";
        }
        if (klasse.endsWith("&")) {
            klasse = klasse.substring(0, klasse.length() - 1);
        }
        return klasse;
    }

    public List<Integer> getKlassen() {

        final Set<Integer> result = new HashSet<Integer>();

        if (this.gruppeA != null) {
            for (final Mannschaft mannschaft : this.gruppeA.getMannschaften()) {
                result.add(mannschaft.getKlasse());
            }
        }

        if (this.gruppeB != null) {
            for (final Mannschaft mannschaft : this.gruppeB.getMannschaften()) {
                result.add(mannschaft.getKlasse());
            }
        }

        final List<Integer> resultList = new ArrayList<Integer>();
        resultList.addAll(result);
        Collections.sort(resultList);
        return resultList;
    }

    public List<Mannschaft> getMannschaftenSorted() {
        List<Mannschaft> list = getMannschaften();
        Collections.sort(list, new MannschaftsNamenComperator());
        return list;
    }

    public List<Mannschaft> getMannschaften() {

        final Set<Mannschaft> result = new HashSet<Mannschaft>();

        if (this.gruppeA != null) {
            for (final Mannschaft mannschaft : this.gruppeA.getMannschaften()) {
                result.add(mannschaft);
            }
        }

        if (this.gruppeB != null) {
            for (final Mannschaft mannschaft : this.gruppeB.getMannschaften()) {
                result.add(mannschaft);
            }
        }

        final List<Mannschaft> resultList = new ArrayList<Mannschaft>();
        resultList.addAll(result);
        Collections.sort(resultList, new MannschaftsNamenComperator());
        return resultList;
    }


    public List<Spiel> getDirektbegegnungen(Mannschaft a, Mannschaft b) {

        List<Spiel> result = new ArrayList<Spiel>();
        Set<Spiel> alle = getSpiele();
        for (Spiel sp : alle) {
            if (sp.getMannschaftAName().equals(a.getName()) && sp.getMannschaftBName().equals(b.getName())) {
                result.add(sp);
            } else if (sp.getMannschaftBName().equals(a.getName()) && sp.getMannschaftAName().equals(b.getName())) {
                result.add(sp);
            }
        }
        return result;
    }


    /**
     * Ist immer dann der Fall, wenn nur 3 Manschaften in der Gruppe sind,
     * weniger als zwei werden einer anderen Kategorie zugeteilt mehr spielen
     * regulaer
     *
     * @return true oder false
     */
    public boolean hasVorUndRueckrunde() {
        if (this.gruppeA == null) {
            return false;
        }
        return (gruppeA.getMannschaften().size() == 3);
    }


    public boolean has2Groups() {
        if (this.gruppeB != null && this.gruppeB.getMannschaften().size() > 0) {
            return true;
        }
        return false;
    }

    public Set<Spiel> getSpiele() {
        Set<Spiel> spiele = new HashSet<Spiel>();
        if (this.gruppeA != null) {
            spiele.addAll(this.gruppeA.getSpiele());
        }
        if (this.gruppeB != null) {
            spiele.addAll(this.gruppeB.getSpiele());
        }
        return spiele;
    }

    public List<Spiel> getSpieleSorted() {
        List<Spiel> spiele = new ArrayList<Spiel>();
        spiele.addAll(getSpiele());
        Collections.sort(spiele, new SpielZeitComperator());
        return spiele;
    }

    public Spiel getLatestSpiel() {
        return getSpieleSorted().remove(getSpieleSorted().size() - 1);
    }

    public boolean isGruppenspieleFertig() {

        List<Spiel> spiele = getSpieleSorted();

        for (Spiel spiel : spiele) {
            if (!spiel.isFertigBestaetigt()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFertigGespielt() {

        if (this.isGruppenspieleFertig() && this.getGrosserFinal().isFertigBestaetigt()) {
            if (this.kleineFinal != null) {
                if (!this.kleineFinal.isFertigBestaetigt()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // normale getter & setter

    public Spiel getKleineFinal() {
        return this.kleineFinal;
    }

    public Spiel getGrosserFinal() {
        return this.grosserFinal;
    }

    public Gruppe getGruppeA() {
        return this.gruppeA;
    }

    public Gruppe getGruppeB() {
        return this.gruppeB;
    }

    public void setGruppeA(final Gruppe gruppeA) {
        this.gruppeA = gruppeA;
    }

    public void setGruppeB(final Gruppe gruppeB) {
        this.gruppeB = gruppeB;
    }

    public Penalty getPenaltyA() {
        return this.penaltyA;
    }

    public Penalty getPenaltyB() {
        return this.penaltyB;
    }

    public void setPenaltyA(Penalty penalty) {
        this.penaltyA = penalty;
    }

    public void setPenaltyB(Penalty penalty) {
        this.penaltyB = penalty;
    }

    public void setKleineFinal(Spiel kleineFinal) {
        this.kleineFinal = kleineFinal;
    }

    public void setGrosserFinal(Spiel grosserFinal) {
        this.grosserFinal = grosserFinal;
    }

    public SpielTageszeit getSpielwunsch() {
        return spielwunsch;
    }

    public void setSpielwunsch(SpielTageszeit spielwunsch) {
        this.spielwunsch = spielwunsch;
    }

    @Override
    public String toString() {
        return "Kategorie [kleineFinal=" + kleineFinal + ", grosserFinal=" + grosserFinal + ", gruppeA=" + gruppeA + ", gruppeB=" + gruppeB + ", penaltyA=" + penaltyA + ", penaltyB=" + penaltyB + "]";
    }


}
