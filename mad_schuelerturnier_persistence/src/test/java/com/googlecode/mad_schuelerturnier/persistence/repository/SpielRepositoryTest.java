package com.googlecode.mad_schuelerturnier.persistence.repository;

import com.googlecode.mad_schuelerturnier.model.enums.SpielEnum;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.junit.Assert;
import org.junit.Ignore;
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
public class SpielRepositoryTest {

    @Autowired
    SpielRepository sRepo;

    @Test
    @Ignore
    public void testKategorie() {

        final Spiel g = new Spiel();
        g.setTyp(SpielEnum.GRUPPE);

        final Spiel f = new Spiel();
        f.setTyp(SpielEnum.GFINAL);

        final Spiel kf = new Spiel();
        kf.setTyp(SpielEnum.KFINAL);

        this.sRepo.save(g);
        this.sRepo.save(f);
        this.sRepo.save(kf);

        final List<Spiel> gruppe = this.sRepo.findGruppenSpiel();
        final List<Spiel> finale = this.sRepo.findFinalSpiel();

        Assert.assertTrue(gruppe.size() == 1);
        Assert.assertTrue(finale.size() == 2);

        for (final Spiel spiel : finale) {
            Assert.assertTrue(spiel.getTyp() != SpielEnum.GRUPPE);
        }
    }
}
