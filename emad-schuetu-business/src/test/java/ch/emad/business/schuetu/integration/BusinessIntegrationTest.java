/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.integration;

import ch.emad.business.schuetu.DataLoaderImpl;
import ch.emad.business.schuetu.Business;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

/**
 * TestSpielKontrollerTest
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context-test3.xml"})
@Transactional
public class BusinessIntegrationTest {

    @Autowired
    private Business business;

    @Autowired
    private MannschaftRepository mrepo;

    @Test
    @Rollback(true)
    public void testSpielKontroller() {
        Assert.assertNull(business.getSpielEinstellungen());
        business.initializeDB();
        Assert.assertNotNull(business.getSpielEinstellungen());
    }

    @Test
    @Rollback(true)
    public void testGetPersonenListe() {
        List<Mannschaft> mannschaften = DataLoaderImpl.getDataLoader().loadMannschaften();
        mrepo.save(mannschaften);
        List<String> listeAlle = business.getPersonenListe(null);
        Assert.assertEquals(165, listeAlle.size());

        List<String> listeDa = business.getPersonenListe("da");
        Assert.assertEquals(11, listeDa.size());
    }

    @Test
    @Rollback(true)
    public void testGetSchulhausListe() {
        List<Mannschaft> mannschaften = DataLoaderImpl.getDataLoader().loadMannschaften();
        mrepo.save(mannschaften);
        List<String> listeAlle = business.getSchulhausListe("");
        Assert.assertEquals(14, listeAlle.size());

        List<String> listeZe = business.getSchulhausListe("ze");
        Assert.assertEquals(1, listeZe.size());
    }

}
