/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.spiel;

import com.googlecode.mad_schuelerturnier.model.Gruppe;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import org.apache.log4j.Logger;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Penalty extends AbstractPersistable<Long> {

    public static final String LEER = "bitte_reihenfolge_angeben";

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(Penalty.class);

    private String reihenfolge = LEER;

    private String idString;
    
    @OneToOne
    private Gruppe gruppe = null;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Mannschaft> finalList = new ArrayList<Mannschaft>();

    private boolean gespielt = false;
    private boolean bestaetigt = false;

    public void addMannschaft(final Mannschaft m) {
        this.finalList.add(m);
    }

    public boolean contains(final Mannschaft m) {
        for (final Mannschaft ma : this.finalList) {
            if (ma.getName().endsWith(m.getName())) {
                return true;
            }
        }
        return false;
    }

    public int getRang(final Mannschaft m) throws Exception {
        if (!this.gespielt) {
            Penalty.LOG.warn("penalty noch nicht gespielt");
            throw new Exception("penalty noch nicht gespielt");
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

    public List<Mannschaft> getFinallist() {
         List<Mannschaft> result = new ArrayList<Mannschaft>();
        if(this.reihenfolge != null && !this.reihenfolge.equals("")){
            String[] re = reihenfolge.split(",") ;

            for(String str:re){
                for(Mannschaft m :this.finalList){
                    if(str.toLowerCase().equals(m.getName().toLowerCase())){
                        result.add(m);
                    }
                }
            }
           return result;
        }
        return this.finalList;
    }

    @Override
    public String toString() {

        return toMannschaftsString() + this.idString;
    }

    public String toMannschaftsString() {

        if(finalList.size() == 0){
            return "penalty ohne mannschaften " + this.idString;
        }

        StringBuffer b = new StringBuffer();
        Mannschaft latest = null;
        for(Mannschaft m:finalList){
            b.append(m.getName().toLowerCase() + ",");
            latest = m;
        }
        String ret = b.toString();
        return ret.replace(latest.getName().toLowerCase()+",",latest.getName().toLowerCase());
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

}
