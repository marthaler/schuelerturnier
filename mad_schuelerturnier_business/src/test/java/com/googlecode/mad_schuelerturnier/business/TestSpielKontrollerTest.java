package com.googlecode.mad_schuelerturnier.business;


import com.googlecode.mad_schuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.mad_schuelerturnier.business.vorbereitung._0_SpielVorbereitungsKontroller;
import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielEinstellungenRepository;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * TestSpielKontrollerTest
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
@Ignore
public class TestSpielKontrollerTest {

    @Autowired
    _0_SpielVorbereitungsKontroller kontroller;

    @Autowired
    MannschaftRepository mannschaftRepo;

    @Autowired
    SpielEinstellungenRepository einstellungen;


    @Autowired
    SpielEinstellungenRepository spielEinstellungenRepository;

    @Autowired
    CVSMannschaftParser mannschaftenGenerieren;

    @Test
    @DirtiesContext
    public void TestSpielKontroller() {

        for (SpielEinstellungen o : einstellungen.findAll()) {
            einstellungen.delete(o);
        }


        mannschaftRepo.save(mannschaftenGenerieren.loadMannschaften4Jahr("2012", true, 4));

        SpielPhasenEnum phase = kontroller.readSpielPhase();
        Assert.assertNotNull(phase);

        Assert.assertEquals(SpielPhasenEnum.A_ANMELDEPHASE, phase);

        kontroller.shiftSpielPhase();

        phase = kontroller.readSpielPhase();
        Assert.assertNotNull(phase);

        Assert.assertEquals(SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG, phase);

    }

}