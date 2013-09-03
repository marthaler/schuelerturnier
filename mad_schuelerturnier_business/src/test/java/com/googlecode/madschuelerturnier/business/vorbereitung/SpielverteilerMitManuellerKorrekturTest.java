package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.business.DataLoader;
import com.googlecode.madschuelerturnier.business.DataLoaderImpl;
import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
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

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class SpielverteilerMitManuellerKorrekturTest {

    private static final Logger LOG = Logger.getLogger(SpielverteilerMitManuellerKorrekturTest.class);

    private DataLoader mannschaftGenerator = DataLoaderImpl.getDataLoader();

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

        final List<Mannschaft> liste1 = this.mannschaftGenerator.loadMannschaften();
        final List<Mannschaft> liste2 = new ArrayList<Mannschaft>();

        for (Mannschaft mannschaft : liste1) {
            if (mannschaft.getKlasse() == 6) {
                liste2.add(mannschaft);
            }
        }

        this.mannschaftRepo.save(liste2);
        this.business.initializeDB();
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

            if (!spielZeile.isEmty()) {
                LOG.info("" + spielZeile);
            }
        }


        String vertauschungen = "sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;sa,09:00,a-sa,09:00,b;";


        final SpielEinstellungen einstellungen = this.business.getSpielEinstellungen();
        einstellungen.setSpielVertauschungen(vertauschungen);

        this.business.saveEinstellungen(einstellungen);


        korr.korrekturenVornehmen();

        Iterable<SpielZeile> liste2 = repo.findAll();

        for (SpielZeile spielZeile : liste2) {

            if (!spielZeile.isEmty()) {
                LOG.info("" + spielZeile);
            }
        }

    }
}
