package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.DataLoaderImpl;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context-test2.xml"})
public class SpielverteilerMitManuellerKorrekturTest {

    private static final Logger LOG = Logger.getLogger(SpielverteilerMitManuellerKorrekturTest.class);

    @Autowired
    private A0SpielVorbereitungsKontroller kontroller;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private SpielZeilenRepository repo;

    @Autowired
    private Business business;

    @Autowired
    private F6SpielverteilerManuelleKorrekturen korr;

    @Before
    public void before() {

        this.mannschaftRepo.save(DataLoaderImpl.getDataLoader().loadMannschaften(true, true, 6, 7));

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
    @DirtiesContext
    @Rollback(true)
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
