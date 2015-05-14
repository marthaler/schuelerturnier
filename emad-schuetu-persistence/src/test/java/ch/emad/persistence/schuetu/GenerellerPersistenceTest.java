package ch.emad.persistence.schuetu;

import ch.emad.model.schuetu.model.SpielEinstellungen;

import ch.emad.persistence.schuetu.repository.KategorieRepository;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;
import ch.emad.model.schuetu.model.Gruppe;
import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.enums.GeschlechtEnum;

import static org.junit.Assert.*;

import ch.emad.persistence.schuetu.repository.SpielEinstellungenRepository2;
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
@ContextConfiguration(locations = "classpath:spring-persistence-context-test.xml")
@Transactional
public class GenerellerPersistenceTest {

    @Autowired
    private MannschaftRepository mRepo;

    @Autowired
    private SpielEinstellungenRepository2 eRepo;

    @Autowired
    private KategorieRepository kRepo;

    @Autowired
    private KorrekturPersistence korrekturen;

    @Test
    public void testMannschaft() {
        Mannschaft m = new Mannschaft();
        m.setBegleitpersonTelefon("2");
        m = mRepo.save(m);
        assertEquals(m, mRepo.findOne(m.getId()));
    }

    @Test
    public void spielEinstellungen() {
        assertFalse(eRepo.isInitialized());

        assertNull(eRepo.getEinstellungen());

        SpielEinstellungen einst = new SpielEinstellungen();

        eRepo.save(einst);
        assertEquals(einst,eRepo.getEinstellungen());

        einst.setSpiellaenge(40);
        eRepo.save(einst);

        assertEquals(40,eRepo.getEinstellungen().getSpiellaenge());

        assertTrue(eRepo.isInitialized());
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

        g.setMannschaften(ml);

        Kategorie k = new Kategorie();
        k.setGruppeA(g);

        kRepo.save(k);

        assertTrue(kRepo.findAll().iterator().hasNext());
        List<Kategorie> list = kRepo.getKategorienKList();

        assertTrue(list.size() > 0);

    }

    @Test
    public void testKorrekturen() {

        korrekturen.save("testTyp", "eins");
        korrekturen.save("testTyp", "zwei");
        korrekturen.save("testTyp", "drei");

        korrekturen.save("testTyp2", "_1");
        korrekturen.save("testTyp2", "_2");
        korrekturen.save("testTyp2", "_3");

        List<String> listen = korrekturen.getKorrekturen("testTyp");

        assertEquals("testTyp: falsches Resultat", "eins", listen.get(0));
        assertEquals("testTyp: falsches Resultat", "zwei", listen.get(1));
        assertEquals("testTyp: falsches Resultat", "drei", listen.get(2));

        assertEquals("testTyp: falsche Menge", 3, listen.size());

    }

}