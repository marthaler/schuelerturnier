/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.bus;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListener;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListenerManager;
import com.googlecode.madschuelerturnier.model.integration.OutgoingMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Controller;

import java.io.Serializable;

/**
 * Produziert Events, die in angeschlossene Remote Contexte gesendet werden. Ebenfalls werden
 * ankommende Events verarbeitet.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Controller
public class BusControllerOut implements ApplicationEventPublisherAware, ModelChangeListener {

    private static final Logger LOG = Logger.getLogger(BusControllerOut.class);
    private ApplicationEventPublisher publisher;

    public BusControllerOut() {
        ModelChangeListenerManager.getInstance().addListener(this);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public void onChangeModel(Serializable object) {



            if(object instanceof SpielEinstellungen){
            LOG.debug("BusControllerOut senden: SpielEinstellungen");
            sendMessage(object);
        }
        else
        if(object instanceof Mannschaft){
            LOG.debug("BusControllerOut senden: Mannschaft");
            sendMessage(object);
        }
        else
        if(object instanceof SpielZeile){
            LOG.debug("BusControllerOut senden: SpielZeile");
            sendMessage(object);
        }
         else
            if(object instanceof DBAuthUser){
                LOG.debug("BusControllerOut senden: DBAuthUser");
                sendMessage(object);
                // todo bins eintragen
    }
        else
        if (object instanceof Spiel) {
            LOG.debug("BusControllerOut senden: Spiel");
            sendMessage(object);
        } else {
            LOG.debug("BusControllerOut senden: mache nichts, geaendert: " + object.getClass());
        }
    }

    private void sendMessage(Serializable object) {
        OutgoingMessage m = new OutgoingMessage(this);
        m.setPayload(object);
        publisher.publishEvent(m);
    }
}
