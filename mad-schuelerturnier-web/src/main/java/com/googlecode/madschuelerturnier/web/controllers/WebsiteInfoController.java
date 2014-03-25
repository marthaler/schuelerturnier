/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
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

    @RequestMapping(value = "/info/{jahr}", method = RequestMethod.GET)
    public String getWebsiteinfo(@PathVariable("jahr") String jahr, Model model) {

        if(jahr.length() < 4){
            jahr = null;
        }

        model.addAttribute("teams", service.getGeschlechtgruppen(jahr));
        return "website/info";

    }

}
