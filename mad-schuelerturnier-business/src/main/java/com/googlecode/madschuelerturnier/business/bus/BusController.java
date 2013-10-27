/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.bus;

import com.googlecode.madschuelerturnier.business.zeit.IncommingMessage;
import com.googlecode.madschuelerturnier.business.zeit.OutgoingMessage;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Produziert Events, die in angeschlossene Remote Contexte gesendet werden. Ebenfalls werden
 * ankommende Events verarbeitet.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Controller
public class BusController implements ApplicationEventPublisherAware , ApplicationListener<IncommingMessage> {

    private static final Logger LOG = Logger.getLogger(BusController.class);

    private ApplicationEventPublisher publisher;

    @Autowired
    private MannschaftRepository repo;

    public void saveMannschaft(Mannschaft mannschaft){
        LOG.info("bus: mannschaft zum senden");
        OutgoingMessage m = new OutgoingMessage(this);
        m.setPayload(m);
        LOG.info("bus: sende mannschaft");
        publisher.publishEvent(m);
        LOG.info("bus: mannschaft gesendet");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(IncommingMessage event) {
        Serializable obj = event.getPayload();
        LOG.info("bus: message von anderem remote kontext angekommen: " + obj);
        if(obj instanceof Mannschaft){
            LOG.info("bus: es ist eine mannschaft, speichern.");
            repo.save((Mannschaft) obj);
        }

    }
}
