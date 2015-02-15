/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;


import com.googlecode.madschuelerturnier.model.enums.AnredeEnum;
import com.googlecode.madschuelerturnier.interfaces.*;

import javax.persistence.Entity;

/**
 * Ein Kontakt zum Versand von Couverts und Serienbriefen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.13
 */
@Entity
public class Kontakt extends Persistent implements CouvertReportable, RechnungReportable {

    private AnredeEnum anrede = AnredeEnum.AN;
    private String name;
    private String vorname;
    private String strasse;
    private String PLZ;
    private String ort;

    private String liste;
    private String ressor;

    private boolean rechnung = false;

    private int anzahl;
    private float preis;
    private float betrag;
    private String ESR;

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


    public boolean isRechnung() {
        return rechnung;
    }

    public void setRechnung(boolean rechnung) {
        this.rechnung = rechnung;
    }

    @Override
    public int getAnzahl() {
        return anzahl;
    }

    @Override
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    @Override
    public float getBetrag() {
        return betrag;
    }

    @Override
    public String getESR() {
        return null;
    }

    @Override
    public void setESR(String esr) {

    }

    public void setBetrag(float betrag) {
        this.betrag = betrag;
    }



    @Override
    public float getPreis() {
        return preis;
    }

    @Override
    public void setPreis(float preis) {
        this.preis = preis;
    }

    // jxls
    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }
}
