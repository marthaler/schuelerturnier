/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.bus;

import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListener;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListenerManager;
import com.googlecode.madschuelerturnier.model.integration.OutgoingMessage;
import org.apache.log4j.Logger;
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
public class BusControllerOut implements ApplicationEventPublisherAware , ModelChangeListener {

    public BusControllerOut(){
        ModelChangeListenerManager.getInstance().addListener(this);
    }

    private static final Logger LOG = Logger.getLogger(BusControllerOut.class);

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public void onChangeModel(Serializable object) {
        LOG.info("new outgoing message: " + object.getClass().getName());

        if( !(object instanceof SpielEinstellungen) ){
            LOG.info("message: senden");
            OutgoingMessage m = new OutgoingMessage(this);
            m.setPayload(object);
            publisher.publishEvent(m);
        } else{
            LOG.info("message: mache nichts");
        }
    }
}
