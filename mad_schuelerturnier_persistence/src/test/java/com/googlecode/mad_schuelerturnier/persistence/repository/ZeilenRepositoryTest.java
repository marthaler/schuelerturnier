package com.googlecode.mad_schuelerturnier.persistence.repository;

import com.googlecode.mad_schuelerturnier.model.spiel.tabelle.SpielZeile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-persistence-context.xml")
@Transactional
@Ignored
public class ZeilenRepositoryTest {

    @Autowired
    SpielZeilenRepository zRepo;

    @Test
    public void testKategorie() {
        // kRepo.deleteAll();
        SpielZeile f = new SpielZeile();
        f.setFinale(true);
        SpielZeile g = new SpielZeile();

        zRepo.save(f);
        zRepo.save(g);

        List<SpielZeile> gruppe = zRepo.findGruppenSpielZeilen();
        List<SpielZeile> finale = zRepo.findFinalSpielZeilen();

        Assert.assertTrue(gruppe.size() == 1);
        Assert.assertTrue(finale.size() == 1);
    }
}
