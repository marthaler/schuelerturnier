/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.mvc;

import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller zum Downloaden der Mannschaftscouverts als C5
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class SiteController {

    private static final Logger LOG = Logger.getLogger(SiteController.class);

    @Autowired
    private WebsiteInfoService service;

    @RequestMapping(value = "/websiteinfo/${jahr}", method = RequestMethod.GET)
    public String getWebsiteinfo(@PathVariable String jahr, Model model) {

        if(jahr.length() < 4){
            jahr = null;
        }

        model.addAttribute("teams", service.getGeschlechtgruppen(jahr));
        return "websiteinf/anzeige.xhtml";

    }


}
