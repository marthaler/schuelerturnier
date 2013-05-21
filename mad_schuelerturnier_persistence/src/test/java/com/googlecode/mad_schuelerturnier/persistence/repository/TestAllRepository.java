package com.googlecode.mad_schuelerturnier.persistence.repository;

import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.model.korrekturen.MannschftsZuordnungsKorrektur;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * Simpler Test zum pruefen ob die CRUD funktionen fuer die DO's funktionieren
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-persistence-context.xml")
@Transactional
public class TestAllRepository {

    @Autowired
    MannschaftRepository mRepo;

    @Autowired
    MannschaftsZuordnungsKorrekturRepository mzRepo;

    @Autowired
    SpielEinstellungenRepository sRepo;


    @Test
    public void testMannschaft() {

        Mannschaft m = new Mannschaft();
        m.setBegleitpersonTelefon("2");

        m = mRepo.save(m);

        assertEquals(m, mRepo.findOne(m.getId()));

    }

    @Test
    public void spielEinstellungen() {
        sRepo.deleteAll();
        SpielEinstellungen m = new SpielEinstellungen();
        m.setPhase(SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG);

        m = sRepo.save(m);

        assertEquals(m, sRepo.findOne(m.getId()));
    }

    @Test
    public void MannschaftsZuordnungsKorrekturRepository() {
        mzRepo.deleteAll();
        MannschftsZuordnungsKorrektur m = new MannschftsZuordnungsKorrektur();
        m.setZielKategorie("AA");

        m = mzRepo.save(m);

        assertEquals(m, mzRepo.findOne(m.getId()));
    }


}