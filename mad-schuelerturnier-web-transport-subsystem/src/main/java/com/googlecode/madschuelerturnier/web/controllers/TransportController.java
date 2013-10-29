/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;


import com.googlecode.madschuelerturnier.business.integration.jms.SchuelerturnierTransportController;
import com.googlecode.madschuelerturnier.business.zeit.MessageWrapper;
import com.googlecode.madschuelerturnier.model.util.XstreamUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Nimmt eingehende Nachrichten entgegen
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class TransportController {

    @Autowired
    SchuelerturnierTransportController controller;

    private static final Logger LOG = Logger.getLogger(TransportController.class);

    @RequestMapping("/transport")
    @ResponseBody
    public String handleRequest(@ModelAttribute("payload") String payload) {

        MessageWrapper obj = (MessageWrapper) XstreamUtil.deserializeFromString(payload);
        LOG.info("TransportController: nachricht angekommen: " + obj);

        controller.onMessage(obj);

        return "ok";
    }

}
