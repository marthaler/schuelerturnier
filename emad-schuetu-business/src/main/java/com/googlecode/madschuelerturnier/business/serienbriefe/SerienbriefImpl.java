/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.serienbriefe;

import ch.emad.schuetu.reports.interfaces.CouvertReportable;
import com.googlecode.madschuelerturnier.business.AsyncAtachementLoader;
import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.bus.BusControllerOut;
import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.turnierimport.ImportHandler;
import com.googlecode.madschuelerturnier.business.vorbereitung.helper.SpielzeilenValidator;
import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
import com.googlecode.madschuelerturnier.business.xls.FromXLSLoader;
import com.googlecode.madschuelerturnier.business.zeit.Zeitgeber;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Korrektur;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Penalty;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.model.Text;
import com.googlecode.madschuelerturnier.model.comperators.KategorieNameComperator;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.enums.SpielTageszeit;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.KorrekturRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.PenaltyRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielEinstellungenRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import com.googlecode.madschuelerturnier.persistence.repository.TextRepository;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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