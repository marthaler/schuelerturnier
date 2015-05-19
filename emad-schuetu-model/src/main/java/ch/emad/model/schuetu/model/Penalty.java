/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model;

import ch.emad.model.schuetu.exceptions.TurnierException;
import org.apache.log4j.Logger;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Penalty extends Persistent {

    private static final Logger LOG = Logger.getLogger(Penalty.class);


    private static final String LEER = "bitte_reihenfolge_angeben";

    private static final long serialVersionUID = 1L;

    private String reihenfolgeOrig = LEER;

    private String reihenfolge = LEER;

    private String idString;

    private boolean gespielt = false;
    private boolean bestaetigt = false;

    @OneToOne
    private Gruppe gruppe = null;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Mannschaft> finalList = new ArrayList<Mannschaft>();


    public void addMannschaftInitial(final Mannschaft m) {
        this.finalList.add(m);
        reihenfolgeOrig = "";
        for (Mannschaft m2 : finalList) {
            reihenfolgeOrig = reihenfolgeOrig + "," + m2.getName();
        }
        reihenfolgeOrig = reihenfolgeOrig.substring(1);
    }

    public void addMannschaft(final Mannschaft m) {
        this.finalList.add(m);
        reihenfolge = "";
        for (Mannschaft m2 : finalList) {
            reihenfolge = reihenfolge + "," + m2.getName();
        }
        reihenfolge = reihenfolge.substring(1);
        reihenfolge = reihenfolge.toUpperCase();
    }

    public boolean contains(final Mannschaft m) {
        for (final Mannschaft ma : this.finalList) {
            if (ma.getName().endsWith(m.getName())) {
                return true;
            }
        }
        return false;
    }

    public int getRang(final Mannschaft m) throws TurnierException {
        if (!this.gespielt) {
            Penalty.LOG.warn("penalty noch nicht gespielt");
            throw new TurnierException("penalty noch nicht gespielt");
        }
        int i = 1;
        for (final Mannschaft m2 : this.getFinallist()) {
            if (m.getName().equals(m2.getName())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void setGr(final Gruppe gr) {
        this.gruppe = gr;
    }

    public void setGespielt(final boolean gespielt) {
        this.gespielt = gespielt;
    }

    // normale getter & setter
    public Gruppe getKategorie() {
        return this.gruppe;
    }

    public void setKategorie(final Gruppe kategorie) {
        this.gruppe = kategorie;
    }

    public boolean isBestaetigt() {
        return this.bestaetigt;
    }

    public void setBestaetigt(final boolean bestaetigt) {
        this.bestaetigt = bestaetigt;
    }

    public boolean isGespielt() {
        return this.gespielt;
    }

    // todo refactoring des getters zu retreaveFinalList
    public List<Mannschaft> getFinallist() {
        List<Mannschaft> result = new ArrayList<Mannschaft>();
        if (this.reihenfolge != null && !this.reihenfolge.equals("")) {
            String[] re = reihenfolge.split(",");

            for (String str : re) {
                for (Mannschaft m : this.finalList) {
                    if (str.equalsIgnoreCase(m.getName())) {
                        result.add(m);
                    }
                }
            }
            return result;
        }
        return this.finalList;
    }

    public List<Mannschaft> getRealFinalList() {
        return this.finalList;
    }

    @Override
    public String toString() {
        return toMannschaftsString();
    }

    public String toMannschaftsString() {

        if (finalList.size() == 0) {
            return "penalty ohne mannschaften " + this.idString;
        }

        StringBuilder stringBuilder = new StringBuilder();
        Mannschaft latest = null;
        for (Mannschaft m : finalList) {
            stringBuilder.append(m.getName() + ",");
            latest = m;
        }
        String ret = stringBuilder.toString();
        return ret.replace(latest.getName() + ",", latest.getName().toUpperCase());
    }

    public String getReihenfolge() {
        return reihenfolge;
    }

    public void setReihenfolge(String reihenfolge) {
        this.reihenfolge = reihenfolge;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public static String getLeer() {
        return LEER;
    }

    public String getReihenfolgeOrig() {
        return reihenfolgeOrig;
    }

    public void setReihenfolgeOrig(String reihenfolgeOrig) {
        this.reihenfolgeOrig = reihenfolgeOrig;
    }

    // getter und setter fuer xls export und import

    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }

}
