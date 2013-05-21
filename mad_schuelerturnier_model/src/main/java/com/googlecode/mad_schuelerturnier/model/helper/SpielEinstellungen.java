/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.helper;

import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class SpielEinstellungen extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1L;

    private SpielPhasenEnum phase = SpielPhasenEnum.A_ANMELDEPHASE;

    private Date starttag = new Date();

    private String starttagstr;

    private String test;

    private Date start = new Date();

    private int verschnellerungsFaktor = 1;

    private boolean startJetzt = true;

    private String spielVertauschungen;

    private int pause = 2;

    private int spiellaenge = 10;

    private int aufholzeitInSekunden = 60;

    private boolean automatischesAufholen = false;

    private boolean automatischesVorbereiten = false;

    private boolean automatischesAnsagen = false;

    private boolean abbrechenZulassen = false;

    private boolean gongEinschalten = false;


    public SpielEinstellungen() {
        DateTime date = new DateTime();
        date.withDate(2013, 6, 8);
        starttag = date.toDate();
    }

    public MannschaftTageskorrektur grabMannschaftsTageskorrekturen() {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("korrekturen", MannschaftTageskorrektur.class);
        if (spielVertauschungen == null || spielVertauschungen.isEmpty()) {
            return null;
        }
        return (MannschaftTageskorrektur) xStream.fromXML(spielVertauschungen);
    }

    public void placeMannschaftsTageskorrekturen(MannschaftTageskorrektur korr) {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("korrekturen", MannschaftTageskorrektur.class);
        spielVertauschungen = xStream.toXML(korr);
    }

    public String getStarttagstr() {
        return this.starttagstr;
    }

    public void setStarttagstr(final String starttagstr) {
        this.starttagstr = starttagstr;
    }

    public int getPause() {
        return this.pause;
    }

    public void setPause(final int pause) {
        this.pause = pause;
    }

    public int getSpiellaenge() {
        return this.spiellaenge;
    }

    public void setSpiellaenge(final int spiellaenge) {
        this.spiellaenge = spiellaenge;
    }

    public SpielPhasenEnum getPhase() {
        return this.phase;
    }

    public void setPhase(final SpielPhasenEnum phase) {
        this.phase = phase;
    }

    public Date getStarttag() {
        return this.starttag;
    }

    public void setStarttag(final Date starttag) {
        this.starttag = starttag;
    }

    public String getTest() {
        return this.test;
    }

    public void setTest(final String test) {
        this.test = test;
    }

    public Date getStart() {
        return this.start;
    }

    public boolean isStartJetzt() {
        return this.startJetzt;
    }

    public void setStartJetzt(final boolean startJetzt) {
        this.startJetzt = startJetzt;
    }

    public void setStart(final Date start) {
        this.start = start;
    }

    public int getVerschnellerungsFaktor() {
        return this.verschnellerungsFaktor;
    }

    public void setVerschnellerungsFaktor(final int verschnellerungsFaktor) {
        this.verschnellerungsFaktor = verschnellerungsFaktor;
    }

    public String getSpielVertauschungen() {
        return spielVertauschungen;
    }

    public void setSpielVertauschungen(String spielVertauschungen) {
        this.spielVertauschungen = spielVertauschungen;
    }

    public boolean isAutomatischesAufholen() {
        return automatischesAufholen;
    }

    public void setAutomatischesAufholen(boolean automatischesAufholen) {
        this.automatischesAufholen = automatischesAufholen;
    }

    public int getAufholzeitInSekunden() {
        return aufholzeitInSekunden;
    }

    public void setAufholzeitInSekunden(int aufholzeitInSekunden) {
        this.aufholzeitInSekunden = aufholzeitInSekunden;
    }

    public boolean isAutomatischesAnsagen() {
        return automatischesAnsagen;
    }

    public void setAutomatischesAnsagen(boolean automatischesAnsagen) {
        this.automatischesAnsagen = automatischesAnsagen;
    }

    public boolean isAutomatischesVorbereiten() {
        return automatischesVorbereiten;
    }

    public void setAutomatischesVorbereiten(boolean automatischesVorbereiten) {
        this.automatischesVorbereiten = automatischesVorbereiten;
    }

    public boolean isAbbrechenZulassen() {
        return abbrechenZulassen;
    }

    public void setAbbrechenZulassen(boolean abbrechenZulassen) {
        this.abbrechenZulassen = abbrechenZulassen;
    }

    public boolean isGongEinschalten() {
        return gongEinschalten;
    }

    public void setGongEinschalten(boolean gongEinschalten) {
        this.gongEinschalten = gongEinschalten;
    }
}