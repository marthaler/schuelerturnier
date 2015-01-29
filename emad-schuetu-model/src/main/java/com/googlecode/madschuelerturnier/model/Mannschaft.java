/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import ch.emad.schuetu.reports.interfaces.CouvertReportable;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Mannschaft extends Persistent implements CouvertReportable {

    private static final Logger LOG = Logger.getLogger(Mannschaft.class);

    private static final long serialVersionUID = 1L;

    private int teamNummer = -1;

    private int klasse = 0;

    private GeschlechtEnum geschlecht = GeschlechtEnum.M;

    private int anzahlSpieler = 0;

    private Integer spielJahr = 2000;

    private String schulhaus = null;

    private String klassenBezeichnung;

    private String captainName = null;

    private String captainStrasse = null;

    private String captainPLZOrt = null;

    private String captainTelefon = null;

    @Email
    private String captainEmail = null;


    private String begleitpersonAnrede = null;

    private String begleitpersonName = null;

    private String begleitpersonStrasse = null;

    private String begleitpersonPLZOrt = null;

    private String begleitpersonTelefon = null;

    private String color = "Blau";

    @Email
    private String begleitpersonEmail = null;

    private String spielWunschHint = null;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private String notizen = null;

    @Transient
    private boolean konflikt = false;

    @OneToOne(fetch = FetchType.EAGER)
    private Gruppe gruppeA = null;

    @OneToOne(fetch = FetchType.EAGER)
    private Gruppe gruppeB = null;

    public String getName() {

        String nu = "";
        if (this.teamNummer < 10) {
            nu = "0";
        }

        if (this.teamNummer == 0) {

            if (this.konflikt) {
                return "" + this.geschlecht + this.klasse + "XX@";
            }
            return "" + this.geschlecht + this.klasse + "XX";

        }

        if (this.konflikt) {
            return "" + this.geschlecht + this.klasse + nu + this.teamNummer + "@";
        }
        return "" + this.geschlecht + this.klasse + nu + this.teamNummer;
    }


    public boolean isMemberofGroupA() {
        if (this.gruppeA.getMannschaften().contains(this)) {
            return true;
        }
        return false;
    }

    // berechnungen
    public int getSpieleAbgeschlossen() {
        int i = 0;
        final List<Spiel> list = this.getSpiele();
        for (final Spiel spiel : list) {
            final Integer sp = spiel.getPunkteVonMannschaft(this);
            if (sp > -1) {
                i++;
            }
        }
        return i;
    }

    public int getPunkteTotal() {
        int punkte = -1;
        final List<Spiel> list = this.getSpiele();
        for (final Spiel spiel : list) {
            final int sp = spiel.getPunkteVonMannschaft(this);
            if (sp > -1) {
                if (punkte == -1) {
                    punkte = sp;
                } else {
                    punkte = punkte + sp;
                }
            }
        }
        return punkte;
    }

    public int getGesammtzahlGruppenSpiele() {
        return this.getSpiele().size();
    }

    public int getGeschosseneTore() {
        int tore = 0;
        final List<Spiel> list = this.getSpiele();
        for (final Spiel spiel : list) {
            final int sp = spiel.getToreErziehlt(this);
            tore = tore + sp;
        }
        return tore;
    }

    public int getKassierteTore() {
        int tore = 0;
        final List<Spiel> list = this.getSpiele();
        for (final Spiel spiel : list) {
            final int sp = spiel.getToreKassiert(this);
            tore = tore + sp;
        }
        return tore;
    }

    public int getTorverhaeltnis() {
        return getGeschosseneTore() - getKassierteTore();
    }


    public List<Spiel> getSpiele() {
        final Set<Spiel> spieleVonGruppe = this.getGruppe().getKategorie().getSpiele();

        final List<Spiel> spieleResult = new ArrayList<Spiel>();
        for (final Spiel spiel : spieleVonGruppe) {
            if (spiel.getMannschaftA().getName().equals(this.getName()) || spiel.getMannschaftB().getName().equals(this.getName())) {
                spieleResult.add(spiel);
            }
        }
        return spieleResult;
    }


    public Kategorie getKategorie() {
        if (this.gruppeA != null) {
            return this.gruppeA.getKategorie();
        }
        return null;
    }

    // normale getter & setter

    @Deprecated
    public Gruppe getGruppeB() {
        return this.gruppeB;
    }

    @Deprecated
    public void setGruppeB(final Gruppe gruppeB) {
        this.gruppeB = gruppeB;
    }

    public Gruppe getGruppe() {
        return this.gruppeA;
    }

    public GeschlechtEnum getGeschlecht() {
        return this.geschlecht;
    }

    public int getAnzahlSpieler() {
        return this.anzahlSpieler;
    }

    public String getSchulhaus() {
        return this.schulhaus;
    }

    public String getCaptainName() {
        return this.captainName;
    }

    public String getBegleitpersonName() {
        return this.begleitpersonName;
    }

    public void setTeamNummer(final int teamNummer) {
        this.teamNummer = teamNummer;
    }

    public void setGeschlecht(final GeschlechtEnum geschlecht) {
        this.geschlecht = geschlecht;
    }

    public void setAnzahlSpieler(final int anzahlSpieler) {
        this.anzahlSpieler = anzahlSpieler;
    }

    public void setCaptainName(final String captainName) {
        this.captainName = captainName;
    }

    public void setBegleitpersonName(final String begleitpersonName) {
        this.begleitpersonName = begleitpersonName;
    }

    public void setGruppe(final Gruppe gruppe) {
        this.gruppeA = gruppe;
    }

    public void setSchulhaus(final String schulhaus) {
        this.schulhaus = schulhaus;
    }

    public int getKlasse() {
        return this.klasse;
    }

    public void setKlasse(final int klasse) {
        this.klasse = klasse;
    }

    public String getKlassenBezeichnung() {
        return this.klassenBezeichnung;
    }

    public void setKlassenBezeichnung(final String klassenBezeichnung) {
        this.klassenBezeichnung = klassenBezeichnung;
    }

    public String getBegleitpersonStrasse() {
        return this.begleitpersonStrasse;
    }

    public void setBegleitpersonStrasse(final String begleitpersonStrasse) {
        this.begleitpersonStrasse = begleitpersonStrasse;
    }

    public String getBegleitpersonPLZOrt() {
        return this.begleitpersonPLZOrt;
    }

    public void setBegleitpersonPLZOrt(final String begleitpersonPLZOrt) {
        this.begleitpersonPLZOrt = begleitpersonPLZOrt;
    }

    public String getBegleitpersonEmail() {
        return this.begleitpersonEmail;
    }

    public void setBegleitpersonEmail(final String begleitpersonEmail) {
        this.begleitpersonEmail = begleitpersonEmail;
    }

    public String getBegleitpersonTelefon() {
        return this.begleitpersonTelefon;
    }

    public void setBegleitpersonTelefon(final String begleitpersonTelefon) {
        this.begleitpersonTelefon = begleitpersonTelefon;
    }

    public boolean isKonflikt() {
        return this.konflikt;
    }

    public void setKonflikt(final boolean konflikt) {
        this.konflikt = konflikt;
    }

    public String getCaptainStrasse() {
        return this.captainStrasse;
    }

    public void setCaptainStrasse(final String captainStrasse) {
        this.captainStrasse = captainStrasse;
    }

    public String getCaptainPLZOrt() {
        return this.captainPLZOrt;
    }

    public void setCaptainPLZOrt(final String captainPLZOrt) {
        this.captainPLZOrt = captainPLZOrt;
    }

    public String getCaptainTelefon() {
        return this.captainTelefon;
    }

    public void setCaptainTelefon(final String captainTelefon) {
        this.captainTelefon = captainTelefon;
    }

    public String getCaptainEmail() {
        return this.captainEmail;
    }

    public void setCaptainEmail(final String captainEmail) {
        this.captainEmail = captainEmail;
    }

    public Integer getSpielJahr() {
        return this.spielJahr;
    }

    public void setSpielJahr(final Integer spielJahr) {
        this.spielJahr = spielJahr;
    }

    public String getNotizen() {
        return this.notizen;
    }

    public void setNotizen(final String notizen) {
        this.notizen = notizen;
    }

    public String getBegleitpersonAnrede() {
        return begleitpersonAnrede;
    }

    public void setBegleitpersonAnrede(String begleitpersonAnrede) {
        this.begleitpersonAnrede = begleitpersonAnrede;
    }

    @Deprecated
    public String getIdString() {
        if ((super.getId() != null) && (super.getId() > 0)) {
            return "" + super.getId();
        }

        LOG.info("" + "FAKE !!! MUSS WEG ");
        return UUID.randomUUID().toString();
    }


    public String toString2() {
        return "Mannschaft{" + "anzahlSpieler=" + this.anzahlSpieler + ", teamNummer=" + this.teamNummer + ", klasse=" + this.klasse + ", geschlecht=" + this.geschlecht + ", spielJahr=" + this.spielJahr + ", schulhaus='" + this.schulhaus + '\'' + ", klassenBezeichnung='" + this.klassenBezeichnung + '\'' + ", captainName='" + this.captainName + '\'' + ", captainStrasse='" + this.captainStrasse + '\'' + ", captainPLZOrt='" + this.captainPLZOrt + '\'' + ", captainTelefon='" + this.captainTelefon
                + '\'' + ", captainEmail='" + this.captainEmail + '\'' + ", begleitpersonName='" + this.begleitpersonName + '\'' + ", begleitpersonStrasse='" + this.begleitpersonStrasse + '\'' + ", begleitpersonPLZOrt='" + this.begleitpersonPLZOrt + '\'' + ", begleitpersonTelefon='" + this.begleitpersonTelefon + '\'' + ", begleitpersonEmail='" + this.begleitpersonEmail + '\'' + ", notizen='" + this.notizen + '\'' + ", konflikt=" + this.konflikt +
                // ", gruppe=" + gruppe +
                ", creationDate=" + super.getCreationdate() +
                // ", paarungen=" + paarungen +
                '}';
    }

    @Override
    public String toString() {
        return "Mannschaft [" + getName() + "]";
    }

    public String getSpielWunschHint() {
        return spielWunschHint;
    }

    public void setSpielWunschHint(String spielWunschHint) {
        this.spielWunschHint = spielWunschHint;
    }

    // getter und setter fuer xls export und import
    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }

    public int getTeamNummer() {
        return this.teamNummer;
    }

    public void setGeschlechtString(String geschlechtIn) {
        String geschlechtS = geschlechtIn.toLowerCase();
        if (geschlechtS.equals("k")) {
            this.geschlecht = GeschlechtEnum.K;
        }
        if (geschlechtS.equals("m")) {
            this.geschlecht = GeschlechtEnum.M;
        }
    }

    public String getGeschlechtString() {
        if (this.geschlecht == GeschlechtEnum.M) {
            return "m";
        }
        if (this.geschlecht == GeschlechtEnum.K) {
            return "k";
        }
        return "";
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getAnrede() {
        return this.begleitpersonAnrede;
    }

    @Override
    public String getNameVorname() {
        return this.getBegleitpersonName();
    }

    @Override
    public String getStrasse() {
        return this.begleitpersonStrasse;
    }

    @Override
    public String getPLZOrt() {
        return this.getBegleitpersonPLZOrt();
    }
}
