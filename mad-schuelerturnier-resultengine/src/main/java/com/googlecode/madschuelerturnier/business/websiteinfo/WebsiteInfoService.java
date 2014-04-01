/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.websiteinfo;

import com.googlecode.madschuelerturnier.business.websiteinfo.model.MannschaftsComperator;
import com.googlecode.madschuelerturnier.business.websiteinfo.model.TeamGruppen;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
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

    private WebsiteInfoService() {

    }


    public TeamGruppen getKnabenMannschaften(String jahr) {
        return convertToGeschlechtgruppe(mannschaftRepo.findAll(),true);
    }

    public TeamGruppen getMaedchenMannschaften(String jahr) {
        return convertToGeschlechtgruppe(mannschaftRepo.findAll(),false);
    }

    private TeamGruppen convertToGeschlechtgruppe(List<Mannschaft> mannschaften, boolean knaben){
        List<TeamGruppen> gruppen = new ArrayList<TeamGruppen>();

        TeamGruppen result = new TeamGruppen();
        if(!knaben){
            result.setName("Die Mädchenteams");
        } else{

            result.setName("Die Knabenteams");
        }

        for(Mannschaft m : mannschaften){
            com.googlecode.madschuelerturnier.business.websiteinfo.model.Mannschaft ma = new com.googlecode.madschuelerturnier.business.websiteinfo.model.Mannschaft();

            ma.setBegleitperson(m.getBegleitpersonName());
            ma.setCaptain(m.getCaptainName());
            ma.setKlassenname(m.getKlassenBezeichnung());
            ma.setKlasse(""+m.getKlasse());
            ma.setSpieler(m.getAnzahlSpieler());
            ma.setSchulhaus(m.getSchulhaus());

            if(m.getGeschlecht() == GeschlechtEnum.K && knaben){
                result.addMannschaft(ma);
                result.setTotal(result.getTotal() + ma.getSpieler());
            }

            if(m.getGeschlecht() == GeschlechtEnum.M && !knaben){
                result.addMannschaft(ma);
                result.setTotal(result.getTotal() + ma.getSpieler());
            }

        }

        // sortieren nach klasse
        Collections.sort(result.getMannschaften(), new MannschaftsComperator());
        return result;

    }

}