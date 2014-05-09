/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;

import com.googlecode.madschuelerturnier.model.util.XstreamUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class SpielEinstellungen extends Persistent {

    private static final long serialVersionUID = 1L;

    private SpielPhasenEnum phase = SpielPhasenEnum.A_ANMELDEPHASE;

    private Date starttag = new Date();

    private String starttagstr = "";

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

    private boolean behandleFinaleProKlassebeiZusammengefuehrten = true;

    private boolean webcamdemomode = false;

    private boolean webcamdemomodescharf = false;

    private boolean websiteInMannschaftslistenmode = false;

    private boolean websiteEnableDownloadLink = false;
    private String websiteDownloadLink = "";

    private boolean websiteEnableProgrammDownloadLink = false;
    private String websiteProgrammDownloadLink = "";

    private String websiteTurnierTitel ="";


    // 05 Spielverteilung
    private int zweiPausenBisKlasse = 0;

    public SpielEinstellungen() {
        DateTime date = new DateTime();
        date.withDate(2013, 6, 8);
        starttag = date.toDate();
    }

    public void setSpielPhaseString(String phaseIn) {
        String phaseS = phaseIn.toLowerCase();

        if (phaseS.startsWith("a")) {
            this.setPhase(SpielPhasenEnum.A_ANMELDEPHASE);
        }

        if (phaseS.startsWith("b")) {
            this.setPhase(SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG);
        }

        if (phaseS.startsWith("c")) {
            this.setPhase(SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN);
        }

        if (phaseS.startsWith("d")) {
            this.setPhase(SpielPhasenEnum.D_SPIELE_ZUORDNUNG);
        }

        if (phaseS.startsWith("e")) {
            this.setPhase(SpielPhasenEnum.E_SPIELBEREIT);
        }

        if (phaseS.startsWith("f")) {
            this.setPhase(SpielPhasenEnum.F_SPIELEN);
        }

        if (phaseS.startsWith("g")) {
            this.setPhase(SpielPhasenEnum.G_ABGESCHLOSSEN);
        }

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

    @Deprecated
    public Date getStart() {
        return this.start;
    }

    public boolean isStartJetzt() {
        return this.startJetzt;
    }

    public void setStartJetzt(final boolean startJetzt) {
        this.startJetzt = startJetzt;
    }
@Deprecated
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

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean isWebcamdemomode() {
        return webcamdemomode;
    }

    public void setWebcamdemomode(boolean webcamdemomode) {
        this.webcamdemomode = webcamdemomode;
    }

    public boolean isBehandleFinaleProKlassebeiZusammengefuehrten() {
        return behandleFinaleProKlassebeiZusammengefuehrten;
    }

    public void setBehandleFinaleProKlassebeiZusammengefuehrten(boolean behandleFinaleProKlassebeiZusammengefuehrten) {
        this.behandleFinaleProKlassebeiZusammengefuehrten = behandleFinaleProKlassebeiZusammengefuehrten;
    }

    public boolean getWebsiteInMannschaftslistenmode() {
        return websiteInMannschaftslistenmode;
    }

    public void setWebsiteInMannschaftslistenmode(boolean websiteInMannschaftslistenmode) {
        this.websiteInMannschaftslistenmode = websiteInMannschaftslistenmode;
    }

    public int getZweiPausenBisKlasse() {
        return zweiPausenBisKlasse;
    }

    public void setZweiPausenBisKlasse(int zweiPausenBisKlasse) {
        this.zweiPausenBisKlasse = zweiPausenBisKlasse;
    }

    public boolean isWebsiteInMannschaftslistenmode() {
        return websiteInMannschaftslistenmode;
    }

    public boolean isWebsiteEnableDownloadLink() {
        return websiteEnableDownloadLink;
    }

    public void setWebsiteEnableDownloadLink(boolean websiteEnableDownloadLink) {
        this.websiteEnableDownloadLink = websiteEnableDownloadLink;
    }

    public String getWebsiteDownloadLink() {
        return websiteDownloadLink;
    }

    public void setWebsiteDownloadLink(String websiteDownloadLink) {
        this.websiteDownloadLink = websiteDownloadLink;
    }

    public String getWebsiteTurnierTitel() {
        return websiteTurnierTitel;
    }

    public void setWebsiteTurnierTitel(String websiteTurnierTitel) {
        this.websiteTurnierTitel = websiteTurnierTitel;
    }


    public boolean isWebsiteEnableProgrammDownloadLink() {
        return websiteEnableProgrammDownloadLink;
    }

    public void setWebsiteEnableProgrammDownloadLink(boolean websiteEnableProgrammDownloadLink) {
        this.websiteEnableProgrammDownloadLink = websiteEnableProgrammDownloadLink;
    }

    public String getWebsiteProgrammDownloadLink() {
        return websiteProgrammDownloadLink;
    }

    public void setWebsiteProgrammDownloadLink(String websiteProgrammDownloadLink) {
        this.websiteProgrammDownloadLink = websiteProgrammDownloadLink;
    }

    public boolean isWebcamdemomodescharf() {
        return webcamdemomodescharf;
    }

    public void setWebcamdemomodescharf(boolean webcamdemomodescharf) {
        this.webcamdemomodescharf = webcamdemomodescharf;
    }
}