package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.Gruppe;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Simpler Test zum pruefen ob die CRUD funktionen fuer die DO's funktionieren
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-persistence-context.xml")
@Transactional
public class KategorieRepositoryTest {

    @Autowired
    KategorieRepository kRepo;

    @Autowired
    MannschaftRepository mRepo;

    @Test
    @Ignore
    public void testKategorie() {
        //kRepo.deleteAll();
        Mannschaft m = new Mannschaft();
        m.setBegleitpersonTelefon("2person");
        m.setGeschlecht(GeschlechtEnum.K);
        m = mRepo.save(m);

        Gruppe g = new Gruppe();
        List<Mannschaft> ml = new ArrayList<Mannschaft>();
        ml.add(m);

        g.setMannschaften(ml);

        Kategorie k = new Kategorie();
        k.setGruppeA(g);

        kRepo.save(k);


        Assert.assertTrue(kRepo.findAll().iterator().hasNext());
        List<Kategorie> list = kRepo.getKategorienKList();

        Assert.assertTrue(list.size() > 0);

    }

}