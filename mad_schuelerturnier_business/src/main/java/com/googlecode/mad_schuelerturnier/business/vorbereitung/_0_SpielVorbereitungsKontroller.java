/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.vorbereitung;

import com.googlecode.mad_schuelerturnier.business.ISpielKontroller;
import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.business.utils.MannschaftenNummerierer;
import com.googlecode.mad_schuelerturnier.business.utils.SysoutHelper;
import com.googlecode.mad_schuelerturnier.model.Kategorie;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * speichert, verwaltet und steuert die spielphasen und deren veraenderungen
 * nach dem instanzieren wird jeweils init mittels @PostConstruct aufgerufen um
 * een spielzustand in die eingestellte phase zu "schieben"
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class _0_SpielVorbereitungsKontroller implements ISpielKontroller {

    private static final Logger LOG = Logger.getLogger(_0_SpielVorbereitungsKontroller.class);

    private boolean isInitialized = false;

    @Autowired
    private Business business;

    @Autowired
    private _1_KategorienZuordner automatischeZuordnung;

    @Autowired
    private _3_MannschaftenAufteiler mannschaftenAufteilen;

    @Autowired
    private _4_GeneratePaarungenAndSpiele spielGenerator;

    @Autowired
    private _5_Spielverteiler spielVerteiler;

    @Autowired
    private _6_SpielverteilerManuelleKorrekturen korrekturen;

    @Autowired
    private MannschaftenNummerierer mannschaftsNummerierer;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private SpielZeilenRepository spielzeilenRepo;

    public SpielPhasenEnum readSpielPhase() {
        SpielEinstellungen einstellung = this.business.getSpielEinstellungen();
        return einstellung.getPhase();
    }

    public _0_SpielVorbereitungsKontroller() {

    }

    @PostConstruct
    public void init() {

        this.isInitialized = true;

        if (!this.business.isDBInitialized()) {
            LOG.info("spielvorbereitungscontroller, initialisiere nicht weil db noch nicht initialisiert wurde: [01]");
            return;
        }
        if (this.isInitialized) {
            _0_SpielVorbereitungsKontroller.LOG
                    .info("spielphasen sind bereits initialisiert, mache nichts");
            return;
        }
        for (final SpielPhasenEnum phase : SpielPhasenEnum.values()) {
            _0_SpielVorbereitungsKontroller.LOG.info("initialisierung spielphase: " + phase
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
            _0_SpielVorbereitungsKontroller.LOG
                    .info("ungueltiger spielphasenwechsel: keine aenderung -> "
                            + vorher + " zu " + phase);
            return;
        }

        if (phase == SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG) {
            _0_SpielVorbereitungsKontroller.LOG
                    .info("spielphasenwechsel in: B_KATEGORIE_ZUORDNUNG");

            // mannschaften Nummerieren und speichern
            List<Mannschaft> mannschaft = mannschaftsNummerierer
                    .mannschaftenNummerieren(business.getMannschaften());
            mannschaftRepo.save(mannschaft);

            Map<String, Kategorie> kategorien = this.automatischeZuordnung
                    .automatischeZuordnung();

            System.out.println("**********");
            SysoutHelper.printKategorieMap(kategorien);
            System.out.println("**********");

        }

        if (phase == SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN) {
            _0_SpielVorbereitungsKontroller.LOG
                    .info("spielphasenwechsel in: C_SPIELTAGE_DEFINIEREN");

            // mannschaften aufteilen auf untergruppen
            mannschaftenAufteilen.mannschaftenVerteilen();

            // spiele zuordnen
            spielGenerator.generatPaarungenAndSpiele();

            SysoutHelper.printKategorieList(business.getKategorien());
        }

        if (phase == SpielPhasenEnum.D_SPIELE_ZUORDNUNG) {

            _0_SpielVorbereitungsKontroller.LOG
                    .info("spielphasenwechsel in: D_SPIELE_ZUORDNUNG");

            // Verteilen
            this.spielVerteiler.spieleAutomatischVerteilen();

            // Manuelle Korrekturen vornehmen, falls welche vorhanden sind
            this.korrekturen.korrekturenVornehmen();

        }

        if (phase == SpielPhasenEnum.E_SPIELBEREIT) {
            _0_SpielVorbereitungsKontroller.LOG.info("spielphasenwechsel in: E_SPIELBEREIT");
        }

        if (phase == SpielPhasenEnum.F_SPIELEN) {
            _0_SpielVorbereitungsKontroller.LOG.info("spielphasenwechsel in: F_SPIELEN");
        }

        if (phase == SpielPhasenEnum.G_ABGESCHLOSSEN) {
            _0_SpielVorbereitungsKontroller.LOG.info("spielphasenwechsel in: G_ABGESCHLOSSEN");
        }

        if (this.isInitialized) {
            einstellung.setPhase(phase);
            this.business.saveEinstellungen(einstellung);
        }
    }


}