package com.googlecode.mad_schuelerturnier.business.resultengine;

import com.googlecode.mad_schuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.mad_schuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.business.vorbereitung._0_SpielVorbereitungsKontroller;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.compusw.RanglisteneintragHistorie;
import com.googlecode.mad_schuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.mad_schuelerturnier.model.enums.SpielEnum;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class ResultengineIntegrationTest {

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
    Business business;

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

        // liste2 = liste;

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

        // String str = resultate.getSpieleMatrix();
        //
        final Collection<RanglisteneintragHistorie> hList = this.resultate.getAllHystorien();
        String str = "";
        for (final RanglisteneintragHistorie ranglisteneintragHistorie : hList) {
            str = this.resultate.generateRanglistenHistorie(ranglisteneintragHistorie);

            System.out.println("********");
            System.out.println(str);
            try {
                FileUtils.writeStringToFile(new File("/" + ranglisteneintragHistorie.getKategorie().getName() + "test.html"), str);
            } catch (final IOException e) {
                e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println("********");

        }

        System.out.println("********");
        System.out.println(str);
        try {
            FileUtils.writeStringToFile(new File("/test.html"), str);
        } catch (final IOException e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("********");
        Assert.assertTrue(str.length() > 10);

    }

}
