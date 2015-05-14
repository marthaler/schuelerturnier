package ch.emad.persistence.schuetu;

import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielZeile;
import ch.emad.persistence.SpringContextPersistence;
import ch.emad.persistence.schuetu.repository.SpielZeilenRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class SpielZeilenTest {

    @Autowired
    private SpielZeilenRepository repo;

    @Test
    public void findNextZeilenTest() {

        Spiel spiel2 = new Spiel();
        spiel2.setRealName("spiel2");
        spiel2.setFertigGespielt(false);

        Spiel spiel3 = new Spiel();
        spiel3.setRealName("spiel3");
        spiel3.setFertigGespielt(false);

        Spiel spiel4 = new Spiel();
        spiel4.setRealName("spiel4");
        spiel4.setFertigGespielt(false);

        SpielZeile zeile1 = new SpielZeile();
        SpielZeile zeile2 = new SpielZeile();
        zeile2.setB(spiel2);
        SpielZeile zeile3 = new SpielZeile();
        zeile3.setC(spiel3);
        SpielZeile zeile4 = new SpielZeile();
        zeile4.setA(spiel4);

        SpielZeile zeile5 = new SpielZeile();

        repo.save(zeile1);
        repo.save(zeile2);
        repo.save(zeile3);
        repo.save(zeile4);
        repo.save(zeile5);

        Pageable p = new PageRequest(0,4);
        List<SpielZeile> zeilen = repo.findNextZeilen(p);

        Assert.assertEquals(3,zeilen.size());


    }

}