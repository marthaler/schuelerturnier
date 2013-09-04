/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.business.DataLoader;
import com.googlecode.madschuelerturnier.business.DataLoaderImpl;
import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
@DirtiesContext
public class SpielverteilerTest {

    private static final Logger LOG = Logger.getLogger(SpielverteilerTest.class);

    private DataLoader mannschaftGenerator = DataLoaderImpl.getDataLoader();

    @Autowired
    protected ResultateVerarbeiter resultate;

    @Autowired
    private _0_SpielVorbereitungsKontroller kontroller;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private SpielZeilenRepository repo;

    @Autowired
    private Business business;

    @Before
    public void before() {

        final List<Mannschaft> liste2 = this.mannschaftGenerator.loadMannschaften();

        this.mannschaftRepo.save(liste2);
        business.initializeDB();
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
            LOG.info("" + spielZeile);
        }

    }

}
