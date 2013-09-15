/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung;


import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.utils.MannschaftenNummerierer;
import com.googlecode.madschuelerturnier.business.utils.SysoutHelper;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * speichert, verwaltet und steuert die spielphasen und deren veraenderungen
 * nach dem instanzieren wird jeweils init mittels @PostConstruct aufgerufen um
 * einen spielzustand in die eingestellte phase zu "schieben"
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class A0SpielVorbereitungsKontroller {

    private static final Logger LOG = Logger.getLogger(A0SpielVorbereitungsKontroller.class);

    private boolean isInitialized = false;

    @Autowired
    private Business business;

    @Autowired
    private B1KategorienZuordner automatischeZuordnung;

    @Autowired
    private C3MannschaftenAufteiler mannschaftenAufteilen;

    @Autowired
    private D4GeneratePaarungenAndSpiele spielGenerator;

    @Autowired
    private E5Spielverteiler spielVerteiler;

    @Autowired
    private F6SpielverteilerManuelleKorrekturen korrekturen;

    @Autowired
    private MannschaftenNummerierer mannschaftsNummerierer;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private KategorieRepository kategorieRepo;

    public SpielPhasenEnum readSpielPhase() {
        SpielEinstellungen einstellung = this.business.getSpielEinstellungen();
        return einstellung.getPhase();
    }

    public A0SpielVorbereitungsKontroller() {

    }

    @PostConstruct
    public void init() {

        this.isInitialized = true;

        if (!this.business.isDBInitialized()) {
            LOG.info("spielvorbereitungscontroller, initialisiere nicht weil db noch nicht initialisiert wurde: [01]");
            return;
        }
        if (this.isInitialized) {
            A0SpielVorbereitungsKontroller.LOG
                    .info("spielphasen sind bereits initialisiert, mache nichts");
            return;
        }
        for (final SpielPhasenEnum phase : SpielPhasenEnum.values()) {
            A0SpielVorbereitungsKontroller.LOG.info("initialisierung spielphase: " + phase
                    + " " + phase.ordinal());
            if (phase.ordinal() <= readSpielPhase().ordinal()) {
                changeSpielPhase(phase);
            }
        }

    }

    /**
     * aendert die spielphase mittels int, welcher zum enum gehoert + 1
     */
    public SpielPhasenEnum shiftSpielPhase() {

        final int i = readSpielPhase().ordinal() + 1;

        for (final SpielPhasenEnum phase : SpielPhasenEnum.values()) {
            if (phase.ordinal() == i) {
                changeSpielPhase(phase);
            }
        }
        return readSpielPhase();
    }

    private void changeSpielPhase(final SpielPhasenEnum phase) {
        SpielEinstellungen einstellung = business.getSpielEinstellungen();
        if (einstellung == null) {
            einstellung = new SpielEinstellungen();
        }

        final SpielPhasenEnum vorher = readSpielPhase();

        if (vorher.ordinal() >= phase.ordinal()) {
            A0SpielVorbereitungsKontroller.LOG
                    .info("ungueltiger spielphasenwechsel: keine aenderung -> "
                            + vorher + " zu " + phase);
            return;
        }

        if (phase == SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG) {
            kategorieZuordnung();
        }

        if (phase == SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN) {
            spieltageDefinieren();
        }

        if (phase == SpielPhasenEnum.D_SPIELE_ZUORDNUNG) {

            spieleZuordnen();


        }

        if (phase == SpielPhasenEnum.E_SPIELBEREIT) {
            A0SpielVorbereitungsKontroller.LOG.info("spielphasenwechsel in: E_SPIELBEREIT");
        }

        if (phase == SpielPhasenEnum.F_SPIELEN) {
            A0SpielVorbereitungsKontroller.LOG.info("spielphasenwechsel in: F_SPIELEN");
        }

        if (phase == SpielPhasenEnum.G_ABGESCHLOSSEN) {
            A0SpielVorbereitungsKontroller.LOG.info("spielphasenwechsel in: G_ABGESCHLOSSEN");
        }

        if (this.isInitialized) {
            einstellung.setPhase(phase);
            this.business.saveEinstellungen(einstellung);
        }
    }

    private void spieleZuordnen() {
        A0SpielVorbereitungsKontroller.LOG
                .info("spielphasenwechsel in: D_SPIELE_ZUORDNUNG");

        // Verteilen
        this.spielVerteiler.spieleAutomatischVerteilen();

        // Manuelle Korrekturen vornehmen, falls welche vorhanden sind
        this.korrekturen.korrekturenVornehmen();

        LOG.info("" + "**********");
        SysoutHelper.printKategorieMap(kategorieRepo.findAll());
        LOG.info("" + "**********");
    }

    private void spieltageDefinieren() {
        A0SpielVorbereitungsKontroller.LOG
                .info("spielphasenwechsel in: C_SPIELTAGE_DEFINIEREN");

        // mannschaften aufteilen auf untergruppen
        mannschaftenAufteilen.mannschaftenVerteilen();

        // spiele zuordnen
        spielGenerator.generatPaarungenAndSpiele();


        SysoutHelper.printKategorieList(business.getKategorien());
    }

    private void kategorieZuordnung() {
        A0SpielVorbereitungsKontroller.LOG
                .info("spielphasenwechsel in: B_KATEGORIE_ZUORDNUNG");

        // mannschaften Nummerieren und speichern
        List<Mannschaft> mannschaft = mannschaftsNummerierer
                .mannschaftenNummerieren(business.getMannschaften());
        mannschaftRepo.save(mannschaft);

        Map<String, Kategorie> kategorien = this.automatischeZuordnung
                .automatischeZuordnung();
        Map<String, Kategorie> kategorienNeu = new HashMap<String, Kategorie>();

        // nachladen aus der db, sonst werden Spiele nicht angezeigt
        for (String key : kategorien.keySet()) {
            Long id = kategorien.get(key).getId();
            kategorienNeu.put(key, kategorieRepo.findOne(id));
        }
    }


}