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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
@DirtiesContext
@Ignore
public class SpielverteilerTest {

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

    @Before
    public void before() {

        final List<Mannschaft> liste2 = this.mannschaftGenerator.loadMannschaften4Jahr("2012", null, null);

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
    public void generateMannschaften() {

        this.business.initZeilen(true);

        this.business.initZeilen(false);
        this.kontroller.shiftSpielPhase();
        Iterable<SpielZeile> liste = repo.findAll();

        for (SpielZeile spielZeile : liste) {
            System.out.println(spielZeile);
        }


        // ;
        // this.kontroller.shiftSpielPhase();

    }

}
