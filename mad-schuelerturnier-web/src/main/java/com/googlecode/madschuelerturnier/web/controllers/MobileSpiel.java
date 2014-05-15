package com.googlecode.madschuelerturnier.web.controllers;

/**
 * Created by dama on 15.05.14.
 */
public class MobileSpiel {

    private String start;
    private String platz;
    private String gegner;

    private boolean verloren;
    private String resultat;

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getPlatz() {
        return platz;
    }

    public void setPlatz(String platz) {
        this.platz = platz;
    }

    public String getGegner() {
        return gegner;
    }

    public void setGegner(String gegner) {
        this.gegner = gegner;
    }

    public boolean isVerloren() {
        return verloren;
    }

    public void setVerloren(boolean verloren) {
        this.verloren = verloren;
    }

}
