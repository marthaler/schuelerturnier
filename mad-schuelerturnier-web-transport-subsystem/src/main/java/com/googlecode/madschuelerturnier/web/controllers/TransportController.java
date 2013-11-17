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
        } else

        if(obj.getTyp().equals(MessageTyp.PULLREQUEST)){
            MessageWrapper message = controller.pullMessageForNode(obj.getSource());
             int i =0;
            while (message == null){
                i++;
                message = controller.pullMessageForNode(obj.getSource());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(i<100){
                    break;
                }
            }

            String toSend = XstreamUtil.serializeToString(message);

            // filter auswerten
            if(obj.getFilter() != null && !obj.getFilter().isEmpty()){
               LOG.debug("TransportController: filter im request gefunden");
               for(String filter : obj.getFilter()){
                   if(toSend.contains(filter)){
                       LOG.debug("TransportController: filter trifft zu");
                        return toSend;
                   }
               }
                LOG.debug("TransportController: filter trifft NICHT zu");
            } else{
                if(message != null){
                    return toSend;
                }
            }

        }

        controller.messageFromServlet(obj);

        return XstreamUtil.serializeToString(controller.createAckMessage(obj.getId()));
    }

    public void setController(SchuelerturnierTransportController controller) {
        this.controller = controller;
    }
}
