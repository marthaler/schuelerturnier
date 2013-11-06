/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.bus;

import com.googlecode.madschuelerturnier.model.messages.OutgoingMessage;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Controller;

/**
 * Produziert Events, die in angeschlossene Remote Contexte gesendet werden. Ebenfalls werden
 * ankommende Events verarbeitet.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Controller
public class BusControllerOut implements ApplicationEventPublisherAware {

    private static final Logger LOG = Logger.getLogger(BusControllerOut.class);

    private ApplicationEventPublisher publisher;

    public void saveMannschaft(Mannschaft mannschaft){
        LOG.info("bus: mannschaft zum senden");
        OutgoingMessage m = new OutgoingMessage(this);
        m.setPayload(mannschaft);
        LOG.info("bus: sende mannschaft");
        publisher.publishEvent(m);
        LOG.info("bus: mannschaft gesendet");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }


}
