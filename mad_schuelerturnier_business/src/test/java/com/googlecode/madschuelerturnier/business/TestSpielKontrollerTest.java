package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.business.vorbereitung.A0SpielVorbereitungsKontroller;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielEinstellungenRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
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
@DirtiesContext
public class TestSpielKontrollerTest {

    @Autowired
    private A0SpielVorbereitungsKontroller kontroller;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private SpielEinstellungenRepository einstellungen;

    @Autowired
    private Business business;

    private DataLoader mannschaftGenerator = DataLoaderImpl.getDataLoader();

    @Test
    @Rollback(true)
    public void TestSpielKontroller() {

        for (SpielEinstellungen o : einstellungen.findAll()) {
            einstellungen.delete(o);
        }

        business.initializeDB();

        mannschaftRepo.save(mannschaftGenerator.loadMannschaften());

        SpielPhasenEnum phase = kontroller.readSpielPhase();
        Assert.assertNotNull(phase);

        Assert.assertEquals("Phase ist nicht; A_ANMELDEPHASE", SpielPhasenEnum.A_ANMELDEPHASE, phase);

        kontroller.shiftSpielPhase();

        phase = kontroller.readSpielPhase();
        Assert.assertNotNull(phase);

        Assert.assertEquals("Phase ist nicht; B_KATEGORIE_ZUORDNUNG", SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG, phase);

    }

}
