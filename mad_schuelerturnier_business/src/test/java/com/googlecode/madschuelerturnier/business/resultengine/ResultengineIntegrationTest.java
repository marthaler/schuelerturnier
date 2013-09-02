package com.googlecode.madschuelerturnier.business.resultengine;

import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.business.vorbereitung._0_SpielVorbereitungsKontroller;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.compusw.RanglisteneintragHistorie;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.madschuelerturnier.model.enums.SpielEnum;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class ResultengineIntegrationTest {

    private static final Logger LOG = Logger.getLogger(ResultengineIntegrationTest.class);

    @Autowired
    protected CVSMannschaftParser mannschaftGenerator;

    @Autowired
    protected ResultateVerarbeiter resultate;

    @Autowired
    private _0_SpielVorbereitungsKontroller kontroller;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private Business business;

    @Before
    public void before() {
        final List<Mannschaft> liste2 = new ArrayList<Mannschaft>();
        final List<Mannschaft> liste = this.mannschaftGenerator.loadMannschaften4Jahr("2011", null, null);

        for (final Mannschaft mannschaft : liste) {
            if ((mannschaft.getKlasse() == 7) && (mannschaft.getGeschlecht() == GeschlechtEnum.K)) {
                liste2.add(mannschaft);
            }

            if ((mannschaft.getKlasse() == 4) && (mannschaft.getGeschlecht() == GeschlechtEnum.M)) {
                liste2.add(mannschaft);
            }

            if ((mannschaft.getKlasse() == 3) && (mannschaft.getGeschlecht() == GeschlechtEnum.K)) {
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

        this.business.initZeilen(true);

        this.business.initZeilen(false);

        this.kontroller.shiftSpielPhase();
        this.kontroller.shiftSpielPhase();

    }

    @Test
    @Ignore
    public void generateMannschaften() {

        final List<Spiel> spiele = this.spielRepo.findAll();

        for (final Spiel spiel : spiele) {
            spiel.setFertigEingetragen(true);
            spiel.setFertigGespielt(true);
            spiel.setFertigBestaetigt(true);

            if (spiel.getTyp() == SpielEnum.GRUPPE) {
                final String a = spiel.getMannschaftA().getName().substring(3, 4);
                final String b = spiel.getMannschaftB().getName().substring(3, 4);

                spiel.setToreABestaetigt(Integer.parseInt(a));
                spiel.setToreBBestaetigt(Integer.parseInt(b));

                final Spiel s = this.spielRepo.save(spiel);
                this.resultate.signalFertigesSpiel(s.getId());

            }

        }

        final Collection<RanglisteneintragHistorie> hList = this.resultate.getAllHystorien();
        String str = "";
        for (final RanglisteneintragHistorie ranglisteneintragHistorie : hList) {
            str = this.resultate.generateRanglistenHistorie(ranglisteneintragHistorie);

            LOG.info("" + "********");
            LOG.info("" + str);

            LOG.info("" + "********");

        }

        LOG.info("" + "********");
        LOG.info("" + str);

        LOG.info("" + "********");
        Assert.assertTrue(str.length() > 10);

    }

}
