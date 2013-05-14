/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.testdata;

import com.googlecode.mad_schuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.mad_schuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.business.vorbereitung._0_SpielVorbereitungsKontroller;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.mad_schuelerturnier.model.enums.SpielEnum;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
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
 * Kann mittels -Dspring.profiles.active="dev" eingeschaltet werden
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
@Profile("dev")
public class ResultengineStarter {

    @Autowired
    protected CVSMannschaftParser mannschaftGenerator;

    @Autowired
    protected ResultateVerarbeiter resultate;

    @Autowired
    _0_SpielVorbereitungsKontroller kontroller;

    @Autowired
    MannschaftRepository mannschaftRepo;

    @Autowired
    SpielRepository spielRepo;

    @Autowired
    Business business;

    @PostConstruct
    public void init(){
        generateMannschaften() ;
    }

          @Async
    public void generateMannschaften() {


        List<Mannschaft> liste = new ArrayList<Mannschaft>();
        List<Mannschaft> listePrimaer = this.mannschaftGenerator.loadMannschaften4Jahr("2013", null, null);


        for (final Mannschaft mannschaft : listePrimaer) {
            // liste.add(mannschaft);
        }


        for (final Mannschaft mannschaft : listePrimaer) {


            if ((mannschaft.getKlasse() == 1) && (mannschaft.getGeschlecht() == GeschlechtEnum.M)) {
                liste.add(mannschaft);
            }

            if ((mannschaft.getKlasse() == 2) && (mannschaft.getGeschlecht() == GeschlechtEnum.M)) {
                liste.add(mannschaft);
            }

            if ((mannschaft.getKlasse() == 7) && (mannschaft.getGeschlecht() == GeschlechtEnum.M)) {
                liste.add(mannschaft);
            }
            if ((mannschaft.getKlasse() == 8) && (mannschaft.getGeschlecht() == GeschlechtEnum.M)) {
                liste.add(mannschaft);
            }
            if ((mannschaft.getKlasse() == 9) && (mannschaft.getGeschlecht() == GeschlechtEnum.M)) {
                liste.add(mannschaft);
            }
            if ((mannschaft.getKlasse() == 8) && (mannschaft.getGeschlecht() == GeschlechtEnum.K)) {
                liste.add(mannschaft);
            }
            if ((mannschaft.getKlasse() == 9) && (mannschaft.getGeschlecht() == GeschlechtEnum.K)) {
                liste.add(mannschaft);
            }
        }


        this.mannschaftRepo.save(liste);

        this.kontroller.shiftSpielPhase();

        this.kontroller.shiftSpielPhase();

        final SpielEinstellungen einstellungen = this.business.getSpielEinstellungen();
        einstellungen.setPause(1);
        einstellungen.setSpiellaenge(5);
        einstellungen.setStarttag(new Date(1349809440304L));

        this.business.saveEinstellungen(einstellungen);

        this.business.initZeilen(true);

        this.business.initZeilen(false);

        this.kontroller.shiftSpielPhase();
        this.kontroller.shiftSpielPhase();

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
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for (final Spiel spiel : spielRepo.findFinalSpielAsc()) {


            spiel.setFertigEingetragen(true);
            spiel.setFertigGespielt(true);
            spiel.setFertigBestaetigt(true);

            if(spiel.getMannschaftA() == null){
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

}
