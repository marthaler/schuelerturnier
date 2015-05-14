package ch.emad.business.schuetu.integration;

import ch.emad.business.schuetu.integration.core.TransportEndpointSender;
import ch.emad.model.schuetu.model.integration.OutgoingMessage;

import java.util.Collection;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
public interface IntegrationController {

    public boolean isMaster();

    public void thisMaster();

    public int countOutboundMessages();

    public int countInboundMessages();

    public int countStoredObjects();

    public Collection<TransportEndpointSender> getAllReceivers();

    public void registerReceiver(String address);

    public String getOwnAddress();

    public void sendMessage(OutgoingMessage event);


}
