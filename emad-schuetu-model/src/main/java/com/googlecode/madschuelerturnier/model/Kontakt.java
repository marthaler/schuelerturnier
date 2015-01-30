/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import ch.emad.schuetu.reports.interfaces.CouvertReportable;
import com.googlecode.madschuelerturnier.model.enums.AnredeEnum;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Ein Kontakt zum Versand von Couverts und Serienbriefen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.13
 */
@Entity
public class Kontakt extends Persistent implements CouvertReportable {

    private AnredeEnum anrede = AnredeEnum.AN;
    private String name;
    private String vorname;
    private String strasse;
    private String PLZ;
    private String ort;

    public String getListe() {
        return liste;
    }

    public void setListe(String liste) {
        this.liste = liste;
    }

    public String getRessor() {
        return ressor;
    }

    public void setRessor(String ressor) {
        this.ressor = ressor;
    }

    private String liste;
    private String ressor;

    @Override
    public String getAnrede() {
        return this.anrede.getText();
    }

    @Override
    public String getNameVorname() {
        return name +" " + vorname;
    }

    @Override
    public String getStrasse() {
        return this.strasse;
    }

    @Override
    public String getPLZOrt() {
        return this.PLZ + " " + ort;
    }
    public void setAnrede(String anrede) {

        this.anrede = AnredeEnum.fromString(anrede);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getPLZ() {
        return PLZ;
    }

    public void setPLZ(String PLZ) {
        this.PLZ = PLZ;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

}
