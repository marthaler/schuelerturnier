/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.turnierimport;

import com.googlecode.madschuelerturnier.business.DataLoaderImpl;
import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.business.vorbereitung.A0SpielVorbereitungsKontroller;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.enums.SpielEnum;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Diese Klasse dient dazu einen bereits fertig konfigurierten Matsch zu laden
 * Kann mittels -Dspring.profiles.active="dev" / spring.profiles.active dev eingeschaltet werden
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
@Profile("dev")
public class DevContentStarter {

    private static final Logger LOG = Logger.getLogger(DevContentStarter.class);

    @Autowired
    private ResultateVerarbeiter resultate;

    @Autowired
    private A0SpielVorbereitungsKontroller kontroller;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private Business business;

    @PostConstruct
    public void init() {
        generateMannschaften();
    }

    private List<Mannschaft> datenbankBefuellen() {
        List<Mannschaft> liste = new ArrayList<Mannschaft>();

        // Knaben
        liste.addAll(DataLoaderImpl.getDataLoader().loadMannschaften(false, true, 1));
        liste.addAll(DataLoaderImpl.getDataLoader().loadMannschaften(false, true, 2));
        liste.addAll(DataLoaderImpl.getDataLoader().loadMannschaften(false, true, 7));
        liste.addAll(DataLoaderImpl.getDataLoader().loadMannschaften(false, true, 8));
        liste.addAll(DataLoaderImpl.getDataLoader().loadMannschaften(false, true, 9));
        // Maedchen
        liste.addAll(DataLoaderImpl.getDataLoader().loadMannschaften(true, false, 8));
        liste.addAll(DataLoaderImpl.getDataLoader().loadMannschaften(true, false, 9));

        // einstellungen setzen
        SpielEinstellungen einstellungen = this.business.getSpielEinstellungen();
        if (einstellungen == null) {
            einstellungen = new SpielEinstellungen();
        }
        einstellungen.setPause(1);
        einstellungen.setSpiellaenge(5);
        einstellungen.setStarttag(new Date(1349809440304L));
        this.business.saveEinstellungen(einstellungen);
        return liste;
    }

    @Async
    public void generateMannschaften() {

        List<Mannschaft> liste = datenbankBefuellen();

        this.mannschaftRepo.save(liste);

        this.kontroller.shiftSpielPhase();

        this.kontroller.shiftSpielPhase();

        this.business.initZeilen(true);

        this.business.initZeilen(false);

        this.kontroller.shiftSpielPhase();
        this.kontroller.shiftSpielPhase();

        spielResultateEintragen();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }

        finalResultateEintragen();
    }

    private void finalResultateEintragen() {
        for (final Spiel spiel : spielRepo.findFinalSpielAsc()) {

            spiel.setFertigEingetragen(true);
            spiel.setFertigGespielt(true);
            spiel.setFertigBestaetigt(true);

            if (spiel.getMannschaftA() == null) {
                continue;
            }

            final String a = spiel.getMannschaftA().getName().substring(3, 4);
            final String b = spiel.getMannschaftB().getName().substring(3, 4);

            spiel.setToreABestaetigt(Integer.parseInt(a));
            spiel.setToreBBestaetigt(Integer.parseInt(b));

            spiel.setToreA(Integer.parseInt(a));
            spiel.setToreB(Integer.parseInt(b));

            final Spiel s = this.spielRepo.save(spiel);
            this.resultate.signalFertigesSpiel(s.getId());

        }
    }

    private void spielResultateEintragen() {
        final List<Spiel> spiele = this.spielRepo.findAll();

        for (final Spiel spiel : spiele) {

            spiel.setFertigEingetragen(true);
            spiel.setFertigGespielt(true);
            spiel.setFertigBestaetigt(true);

            if (spiel.getTyp() == SpielEnum.GRUPPE) {
                final String a = spiel.getMannschaftA().getName().substring(3, 4);
                final String b = spiel.getMannschaftB().getName().substring(3, 4);

                spiel.setToreA(Integer.parseInt(a));
                spiel.setToreB(Integer.parseInt(b));

                spiel.setToreABestaetigt(Integer.parseInt(a));
                spiel.setToreBBestaetigt(Integer.parseInt(b));

                final Spiel s = this.spielRepo.save(spiel);
                this.resultate.signalFertigesSpiel(s.getId());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }
}
