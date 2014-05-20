/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.bus;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.model.integration.IncommingMessage;
import com.googlecode.madschuelerturnier.model.integration.StartFile;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

import java.io.Serializable;

/**
 * Verarbeitet die Events, die vom remote host ankommen
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
    private PenaltyRepository pRepo;

    @Autowired
    private TextRepository tRepo;

    @Autowired
    private FileRepository fRepo;

    @Autowired
    private ResultateVerarbeiter resultEngine;

    @Override
    public void onApplicationEvent(IncommingMessage event) {
        Serializable obj = event.getPayload();
        LOG.info("BusControllerIn empfangen: message von anderem remote kontext angekommen: " + obj);
        if (obj instanceof Mannschaft) {
            repo.save((Mannschaft) obj);
        }
        if (obj instanceof DBAuthUser) {
            uRepo.save((DBAuthUser) obj);
        }
        if (obj instanceof SpielZeile) {
            szRepo.save((SpielZeile) obj);
        }
        if (obj instanceof Penalty) {
            pRepo.save((Penalty) obj);
        }
        if (obj instanceof File) {
            fRepo.save((File) obj);
        }
        if (obj instanceof Text) {
            tRepo.save((Text) obj);
        }
        if (obj instanceof Spiel) {
            Spiel spiel = (Spiel) obj;
            if(spiel.isFertigEingetragen()){
                resultEngine.signalFertigesSpiel(spiel.getId());
            }
            sRepo.save((Spiel) obj);
        }
    }
}
