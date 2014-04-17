/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.turnierimport;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.business.vorbereitung.A0SpielVorbereitungsKontroller;
import com.googlecode.madschuelerturnier.business.vorbereitung.helper.KorrekturenHelper;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.comperators.SpielZeitComperator;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Dient dazu nach einem XLS-Import durchzufuehren und das Spiel wieder in die entsprechende Phase zu setzen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@Component
public class ImportHandler {

    private static final Logger LOG = Logger.getLogger(ImportHandler.class);
    @Autowired
    private A0SpielVorbereitungsKontroller kontroller;
    @Autowired
    private Business business;
    @Autowired
    private SpielRepository sRepo;
    @Autowired
    private KorrekturenHelper korrekturen;
    @Autowired
    private ResultateVerarbeiter verarbeiter;

    public void turnierHerstellen(List<Spiel> spiele) {

        SpielEinstellungen einstellungenStart = business.getSpielEinstellungen();
        SpielPhasenEnum startPhase = einstellungenStart.getPhase();

        einstellungenStart.setPhase(SpielPhasenEnum.A_ANMELDEPHASE);
        business.saveEinstellungen(einstellungenStart);


        LOG.info("SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG");
        if (isToDo(startPhase)) {
            LOG.info("SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG: wird durchgefuehrt");
            phasenCheck(startPhase);
        }

        LOG.info("SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN");
        if (isToDo(startPhase)) {
            business.initZeilen(false);
            business.initZeilen(true);

            korrekturen.spielzeilenkorrekturAusDbAnwenden();

            LOG.info("SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN: wird durchgefuehrt");
            phasenCheck(startPhase);
        }

        LOG.info("SpielPhasenEnum.D_SPIELE_ZUORDNUNG");
        if (isToDo(startPhase)) {
            LOG.info("SpielPhasenEnum.D_SPIELE_ZUORDNUNG: wird durchgefuehrt");
            phasenCheck(startPhase);
            // Spiele updaten
            spieleUpdaten(spiele);

        }

        LOG.info("SpielPhasenEnum.E_SPIELBEREIT");
        if (isToDo(startPhase)) {
            LOG.info("SpielPhasenEnum.E_SPIELBEREIT: wird durchgefuehrt");
            phasenCheck(startPhase);
        }

        LOG.info("SpielPhasenEnum.F_SPIELEN");
        if (isToDo(startPhase)) {
            LOG.info("SpielPhasenEnum.F_SPIELEN: wird durchgefuehrt");
            phasenCheck(startPhase);
        }

        LOG.info("SpielPhasenEnum.G_ABGESCHLOSSEN");
        if (isToDo(startPhase)) {
            LOG.info("SpielPhasenEnum.G_ABGESCHLOSSEN: wird durchgefuehrt");
            phasenCheck(startPhase);
        }

    }

    private void spieleUpdaten(List<Spiel> spiele) {
        try {
            Collections.sort(spiele, new SpielZeitComperator());
            for (Spiel s : spiele) {
                Spiel temp = sRepo.findOne(s.getId());
                Date startGeneriert = temp.getStart();
                // Objekte setzen welche sonst null wären vor dem Uebertragen
                s.setMannschaftA(temp.getMannschaftA());
                s.setMannschaftB(temp.getMannschaftB());
                // notizen uebertragen
                if (s.getNotizen() != null && s.getNotizen().getValue() != null && s.getNotizen().getValue().length() > 0) {
                    temp.getNotizen().setValue(s.getNotizen().getValue());
                    temp.getNotizen().setKey(s.getNotizen().getKey());
                }

                try {
                    BeanUtils.copyProperties(temp, s);
                } catch (IllegalAccessException e) {
                    LOG.error(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    LOG.error(e.getMessage(), e);
                }

                // generierter Start wieder setzen, wegen dem Timezone problem
                LOG.info("spielimport: " + temp.getIdString() + "-" +  s.getIdString());
                LOG.info("spielimport, start aus xls: " + temp.getStart());
                LOG.info("spielimport, generiert: " + startGeneriert);
                temp.setStart(startGeneriert);
                temp = sRepo.save(temp);
                // signalisiere fertiges Spiel an Resultate Verabeiter
                if (temp.isFertigGespielt()) {
                    verarbeiter.signalFertigesSpiel(temp.getId());
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void phasenCheck(SpielPhasenEnum startPhase) {
        if (business.getSpielEinstellungen().getPhase() != startPhase) {
            kontroller.shiftSpielPhase();
        }
    }

    private boolean isToDo(SpielPhasenEnum startPhase) {
        return business.getSpielEinstellungen().getPhase().ordinal() <= startPhase.ordinal();
    }
}
