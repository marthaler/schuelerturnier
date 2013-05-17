package com.googlecode.mad_schuelerturnier.business.vorbereitung;

import com.googlecode.mad_schuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.mad_schuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielZeilenRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class SpielverteilerMitManuellerKorrekturTest {

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
    SpielZeilenRepository repo;

    @Autowired
    Business business;

    @Autowired
    _6_SpielverteilerManuelleKorrekturen korr;

    @Before
    public void before() {

        final List<Mannschaft> liste1 = this.mannschaftGenerator.loadMannschaften4Jahr("2012", null, null);
        final List<Mannschaft> liste2 = new ArrayList<Mannschaft>();

        for (Mannschaft mannschaft : liste1) {
                  if(mannschaft.getKlasse() == 6){
                      liste2.add(mannschaft);
                  }
        }

        this.mannschaftRepo.save(liste2);

        this.kontroller.shiftSpielPhase();

        this.kontroller.shiftSpielPhase();

        final SpielEinstellungen einstellungen = this.business.getSpielEinstellungen();
        einstellungen.setPause(2);
        einstellungen.setSpiellaenge(10);
        einstellungen.setStarttag(new Date(1349809440304L));

        this.business.saveEinstellungen(einstellungen);

    }

    @Test
    @Ignore
    public void generateMannschaften() {

        this.business.initZeilen(true);
        this.business.initZeilen(false);
        this.kontroller.shiftSpielPhase();

        Iterable<SpielZeile> liste = repo.findAll();

        for (SpielZeile spielZeile : liste) {

            if(! spielZeile.isEmty()){
                System.out.println(spielZeile);
            }
        }


    String vertauschungen = "sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;";


        final SpielEinstellungen einstellungen = this.business.getSpielEinstellungen();
        einstellungen.setSpielVertauschungen(vertauschungen);

        this.business.saveEinstellungen(einstellungen);


        korr.korrekturenVornehmen();

        Iterable<SpielZeile> liste2 = repo.findAll();

        for (SpielZeile spielZeile : liste2) {

            if(! spielZeile.isEmty()){
                System.out.println(spielZeile);
            }
        }

    }
}
