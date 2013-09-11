/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.turnierimport;

import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.business.vorbereitung.A0SpielVorbereitungsKontroller;
import com.googlecode.madschuelerturnier.business.vorbereitung.KorrekturenHelper;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private KorrekturenHelper korrekturen;

    public void turnierHerstellen(List<Spiel> spiele) {

        SpielEinstellungen einstellungenStart = business.getSpielEinstellungen();
        SpielPhasenEnum startPhase = einstellungenStart.getPhase();

        einstellungenStart.setPhase(SpielPhasenEnum.A_ANMELDEPHASE);
        business.saveEinstellungen(einstellungenStart);

        LOG.info("SpielPhasenEnum.A_ANMELDEPHASE");
        if (isToDo(startPhase)) {
            LOG.info("SpielPhasenEnum.A_ANMELDEPHASE: wird durchgefuehrt");
            phasenCheck(startPhase);
        }

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
            // todo hier spiele ueberschreiben?
            phasenCheck(startPhase);
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

    private void phasenCheck(SpielPhasenEnum startPhase) {
        if (business.getSpielEinstellungen().getPhase() != startPhase) {
            kontroller.shiftSpielPhase();
        }
    }

    private boolean isToDo(SpielPhasenEnum startPhase) {
        return business.getSpielEinstellungen().getPhase() != startPhase;
    }
}
