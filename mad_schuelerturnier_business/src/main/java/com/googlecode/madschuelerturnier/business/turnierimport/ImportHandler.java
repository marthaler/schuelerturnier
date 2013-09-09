/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.turnierimport;

import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.business.vorbereitung.A0SpielVorbereitungsKontroller;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void turnierHerstellen() {
        SpielEinstellungen einstellungenStart = business.getSpielEinstellungen();
        SpielPhasenEnum startPhase = einstellungenStart.getPhase();

        einstellungenStart.setPhase(SpielPhasenEnum.A_ANMELDEPHASE);
        business.saveEinstellungen(einstellungenStart);

        LOG.info("SpielPhasenEnum.A_ANMELDEPHASE");
        phasenCheck(startPhase);

        LOG.info("SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG");
        phasenCheck(startPhase);

        LOG.info("SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG");
        phasenCheck(startPhase);

        LOG.info("SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN");
        phasenCheck(startPhase);

        LOG.info("SpielPhasenEnum.D_SPIELE_ZUORDNUNG");
        phasenCheck(startPhase);

        LOG.info("SpielPhasenEnum.E_SPIELBEREIT");
        phasenCheck(startPhase);

        LOG.info("SpielPhasenEnum.F_SPIELEN");
        phasenCheck(startPhase);

        LOG.info("SpielPhasenEnum.G_ABGESCHLOSSEN");
        phasenCheck(startPhase);

    }

    private void phasenCheck(SpielPhasenEnum startPhase) {
        if (business.getSpielEinstellungen().getPhase() != startPhase) {
            kontroller.shiftSpielPhase();
        }
    }
}
