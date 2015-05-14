package ch.emad.persistence.schuetu;

import ch.emad.model.schuetu.model.SpielEinstellungen;

import ch.emad.persistence.SpringContextPersistence;
import ch.emad.persistence.schuetu.repository.*;
import ch.emad.model.schuetu.model.Gruppe;
import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.enums.GeschlechtEnum;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Simpler DateUtil zum pruefen ob die CRUD funktionen fuer die DO's funktionieren und die weitere
 * Persistenz ok ist
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= SpringContextPersistence.class)
public class GenerellerPersistenceTest {

    @Autowired
    private MannschaftRepository mRepo;


    @Autowired
    private KategorieRepository kRepo;

    @Autowired
    private GruppeRepository gRepo;


    @Test
    public void testMannschaft() {
        Mannschaft m = new Mannschaft();
        m.setBegleitpersonTelefon("2");
        m = mRepo.save(m);
        assertEquals(m, mRepo.findOne(m.getId()));
    }


    @Test
    public void testKategorie() {

        Mannschaft m = new Mannschaft();
        m.setBegleitpersonTelefon("2person");
        m.setGeschlecht(GeschlechtEnum.K);
        m = mRepo.save(m);

        Gruppe g = new Gruppe();
        List<Mannschaft> ml = new ArrayList<Mannschaft>();
        ml.add(m);
        g = gRepo.save(g);
        g.setMannschaften(ml);


        Kategorie k = new Kategorie();
        k = kRepo.save(k);
        k.setGruppeA(g);

        kRepo.save(k);

        assertTrue(kRepo.findAll().iterator().hasNext());
        List<Kategorie> list = kRepo.getKategorienKList();

        assertTrue(list.size() > 0);

    }



}