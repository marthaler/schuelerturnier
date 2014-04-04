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
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
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


    private WebsiteInfoService() {

    }


    public TeamGruppen getKnabenMannschaften(String jahr) {
        return convertToGeschlechtgruppe(mannschaftRepo.findAll(),true);
    }

    public TeamGruppen getMaedchenMannschaften(String jahr) {
        return convertToGeschlechtgruppe(mannschaftRepo.findAll(),false);
    }

    public List<TeamGruppen> getKnabenKategorien(String jahr) {
        return convertToKategorienGruppen(kategorieRepository.findAll(),true);
    }

    public List<TeamGruppen> getMaedchenKategorien(String jahr) {
        return convertToKategorienGruppen(kategorieRepository.findAll(),true);
    }

    private List<TeamGruppen> convertToKategorienGruppen(List<Kategorie> kategorien, boolean knaben){

        List<TeamGruppen> gruppen = new ArrayList<TeamGruppen>();

        //MannschaftenKonvertierenUndEinfuellen(mannschaften, knaben, result);

        for(Kategorie m : kategorien){
            // nicht gewuenschte wegfiltern
            if(!knaben && m.getMannschaften().get(0).getGeschlecht() == GeschlechtEnum.K){
                continue;
            }
            TeamGruppen gr = new TeamGruppen();
            gr.setName(m.getName());
            this.MannschaftenKonvertierenUndEinfuellen(m.getMannschaften(),gr);
            gruppen.add(gr);
        }
        return gruppen;
    }

    private TeamGruppen convertToGeschlechtgruppe(List<Mannschaft> mannschaften, boolean knaben){

            TeamGruppen result = new TeamGruppen();
        if(!knaben){
            result.setName("Die Mädchenteams");
        } else{

            result.setName("Die Knabenteams");
        }

        MannschaftenKonvertierenUndEinfuellen(mannschaften, result);

        return result;

    }

    private void MannschaftenKonvertierenUndEinfuellen(List<Mannschaft> mannschaften, TeamGruppen result) {
        for(Mannschaft m : mannschaften){
            com.googlecode.madschuelerturnier.business.websiteinfo.model.Mannschaft ma = new com.googlecode.madschuelerturnier.business.websiteinfo.model.Mannschaft();

            ma.setBegleitperson(m.getBegleitpersonName());
            ma.setCaptain(m.getCaptainName());
            ma.setKlassenname(m.getKlassenBezeichnung());
            ma.setKlasse(""+m.getKlasse());
            ma.setSpieler(m.getAnzahlSpieler());
            ma.setSchulhaus(m.getSchulhaus());

                result.addMannschaft(ma);
                result.setTotal(result.getTotal() + ma.getSpieler());
        }

        // sortieren nach klasse
        Collections.sort(result.getMannschaften(), new MannschaftsComperator());
    }

}