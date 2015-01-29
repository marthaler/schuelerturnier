package com.googlecode.madschuelerturnier.business.integration;

import com.googlecode.madschuelerturnier.business.integration.core.TransportEndpointSender;
import com.googlecode.madschuelerturnier.model.integration.OutgoingMessage;

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
