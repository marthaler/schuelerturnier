/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.web.backingbeans;

import ch.emad.business.schuetu.Business;
import ch.emad.model.schuetu.model.SpielEinstellungen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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