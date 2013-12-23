/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;


import com.googlecode.madschuelerturnier.business.integration.jms.SchuelerturnierTransportControllerImpl;
import com.googlecode.madschuelerturnier.model.enums.MessageTyp;
import com.googlecode.madschuelerturnier.model.messages.MessageWrapperToSend;
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
public class TransportReceiver {

    @Autowired
    SchuelerturnierTransportControllerImpl controller;

    private static final Logger LOG = Logger.getLogger(TransportReceiver.class);

    @RequestMapping("/transport")
    @ResponseBody
    public String handleRequest(@ModelAttribute("payload") String payload) {

        MessageWrapperToSend incommingMessage = (MessageWrapperToSend) XstreamUtil.deserializeFromString(payload);

         LOG.debug("TransportController: nachricht angekommen: " + incommingMessage);
        if(incommingMessage.getTyp() != MessageTyp.PULLREQUEST){
            controller.messageFromServlet(incommingMessage);
        }

            // nachricht fuer aufrufer da sofort senden!
            MessageWrapperToSend outgoingMessage = controller.pullMessageForNode(incommingMessage.getSource());

            int i = 0;

            while(outgoingMessage != null || i < 100){
                i++;
                String response = sendMessage(incommingMessage, outgoingMessage);
                if(response == null || response.equals("<null/>")){
                    outgoingMessage = controller.pullMessageForNode(incommingMessage.getSource());
                    // aus der schleife springen wenn last angekommen ist
                    if(incommingMessage.getTyp() == MessageTyp.PAYLOAD){
                        break;
                    }
                }else{
                    return response;
                }
            }

        return XstreamUtil.serializeToString(controller.createAckMessage(incommingMessage.getId()));
    }

    private String sendMessage(MessageWrapperToSend incommingMessage, MessageWrapperToSend outgoingMessage) {
        String toSend = XstreamUtil.serializeToString(outgoingMessage);
        // filter auswerten
        if(incommingMessage.getFilter() != null && !incommingMessage.getFilter().isEmpty()){
           LOG.debug("TransportController: filter im incommingMessage gefunden");
           for(String filter : incommingMessage.getFilter()){
               if(toSend.contains(filter)){
                   LOG.debug("TransportController: filter trifft zu");
                   return toSend;
               }
           }
           LOG.debug("TransportController: filter trifft NICHT zu");
        } else{
            return toSend;
        }
        return null;
    }

    public void setController(SchuelerturnierTransportControllerImpl controller) {
        this.controller = controller;
    }
}
