package com.googlecode.madschuelerturnier.persistence;

import com.googlecode.madschuelerturnier.model.Gruppe;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielEinstellungenRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Simpler Test zum pruefen ob die CRUD funktionen fuer die DO's funktionieren und die weitere
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
    private SpielEinstellungenRepository sRepo;

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
        sRepo.deleteAll();
        SpielEinstellungen m = new SpielEinstellungen();
        m.setPhase(SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG);
        m = sRepo.save(m);
        assertEquals(m, sRepo.findOne(m.getId()));
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

        Assert.assertTrue(kRepo.findAll().iterator().hasNext());
        List<Kategorie> list = kRepo.getKategorienKList();

        Assert.assertTrue(list.size() > 0);

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

        Assert.assertEquals("testTyp: falsches Resultat", "eins", listen.get(0));
        Assert.assertEquals("testTyp: falsches Resultat", "zwei", listen.get(1));
        Assert.assertEquals("testTyp: falsches Resultat", "drei", listen.get(2));

        Assert.assertEquals("testTyp: falsche Menge", 3, listen.size());

    }

}