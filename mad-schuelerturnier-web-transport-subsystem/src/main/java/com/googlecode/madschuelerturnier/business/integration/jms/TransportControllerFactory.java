package com.googlecode.madschuelerturnier.business.integration.jms;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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

    public SchuelerturnierTransportController createController(String ownAddress,String remoteAddress){
        SchuelerturnierTransportController co = new SchuelerturnierTransportController(ownAddress,remoteAddress);
        co.setApplicationEventPublisher(applicationEventPublisher);
        return co;
    }

    public static TransportControllerFactory getInstance(){
        return INSTANCE;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
