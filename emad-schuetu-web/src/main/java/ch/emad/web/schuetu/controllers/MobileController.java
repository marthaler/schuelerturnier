/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu.controllers;

import ch.emad.business.schuetu.mobile.MatchInfoService;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.comperators.SpielZeitComperator;
import ch.emad.model.schuetu.model.enums.SpielEnum;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import ch.emad.model.schuetu.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Controller fuer die Mobile Spielanzeige
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component
@Scope("session")
public class MobileController {

    private static final Logger LOG = Logger.getLogger(MobileController.class);

    private String mannschaftAuswahl;

    @Autowired
    private SpielRepository srepo;

    @Autowired
    private MatchInfoService matchinfo;

    @Autowired
    SessionHelper session;

    private MobileSpiel finale;

    public List<String> getMannschaften() {
        return matchinfo.getMannschaftsNamen();
    }

    public List<MobileSpiel> getSpiele() {

        if(this.getMannschaftAuswahl() == null){
            return new ArrayList<MobileSpiel>();
        }

        List<MobileSpiel> res = new ArrayList<MobileSpiel>();
        List<Spiel> spiele = srepo.findSpielFromMannschaft(matchinfo.evaluateMannschaftId(this.getMannschaftAuswahl().toUpperCase()));
        Collections.sort(spiele, new SpielZeitComperator());

        boolean finaleok = false;

        if(this.getMannschaftAuswahl() == null || this.getMannschaftAuswahl().isEmpty() || this.getMannschaftAuswahl().length() > 5){
            return null;
        }

        for (Spiel s : spiele) {


            if (this.getMannschaftAuswahl() != null && s.getMannschaftA() != null && s.getMannschaftA().getName().toUpperCase().equals(this.getMannschaftAuswahl().toUpperCase())) {

                if (!finaleok) {
                    finaleUpdaten(s, this.getMannschaftAuswahl());
                    finaleok = true;
                }


                Boolean verloren = null;
                if (s.getToreABestaetigt() < s.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }
                if (s.getToreABestaetigt() > s.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }

                MobileSpiel sp = getMobileSpiel(s, s.getMannschaftB().getName(), "(" + s.getToreABestaetigt() + ":" + s.getToreBBestaetigt() + ")", verloren);

                if (s.getTyp() == SpielEnum.GRUPPE) {
                    res.add(sp);
                }

            }

            if (mannschaftAuswahl != null && s.getMannschaftA() != null && s.getMannschaftB().getName().toUpperCase().equals(mannschaftAuswahl.toUpperCase())) {

                if (!finaleok) {
                    finaleUpdaten(s, this.getMannschaftAuswahl());
                    finaleok = true;
                }

                Boolean verloren = null;
                if (s.getToreABestaetigt() < s.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }
                if (s.getToreABestaetigt() > s.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }

                MobileSpiel sp = getMobileSpiel(s, s.getMannschaftA().getName(), "(" + s.getToreBBestaetigt() + ":" + s.getToreABestaetigt() + ")", verloren);

                if (s.getTyp() == SpielEnum.GRUPPE) {
                    res.add(sp);
                }
            }
        }
        return res;
    }

    public List<MobileSpiel> getFinale() {
        List<MobileSpiel> res = new ArrayList<MobileSpiel>();
        res.add(this.finale);
        return res;
    }

    public MobileSpiel getMobileSpiel(Spiel s, String mannschaft, String tore, Boolean verloren) {


        MobileSpiel sp = new MobileSpiel();

        sp.setZeile(sp.getZeile() + DateUtil.getShortTimeDayString(s.getStart()) + " | " + " Platz:" + s.getPlatz() + " | vs: " + mannschaft);

        // tore anfuegen wenn fertig
        if (s.isFertigBestaetigt()) {
            sp.setZeile(sp.getZeile() + " | " + tore);
        }

        // unentschieden gespielt = gelb
        if (s.isAmSpielen()) {
            sp.setColor("blue");
        }

        // unentschieden gespielt = gelb
        if (s.isFertigEingetragen() && verloren == null) {
            sp.setColor("orange");
        }

        // gewonnen = gruen
        if (s.isFertigEingetragen() && verloren == Boolean.FALSE) {
            sp.setColor("green");
        }

        // verloren = rot
        if (s.isFertigEingetragen() && verloren == Boolean.TRUE) {
            sp.setColor("red");
        }

        return sp;
    }

