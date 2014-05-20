/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.model.comperators.SpielZeitComperator;
import com.googlecode.madschuelerturnier.model.enums.SpielEnum;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.madschuelerturnier.util.DateUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
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
    private KategorieRepository krepo;

    @Autowired
    private MannschaftRepository mrepo;

    private MobileSpiel finale;

    public List<String> getMannschaften() {
        List<String> res = new ArrayList<String>();
        res.add("Mannschaft wählen");
        List<Mannschaft> all = mrepo.findAll();
        Collections.sort(all, new MannschaftsNamenComperator());
        for (Mannschaft m : all) {
            res.add(m.getName().toUpperCase());
        }
        return res;
    }

    public List<MobileSpiel> getSpiele() {

        List<MobileSpiel> res = new ArrayList<MobileSpiel>();
        List<Spiel> spiele = srepo.findAll();

        Collections.sort(spiele, new SpielZeitComperator());

        boolean finaleok = false;

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

        if (kfinale != null && kfinale.getMannschaftA() != null && kfinale.getMannschaftB() != null) {
            if (kfinale.getMannschaftA().getName().equals(mannschaft)) {
                Boolean verloren = null;
                if (kfinale.getToreABestaetigt() < kfinale.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }
                if (gfinale.getToreABestaetigt() > kfinale.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }

                this.finale = getMobileSpiel(gfinale, mannschaft, "(" + kfinale.getToreABestaetigt() + ":" + kfinale.getToreBBestaetigt() + ")", verloren);
                this.finale.setZeile("kleiner Finale: " + this.finale.getZeile());
                return;


            } else if (kfinale.getMannschaftB().getName().equals(mannschaft)) {
                Boolean verloren = null;
                if (kfinale.getToreABestaetigt() < kfinale.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }
                if (kfinale.getToreABestaetigt() > kfinale.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }

                this.finale = getMobileSpiel(gfinale, mannschaft, "(" + kfinale.getToreBBestaetigt() + ":" + kfinale.getToreABestaetigt() + ")", verloren);
                this.finale.setZeile("Kleiner Finale: " + this.finale.getZeile());
                return;
            } else {
                this.finale = new MobileSpiel();
                finale.setZeile("Einzug ins Finale leider nicht geschafft");
            }

        } else if (gfinale != null && gfinale.getMannschaftA() != null && gfinale.getMannschaftB() != null) {

            if (gfinale.getMannschaftA().getName().equals(mannschaft)) {
                Boolean verloren = null;
                if (gfinale.getToreABestaetigt() < gfinale.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }
                if (gfinale.getToreABestaetigt() > gfinale.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }

                this.finale = getMobileSpiel(gfinale, mannschaft, "(" + gfinale.getToreABestaetigt() + ":" + gfinale.getToreBBestaetigt() + ")", verloren);
                this.finale.setZeile("grosser Finale: " + this.finale.getZeile());
                return;
            } else if (gfinale.getMannschaftB().getName().equals(mannschaft)) {
                Boolean verloren = null;
                if (gfinale.getToreABestaetigt() < gfinale.getToreBBestaetigt()) {
                    verloren = Boolean.FALSE;
                }
                if (gfinale.getToreABestaetigt() > gfinale.getToreBBestaetigt()) {
                    verloren = Boolean.TRUE;
                }

                this.finale = getMobileSpiel(gfinale, mannschaft, "(" + gfinale.getToreBBestaetigt() + ":" + gfinale.getToreABestaetigt() + ")", verloren);
                this.finale.setZeile("grosser Finale: " + this.finale.getZeile());
                return;
            } else {
                this.finale = new MobileSpiel();
                finale.setZeile("Einzug ins Finale leider nicht geschafft");

            }
        } else {
            this.finale = new MobileSpiel();

            Date d = s.getMannschaftA().getKategorie().getLatestSpiel().getStart();
            DateTime dd = new DateTime(d);
            dd = dd.plusMinutes(15);
            finale.setZeile("Paarung bekannt ab: " + DateUtil.getShortTimeDayString(dd.toDate()));
        }
    }

    public String getMannschaftAuswahl() {

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
