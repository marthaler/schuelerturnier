package com.googlecode.mad_schuelerturnier.business;

import com.googlecode.mad_schuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.business.vorbereitung._0_SpielVorbereitungsKontroller;
import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielEinstellungenRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielZeilenRepository;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Business Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
@DirtiesContext
public class SpielZuweiserIntegrationTest {

    @Autowired
    _0_SpielVorbereitungsKontroller kontroller;

    @Autowired
    MannschaftRepository mannschaftRepo;

    @Autowired
    SpielEinstellungenRepository spielEinstellungenRepository;

    @Autowired
    SpielZeilenRepository spielzeilenRepo;

    @Autowired
    Business business;

    @Autowired
    CVSMannschaftParser cvsMannschaften;

    @Test
    @Ignore
    @DirtiesContext
    public void TestSpielKontroller() {

        this.mannschaftRepo.save(this.cvsMannschaften.loadMannschaften4Jahr("2011", null, null));

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
        System.out.println(System.currentTimeMillis());
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
            System.out.println(spielZeile);
            System.out.println("ZZ:" + spielZeile.getId());
        }

        System.out.println("***********");
        final List<SpielZeile> sonntag = this.business.getSpielzeilen(true);
        for (final SpielZeile spielZeile : sonntag) {
            System.out.println(spielZeile);
            System.out.println("ZZ:" + spielZeile.getId());
        }

        final List<SpielZeile> list = this.spielzeilenRepo.findNextZeile();
        Assert.assertTrue(0 < list.size());

    }
}
