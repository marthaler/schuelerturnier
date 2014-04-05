/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.web.SpielstatusWebBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller fuer die Webseiteninformationen
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component
@Scope("session")
@RequestMapping(value = "/website")
public class WebsiteInfoController {

    private static final Logger LOG = Logger.getLogger(WebsiteInfoController.class);

    @Autowired
    private WebsiteInfoService service;
    @Autowired
    private Business business;
    @Autowired
    private SpielstatusWebBean spielstatusWebBean;

    @RequestMapping(value = "/info/{jahr}", method = RequestMethod.GET)
    public String getWebsiteinfo(@PathVariable("jahr") String jahr, Model model) {

        if(jahr.length() < 4){
            jahr = null;
        }

        boolean ganzeListe = business.getSpielEinstellungen().getWebsiteInMannschaftslistenmode();
        boolean anmeldung = business.getSpielEinstellungen().getPhase() == SpielPhasenEnum.A_ANMELDEPHASE;

        boolean liste = ganzeListe || anmeldung;

        model.addAttribute("maedchen", service.getMaedchenMannschaften(jahr,liste));
        model.addAttribute("knaben", service.getKnabenMannschaften(jahr,liste));

        model.addAttribute("gruppenspiele", service.getGruppenspiele(jahr));
        model.addAttribute("finalspiele", service.getFinalspiele(jahr));

        model.addAttribute("einstellungen", business.getSpielEinstellungen());
        model.addAttribute("spielstatusWebBean", spielstatusWebBean);

        return "website/info";

    }

}
