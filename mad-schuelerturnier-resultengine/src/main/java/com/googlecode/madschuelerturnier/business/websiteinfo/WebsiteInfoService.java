/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.websiteinfo;

import com.googlecode.madschuelerturnier.business.websiteinfo.model.MannschaftsComperator;
import com.googlecode.madschuelerturnier.business.websiteinfo.model.TeamGruppen;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.comperators.KategorieKlasseUndGeschlechtComperator;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.openpojo.business.identity.impl.DefaultBusinessValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Zugriff auf die Mannschaften, Kategorien und Resultate fuer die Darstellung auf der Webseite
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.0
 */
@Component
public final class WebsiteInfoService {

    private static final Logger LOG = Logger.getLogger(WebsiteInfoService.class);

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private KategorieRepository kategorieRepository;

    @Autowired
    private SpielRepository spielRepository;

    private WebsiteInfoService() {

    }

    public List<Spiel> getGruppenspiele(String jahr){
        return spielRepository.findGruppenSpielAsc();
    }

    public List<Spiel> getFinalspiele(String jahr){
        return spielRepository.findFinalSpielAsc();
    }

    public List<TeamGruppen> getKnabenMannschaften(String jahr, boolean ganzeliste) {

        if (ganzeliste) {
            return convertToGanzeGruppe(mannschaftRepo.findAll(), true);
        }
        return convertToKategorienGruppen(kategorieRepository.findAll(), true);

    }

    public List<TeamGruppen> getMaedchenMannschaften(String jahr, boolean ganzeliste) {
        if (ganzeliste) {
            return convertToGanzeGruppe(mannschaftRepo.findAll(), false);
        }
        return convertToKategorienGruppen(kategorieRepository.findAll(), false);
    }

    private List<TeamGruppen> convertToKategorienGruppen(List<Kategorie> kategorien, boolean knaben) {
        Collections.sort(kategorien, new KategorieKlasseUndGeschlechtComperator());
        List<Kategorie> maedchen = new ArrayList<Kategorie>();
        List<Kategorie> kanben = new ArrayList<Kategorie>();
        List<Kategorie> list = new ArrayList<Kategorie>();

        List<TeamGruppen> result = new ArrayList<TeamGruppen>();

        for (Kategorie k : kategorien) {
            if (k.getMannschaften().get(0).getGeschlecht() == GeschlechtEnum.K) {
                kanben.add(k);
            } else {
                maedchen.add(k);
            }
        }
        if (knaben) {
            list = kanben;
        } else {
            list = maedchen;
        }

        for (Kategorie m : list) {

            TeamGruppen gr = new TeamGruppen();
            gr.setName("Gruppe: " + m.getName());
            this.mannschaftenKonvertierenUndEinfuellen(m.getMannschaften(), gr);
            result.add(gr);
        }
        return result;
    }

    private List<TeamGruppen> convertToGanzeGruppe(List<Mannschaft> mannschaften, boolean knaben) {

        List<Mannschaft> maedchen = new ArrayList<Mannschaft>();
        List<Mannschaft> kanben = new ArrayList<Mannschaft>();

        for (Mannschaft m : mannschaften) {
            if (m.getGeschlecht() == GeschlechtEnum.K) {
                kanben.add(m);
            } else {
                maedchen.add(m);
            }
        }

        TeamGruppen result = new TeamGruppen();
        if (!knaben) {
            mannschaftenKonvertierenUndEinfuellen(maedchen, result);
            result.setName("Die Mädchenteams");
        } else {
            mannschaftenKonvertierenUndEinfuellen(kanben, result);
            result.setName("Die Knabenteams");
        }
        List<TeamGruppen> reslist = new ArrayList<TeamGruppen>();
        reslist.add(result);
        return reslist;
    }

    private void mannschaftenKonvertierenUndEinfuellen(List<Mannschaft> mannschaften, TeamGruppen result) {
        for (Mannschaft m : mannschaften) {
            com.googlecode.madschuelerturnier.business.websiteinfo.model.Mannschaft ma = new com.googlecode.madschuelerturnier.business.websiteinfo.model.Mannschaft();
            ma.setNummer(m.getName());
            ma.setBegleitperson(m.getBegleitpersonName());
            ma.setCaptain(m.getCaptainName());
            ma.setKlassenname(m.getKlassenBezeichnung());
            ma.setKlasse("" + m.getKlasse());
            ma.setSpieler(m.getAnzahlSpieler());
            ma.setSchulhaus(m.getSchulhaus());

            result.addMannschaft(ma);
            result.setTotal(result.getTotal() + ma.getSpieler());
        }

        // sortieren nach klasse
        Collections.sort(result.getMannschaften(), new MannschaftsComperator());
    }

}