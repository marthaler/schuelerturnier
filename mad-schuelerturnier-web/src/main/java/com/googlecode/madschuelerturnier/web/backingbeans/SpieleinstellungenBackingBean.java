/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.backingbeans;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Backing Bean fuer die Speicherung des Spiel Einstellungs Objekts
 *
 * @author marthaler.worb@gmail.com
 * @since 1.3.0
 */
@Component
public class SpieleinstellungenBackingBean {

    @Autowired
    private Business business;

    private boolean websiteInMannschaftslistenmode;

    private boolean webcamdemomode;

    public boolean getWebsiteInMannschaftslistenmode() {
        if(business != null){
            return business.getSpielEinstellungen().getWebsiteInMannschaftslistenmode();
        }
        return Boolean.FALSE;
    }

    public void isWebsiteInMannschaftslistenmode(boolean websiteInMannschaftslistenmode) {

        if(business != null){
            SpielEinstellungen einst = business.getSpielEinstellungen();
            einst.setWebsiteInMannschaftslistenmode(websiteInMannschaftslistenmode);
            business.saveEinstellungen(einst);
        }
    }


    public boolean isWebcamdemomode() {
        if(business != null){
            return business.getSpielEinstellungen().isWebcamdemomode();
        }
        return Boolean.FALSE;
    }

    public void setWebcamdemomode(boolean webcamdemomode) {
        if(business != null){
            SpielEinstellungen einst = business.getSpielEinstellungen();
            einst.setWebcamdemomode(webcamdemomode);
            business.saveEinstellungen(einst);
        }
    }

}