package ch.emad.web.schuetu.controllers;

import ch.emad.model.schuetu.model.Spiel;

/**
 * Created by dama on 15.05.14.
 */
public class MobileSpiel {

    private String zeile = "";
    private String color;

    private String start;
    private String platz;
    private String gegner;

    private boolean verloren;
    private String resultat;

    private boolean amSpielen = false;

    public boolean stehtBevor = true;

    private Spiel spiel;

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

    public Spiel getSpiel() {
        return spiel;
    }

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    public boolean isAmSpielen() {
        return amSpielen;
    }

    public void setAmSpielen(boolean amSpielen) {
        this.amSpielen = amSpielen;
    }

    public boolean isStehtBevor() {
        return stehtBevor;
    }

    public void setStehtBevor(boolean stehtBevor) {
        this.stehtBevor = stehtBevor;
    }
    public String getZeile() {
        return zeile;
    }

    public void setZeile(String zeile) {
        this.zeile = zeile;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
