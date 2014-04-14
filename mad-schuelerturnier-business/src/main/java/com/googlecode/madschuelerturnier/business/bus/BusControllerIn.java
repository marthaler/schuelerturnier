/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.bus;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.model.integration.IncommingMessage;
import com.googlecode.madschuelerturnier.model.integration.StartFile;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

import java.io.Serializable;

/**
 * Produziert Events, die in angeschlossene Remote Contexte gesendet werden. Ebenfalls werden
 * ankommende Events verarbeitet.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Controller
public class BusControllerIn implements ApplicationListener<IncommingMessage> {

    private static final Logger LOG = Logger.getLogger(BusControllerIn.class);

    @Autowired
    private MannschaftRepository repo;

    @Autowired
    private SpielZeilenRepository szRepo;

    @Autowired
    private SpielRepository sRepo;

    @Autowired
    private DBAuthUserRepository uRepo;

    @Autowired
    private Business business;

    @Autowired
    private ResultateVerarbeiter resultEngine;

    @Override
    public void onApplicationEvent(IncommingMessage event) {
        Serializable obj = event.getPayload();
        LOG.info("BusControllerIn empfangen: message von anderem remote kontext angekommen: " + obj);
        if (obj instanceof Mannschaft) {
            repo.save((Mannschaft) obj);
        } else
        if (obj instanceof StartFile) {
            business.generateSpielFromXLS(((StartFile) obj).getContent());
        } else
        if (obj instanceof DBAuthUser) {
            uRepo.save((DBAuthUser) obj);
        } else
        if (obj instanceof SpielZeile) {
            szRepo.save((SpielZeile) obj);
        }
        else
        if (obj instanceof Spiel) {
            Spiel spiel = (Spiel) obj;
            if(spiel.isFertigEingetragen()){
                resultEngine.signalFertigesSpiel(spiel.getId());
            }
            sRepo.save((Spiel) obj);
        }

        // penalty

    }
}
