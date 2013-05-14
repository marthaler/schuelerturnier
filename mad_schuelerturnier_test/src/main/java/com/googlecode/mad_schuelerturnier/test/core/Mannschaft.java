/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.test.core;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class Mannschaft {

    private static final long serialVersionUID = 1L;


    private boolean knaben = true;

    public boolean isKnaben() {
        return knaben;
    }

    public void setKnaben(boolean knaben) {
        this.knaben = knaben;
    }

    private int teamNummer = 0;

    private int klasse = 0;

    public int getAnzahlSpieler() {
        return anzahlSpieler;
    }

    public void setAnzahlSpieler(int anzahlSpieler) {
        this.anzahlSpieler = anzahlSpieler;
    }


    private int anzahlSpieler = 0;

    private String schulhaus = null;

    private String klassenBezeichnung;

    private String captainName = null;

    private String begleitpersonName = null;

    private String begleitpersonStrasse = null;

    private String begleitpersonPLZOrt = null;

    public String getBegleitpersonEmail() {
        return begleitpersonEmail;
    }

    public void setBegleitpersonEmail(String begleitpersonEmail) {
        this.begleitpersonEmail = begleitpersonEmail;
    }

    public String getBegleitpersonName() {
        return begleitpersonName;
    }

    public void setBegleitpersonName(String begleitpersonName) {
        this.begleitpersonName = begleitpersonName;
    }

    public String getBegleitpersonPLZOrt() {
        return begleitpersonPLZOrt;
    }

    public void setBegleitpersonPLZOrt(String begleitpersonPLZOrt) {
        this.begleitpersonPLZOrt = begleitpersonPLZOrt;
    }

    public String getBegleitpersonStrasse() {
        return begleitpersonStrasse;
    }

    public void setBegleitpersonStrasse(String begleitpersonStrasse) {
        this.begleitpersonStrasse = begleitpersonStrasse;
    }

    public String getBegleitpersonTelefon() {
        return begleitpersonTelefon;
    }

    public void setBegleitpersonTelefon(String begleitpersonTelefon) {
        this.begleitpersonTelefon = begleitpersonTelefon;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public String getKlassenBezeichnung() {
        return klassenBezeichnung;
    }

    public void setKlassenBezeichnung(String klassenBezeichnung) {
        this.klassenBezeichnung = klassenBezeichnung;
    }

    public String getSchulhaus() {
        return schulhaus;
    }

    public void setSchulhaus(String schulhaus) {
        this.schulhaus = schulhaus;
    }

    public int getTeamNummer() {
        return teamNummer;
    }

    public void setTeamNummer(int teamNummer) {
        this.teamNummer = teamNummer;
    }

    private String begleitpersonTelefon = null;

    private String begleitpersonEmail = null;


}
