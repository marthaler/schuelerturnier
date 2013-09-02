/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.utils;

import com.googlecode.madschuelerturnier.business.dataloader.CVSMannschaftParser;
import com.googlecode.madschuelerturnier.business.vorbereitung._1_KategorienZuordner;
import com.googlecode.madschuelerturnier.business.vorbereitung._3_MannschaftenAufteiler;
import com.googlecode.madschuelerturnier.business.vorbereitung._4_GeneratePaarungenAndSpiele;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
@DirtiesContext
public class TestSpielInformationExpert {

    private static final Logger LOG = Logger.getLogger(TestSpielInformationExpert.class);

    @Autowired
    private _4_GeneratePaarungenAndSpiele spiele;

    @Autowired
    private CVSMannschaftParser generator;

    @Autowired
    private _1_KategorienZuordner zuord;

    @Autowired
    private _3_MannschaftenAufteiler aufteiler;

    @Autowired
    private _1_KategorienZuordner automatischeZuordnung;

    @Autowired
    private MannschaftenNummerierer mannschaftsNummerierer;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private KategorieRepository kategorieRepo;

    @Autowired
    private SpielInformationExpert expert;

    @Test
    @Ignore
    public void testGetAnzahlSpiele() throws Exception {

        this.kategorieRepo.deleteAll();

        final List<Mannschaft> list = this.generator.loadMannschaften4Jahr("2011", null, null);

        Collections.sort(list, new MannschaftsNamenComperator());

        final Map<String, Kategorie> map = this.zuord.automatischeZuordnung();

        // mannschaften Nummerieren und speichern
        final List<Mannschaft> mannschaft = this.mannschaftsNummerierer.mannschaftenNummerieren(list);
        this.mannschaftRepo.save(mannschaft);

        final Map<String, Kategorie> kategorien = this.automatischeZuordnung.automatischeZuordnung();

        this.aufteiler.mannschaftenVerteilen();

        final Collection<Kategorie> values = kategorien.values();
        for (final Kategorie kategorie : values) {

            this.kategorieRepo.save(kategorie);
        }

        final Iterable<Kategorie> test = this.kategorieRepo.findAll();

        for (final Kategorie kategorie : test) {
            // final int size = kategorie.getGruppeA().getMannschaften().size();
            // Assert.assertTrue(size > 2);
        }

        this.spiele.generatPaarungenAndSpiele();

        final Iterable<Kategorie> te = this.kategorieRepo.findAll();

        for (final Kategorie kategorie : te) {

            LOG.info("" + kategorie.getName() + kategorie.getMannschaften().size() + " " + kategorie.getSpiele().size());
        }

        final int anzahl = this.expert.getAnzahlSpiele();
        // TODO pruefen
        // Assert.assertEquals(107, expert.getAnzahlNoetigeDurchfuehrungen(anzahl, 2));
        // Assert.assertEquals(72, expert.getAnzahlNoetigeDurchfuehrungen(anzahl, 3));
        // Assert.assertEquals(54, expert.getAnzahlNoetigeDurchfuehrungen(anzahl, 4));

        LOG.info("" + anzahl);
    }

    @Test
    @DirtiesContext
    public void testMoeglichkeiten() throws Exception {

        final DateTime start1 = new DateTime(2012, 06, 01, 9, 0);
        final DateTime ende1 = new DateTime(2012, 06, 01, 10, 1);
        final Duration d1 = new Duration(start1, ende1);

        final DateTime start2 = new DateTime(2012, 06, 01, 11, 30);
        final DateTime ende2 = new DateTime(2012, 06, 01, 12, 36);
        final Duration d2 = new Duration(start2, ende2);

        final DateTime start3 = new DateTime(2012, 06, 01, 9, 0);
        final DateTime ende3 = new DateTime(2012, 06, 01, 12, 30);
        final Duration d3 = new Duration(start3, ende3);

    }
}
