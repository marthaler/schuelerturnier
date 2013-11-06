/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;


import com.googlecode.madschuelerturnier.business.integration.jms.SchuelerturnierTransportController;
import com.googlecode.madschuelerturnier.model.enums.MessageTyp;
import com.googlecode.madschuelerturnier.model.messages.MessageWrapper;
import com.googlecode.madschuelerturnier.model.util.XstreamUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Nimmt eingehende Anfragen entgegen
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

        if(obj.getTyp() != MessageTyp.PULLREQUEST){
        LOG.info("TransportController: nachricht angekommen: " + obj);
        }

        if(obj.getTyp().equals(MessageTyp.PULLREQUEST)){
             MessageWrapper message = controller.pullMesageForNode(obj.getSource());

            if(message != null){
                return XstreamUtil.serializeToString(message);
            }
        }

        controller.messageFromServlet(obj);

        return XstreamUtil.serializeToString(controller.createAckMessage(obj.getId()));
    }

    public void setController(SchuelerturnierTransportController controller) {
        this.controller = controller;
    }
}