    private void finaleUpdaten(Spiel s, String mannschaft) {

        Spiel kfinale = s.getMannschaftA().getKategorie().getKleineFinal();
        Spiel gfinale = s.getMannschaftA().getKategorie().getGrosserFinal();

        // kleiner Finale evaluieren
        if (kfinale != null && kfinale.getMannschaftA() != null && kfinale.getMannschaftB() != null) {
            if (kfinale.getMannschaftA().getName().equals(mannschaft)) {
                Boolean verloren = null;
                if (kfinale.getToreABestaetigt() < kfinale.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }
                if (kfinale.getToreABestaetigt() > kfinale.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }

                this.finale = getMobileSpiel(kfinale, kfinale.getMannschaftBName(), "(" + kfinale.getToreABestaetigt() + ":" + kfinale.getToreBBestaetigt() + ")", verloren);
                this.finale.setZeile("Kl. Finale: " + this.finale.getZeile().replace("Platz","Pl."));
                return;
            } else if (kfinale.getMannschaftB().getName().equals(mannschaft)) {
                Boolean verloren = null;
                if (kfinale.getToreABestaetigt() < kfinale.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }
                if (kfinale.getToreABestaetigt() > kfinale.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }

                this.finale = getMobileSpiel(kfinale, kfinale.getMannschaftAName(), "(" + kfinale.getToreBBestaetigt() + ":" + kfinale.getToreABestaetigt() + ")", verloren);
                this.finale.setZeile("Kl. Finale: " + this.finale.getZeile().replace("Platz","Pl."));
                return;
            }
        }

        // grosser Finale evaluieren
        if (gfinale != null && gfinale.getMannschaftA() != null && gfinale.getMannschaftB() != null) {

            if (gfinale.getMannschaftA().getName().equals(mannschaft)) {
                Boolean verloren = null;
                if (gfinale.getToreABestaetigt() < gfinale.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }
                if (gfinale.getToreABestaetigt() > gfinale.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }

                this.finale = getMobileSpiel(gfinale, gfinale.getMannschaftBName(), "(" + gfinale.getToreABestaetigt() + ":" + gfinale.getToreBBestaetigt() + ")", verloren);
                this.finale.setZeile("Gr. Finale: " + this.finale.getZeile().replace("Platz","Pl."));
                return;
            } else if (gfinale.getMannschaftB().getName().equals(mannschaft)) {
                Boolean verloren = null;
                if (gfinale.getToreABestaetigt() < gfinale.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }
                if (gfinale.getToreABestaetigt() > gfinale.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }

                this.finale = getMobileSpiel(gfinale, gfinale.getMannschaftAName(), "(" + gfinale.getToreBBestaetigt() + ":" + gfinale.getToreABestaetigt() + ")", verloren);
                this.finale.setZeile("Gr. Finale: " + this.finale.getZeile().replace("Platz","Pl."));
                return;
            }
        }

        // finale noch nicht gesetzt !!
        if(gfinale.getMannschaftA() == null && gfinale.getMannschaftB() == null){
            this.finale = new MobileSpiel();
            finale.setZeile("Paarung bekannt ab: " + this.matchinfo.evaluateFinalSpielPaarungBekannt(s.getMannschaftA()));
        }
        else {
            this.finale = new MobileSpiel();
            finale.setZeile("Einzug ins Finale leider nicht geschafft");
        }
    }

    public String getMannschaftAuswahl() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        session.invoke(request,"mobile");

        Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestCookieMap();
        javax.servlet.http.Cookie msch = (Cookie) requestCookieMap.get("SchuetuMannschaft");

        if (msch != null && msch.getValue() != null) {
            mannschaftAuswahl = msch.getValue();

        }
        return mannschaftAuswahl;
    }

    public void setMannschaftAuswahl(String mannschaftAuswahl) {
        this.mannschaftAuswahl = mannschaftAuswahl;
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie cookie = new Cookie("SchuetuMannschaft", mannschaftAuswahl);
        cookie.setMaxAge(60 * 60 * 24 * 30 * 2);
        cookie.setVersion(cookie.getVersion() + 1);
        response.addCookie(cookie);
    }
}
