/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.support.WebSessionLog;
import com.googlecode.madschuelerturnier.persistence.repository.WebSessionLogRepository;
import com.googlecode.madschuelerturnier.web.SpielstatusWebBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private WebsiteInfoService service;

    @Autowired
    private SpielstatusWebBean spielstatusWebBean;

    @Autowired
    SessionHelper session;

    @RequestMapping(value = "/info/{jahr}", method = RequestMethod.GET)
    public String getWebsiteinfo(@PathVariable("jahr") String jahr, Model model, HttpServletRequest request) {

        session.invoke(request,"web");

        // fuer die anschaltung der details im jahr auch wenn im aktuellen die details ausgeblendet sind
        if (jahr.length() == 4) {
            model.addAttribute("thisyear", false);
        } else {
            model.addAttribute("thisyear", true);
            jahr = "now"; // = jetzt
        }


        SpielEinstellungen einstellungen = service.getEinstellungen(jahr);

        boolean ganzeListe = einstellungen.getWebsiteInMannschaftslistenmode();
        boolean anmeldung = einstellungen.getPhase() == SpielPhasenEnum.A_ANMELDEPHASE;

        boolean liste = ganzeListe || anmeldung;

        model.addAttribute("maedchen", service.getMaedchenMannschaften(jahr, liste));
        model.addAttribute("knaben", service.getKnabenMannschaften(jahr, liste));

        model.addAttribute("gruppenspiele", service.getGruppenspiele(jahr));
        model.addAttribute("finalspiele", service.getFinalspiele(jahr));

        model.addAttribute("rangliste", service.getRangliste(jahr));

        model.addAttribute("einstellungen", einstellungen);
        model.addAttribute("spielstatusWebBean", spielstatusWebBean);

        return "website/info";

    }


}
