/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.serienbriefe;

import com.googlecode.madschuelerturnier.interfaces.CouvertReportable;
import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
import com.googlecode.madschuelerturnier.model.Spiel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */
@Controller
public class SerienbriefImpl {

    private static final Logger LOG = Logger.getLogger(SerienbriefImpl.class);

    @Autowired
    private WebsiteInfoService service;

    private Set<CouvertReportable> getAdressen(String jahr,String listName){
        if(listName.equals("betreuer")) {
            List<Spiel> gruppenspiele = service.getGruppenspiele(jahr);
            Set<CouvertReportable> mannschaften = new TreeSet<CouvertReportable>();
            for (Spiel spiel : gruppenspiele) {
                mannschaften.add(spiel.getMannschaftA());
                mannschaften.add(spiel.getMannschaftB());
            }
            return mannschaften;
        }

        if(listName.equals("schulen")){

        }

        if(listName.equals("sponsoren")){

        }

        if(listName.equals("helfer")){

        }

        return null;
    }

}