package com.googlecode.madschuelerturnier.business.integration.jms;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Created by dama on 16.11.13.
 */
public class TransportControllerFactory implements ApplicationEventPublisherAware {

    public static TransportControllerFactory INSTANCE;

    private ApplicationEventPublisher applicationEventPublisher;

    public  TransportControllerFactory(){
           INSTANCE = this;
    }

    public SchuelerturnierTransportControllerImpl createController(String ownAddress,String remoteAddress){
        SchuelerturnierTransportControllerImpl co = new SchuelerturnierTransportControllerImpl( ownAddress, remoteAddress);
        co.setApplicationEventPublisher(applicationEventPublisher);
        co.init();
        return co;
    }

    public static TransportControllerFactory getInstance(){
        return INSTANCE;
    }

    public void shutDown(){
        if(INSTANCE != null){
            INSTANCE.shutDown();
            INSTANCE = null;
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
