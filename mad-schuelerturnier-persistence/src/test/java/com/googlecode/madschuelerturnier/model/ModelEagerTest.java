/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test zum pruefen ob die CRUD funktionen fuer die DO's funktionieren, mit eager loading
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-persistence-context.xml")
public class ModelEagerTest {

    private static final Logger LOG = Logger.getLogger(ModelEagerTest.class);

    @Autowired
    KategorieRepository kRepo;

    @Autowired
    SpielRepository sRepo;

    @Test
    public void testKategorie() throws Exception {
        Kategorie kategorie = new Kategorie();
        final Gruppe gruppe = new Gruppe();

        final Mannschaft m1 = new Mannschaft();
        m1.setAnzahlSpieler(10);
        m1.setGeschlecht(GeschlechtEnum.K);
        m1.setKlasse(2);
        m1.setTeamNummer(1);
        final Mannschaft m2 = new Mannschaft();
        m2.setAnzahlSpieler(10);
        m2.setGeschlecht(GeschlechtEnum.K);
        m2.setKlasse(2);
        m2.setTeamNummer(3);
        final Mannschaft m3 = new Mannschaft();
        m3.setAnzahlSpieler(10);
        m3.setGeschlecht(GeschlechtEnum.K);
        m3.setKlasse(2);
        m3.setTeamNummer(3);

        gruppe.addMannschaft(m1);
        gruppe.addMannschaft(m2);
        gruppe.addMannschaft(m3);

        kategorie.setGruppeA(gruppe);

        kategorie = this.kRepo.save(kategorie);

        final Mannschaft m = kategorie.getGruppeA().getMannschaften().get(0);


        final Spiel s = new Spiel();
        this.sRepo.save(s);


        final Kategorie kategorie2 = this.kRepo.save(kategorie);

        final Spiel sp = this.sRepo.findAll().iterator().next();

        final Kategorie kategorie3 = this.kRepo.findAll().iterator().next();


    }
}
