package com.googlecode.madschuelerturnier.business.integration.jms;

/**
 * Created by dama on 22.12.13.
 */
public interface SchuelerturnierTransportController {

    public boolean isMaster();

    public void setMaster(boolean master);

}
