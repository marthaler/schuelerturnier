/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.bus;

import ch.emad.model.common.model.DBAuthUser;
import ch.emad.model.common.model.File;
import ch.emad.model.common.model.Text;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Penalty;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielZeile;
import ch.emad.model.schuetu.model.callback.ModelChangeListener;
import ch.emad.model.schuetu.model.callback.ModelChangeListenerManager;
import ch.emad.model.schuetu.model.integration.OutgoingMessage;
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

        if (object instanceof Text) {
            LOG.debug("BusControllerOut senden: Text (SpielEinstellung)");
            sendMessage(object);
        } else if (object instanceof Mannschaft) {
            LOG.debug("BusControllerOut senden: Mannschaft");
            sendMessage(object);
        } else if (object instanceof SpielZeile) {
            LOG.debug("BusControllerOut senden: SpielZeile");
            sendMessage(object);
        } else if (object instanceof DBAuthUser) {
            LOG.debug("BusControllerOut senden: DBAuthUser");
            sendMessage(object);
        } else if (object instanceof File) {
            LOG.debug("BusControllerOut senden: File");
            sendMessage(object);
        } else if (object instanceof Penalty) {
            LOG.debug("BusControllerOut senden: Penalty");
            sendMessage(object);
        } else if (object instanceof Spiel) {
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
