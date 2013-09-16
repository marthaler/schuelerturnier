package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.business.vorbereitung.A0SpielVorbereitungsKontroller;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielEinstellungenRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * BusinessImpl Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context-test1.xml"})
public class SpielZuweiserIntegrationTest {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SpielZuweiserIntegrationTest.class);

    @Autowired
    private A0SpielVorbereitungsKontroller kontroller;

    @Autowired
    MannschaftRepository mannschaftRepo;

    @Autowired
    SpielEinstellungenRepository spielEinstellungenRepository;

    @Autowired
    SpielZeilenRepository spielzeilenRepo;

    @Autowired
    Business business;

    private DataLoader mannschaftGenerator = DataLoaderImpl.getDataLoader();

    @Test
    @DirtiesContext
    @Rollback(true)
    public void TestSpielKontroller() {

        this.mannschaftRepo.save(this.mannschaftGenerator.loadMannschaften());
        business.initializeDB();
        SpielPhasenEnum phase = this.kontroller.readSpielPhase();
        Assert.assertNotNull(phase);

        Assert.assertEquals(SpielPhasenEnum.A_ANMELDEPHASE, phase);

        this.kontroller.shiftSpielPhase();
        phase = this.kontroller.readSpielPhase();
        Assert.assertNotNull(phase);
        Assert.assertEquals(SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG, phase);

        this.kontroller.shiftSpielPhase();
        phase = this.kontroller.readSpielPhase();
        Assert.assertNotNull(phase);
        Assert.assertEquals(SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN, phase);

        final SpielEinstellungen einstellungen = this.business.getSpielEinstellungen();
        einstellungen.setPause(2);
        einstellungen.setSpiellaenge(10);
        LOG.info("" + System.currentTimeMillis());
        einstellungen.setStarttag(new Date(1349809440304L));

        this.business.saveEinstellungen(einstellungen);

        this.business.initZeilen(true);
        this.business.initZeilen(false);

        this.kontroller.shiftSpielPhase();
        phase = this.kontroller.readSpielPhase();
        Assert.assertNotNull(phase);
        Assert.assertEquals(SpielPhasenEnum.D_SPIELE_ZUORDNUNG, phase);

        final List<SpielZeile> samstag = this.business.getSpielzeilen(false);
        for (final SpielZeile spielZeile : samstag) {
            LOG.info("" + spielZeile);
            LOG.info("" + "ZZ:" + spielZeile.getId());
        }

        LOG.info("" + "***********");
        final List<SpielZeile> sonntag = this.business.getSpielzeilen(true);
        for (final SpielZeile spielZeile : sonntag) {
            LOG.info("" + spielZeile);
            LOG.info("" + "ZZ:" + spielZeile.getId());
        }

        final List<SpielZeile> list = this.spielzeilenRepo.findNextZeile();
        Assert.assertTrue(0 < list.size());

    }
}
