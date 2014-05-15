/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.model.enums.SpielEnum;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.madschuelerturnier.web.SpielstatusWebBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controller fuer die Mobile Spielanzeige
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component
@Scope("session")
public class MobileController {

    private static final Logger LOG = Logger.getLogger(MobileController.class);


    private String mannschaftAuswahl;

    @Autowired
    private SpielRepository srepo;

    @Autowired
    private MannschaftRepository mrepo;

    public List<String> getMannschaften() {
        List<String> res = new ArrayList<String>();
        res.add("Mannschaft wählen");
        List<Mannschaft> all = mrepo.findAll();
        Collections.sort(all, new MannschaftsNamenComperator());
        for (Mannschaft m : all) {

                res.add(m.getName().toLowerCase());

        }
        return res;
    }



    public List<MobileSpiel> getSpiele() {

        List<MobileSpiel> res = new ArrayList<MobileSpiel>();
        List<Spiel> spiele = srepo.findAll();
        for (Spiel s : spiele) {

            if (mannschaftAuswahl != null && s.getMannschaftA() != null && s.getMannschaftA().getName().toLowerCase().equals(mannschaftAuswahl.toLowerCase())) {
                MobileSpiel sp = new MobileSpiel();

                sp.setGegner(s.getMannschaftB().getName());

                SimpleDateFormat fm = new SimpleDateFormat("EEE HH:mm");

                sp.setStart(fm.format(s.getStart()));

                sp.setResultat("(" + s.getToreABestaetigt() + ":" + s.getToreBBestaetigt() + ")");

                sp.setPlatz(s.getPlatz().getText());

                sp.setVerloren(s.getToreABestaetigt() < s.getToreBBestaetigt());
if(s.getTyp() == SpielEnum.GRUPPE){
                res.add(sp);
}

            }

            if (mannschaftAuswahl != null && s.getMannschaftA() != null && s.getMannschaftB().getName().toLowerCase().equals(mannschaftAuswahl.toLowerCase())) {
                MobileSpiel sp = new MobileSpiel();

                sp.setGegner(s.getMannschaftA().getName());

                SimpleDateFormat fm = new SimpleDateFormat("EEE HH:mm");

                sp.setStart(fm.format(s.getStart()));

                sp.setResultat("(" + s.getToreABestaetigt() + ":" + s.getToreBBestaetigt() + ")");

                sp.setPlatz(s.getPlatz().getText());

                sp.setVerloren(s.getToreBBestaetigt() < s.getToreABestaetigt());

                if(s.getTyp() == SpielEnum.GRUPPE){
                    res.add(sp);
                }

            }

        }
        return res;
    }


    public String getMannschaftAuswahl() {


        Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestCookieMap();

        javax.servlet.http.Cookie msch = (Cookie) requestCookieMap.get("SchuetuMannschaft");

if(msch != null && msch.getValue() != null){
    mannschaftAuswahl = msch.getValue();
}

        return mannschaftAuswahl;
    }

    public void setMannschaftAuswahl(String mannschaftAuswahl) {
        this.mannschaftAuswahl = mannschaftAuswahl;

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .addResponseCookie("SchuetuMannschaft", mannschaftAuswahl, null);

    }
}
