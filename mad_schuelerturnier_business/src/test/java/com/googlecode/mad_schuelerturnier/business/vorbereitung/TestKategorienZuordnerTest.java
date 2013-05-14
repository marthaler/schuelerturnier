package com.googlecode.mad_schuelerturnier.business.vorbereitung;

import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.business.utils.MannschaftenNummerierer;
import com.googlecode.mad_schuelerturnier.exceptions.SpielPhasenException;
import com.googlecode.mad_schuelerturnier.model.Kategorie;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.mad_schuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class TestKategorienZuordnerTest {

    @Autowired
    Business business;

    @Autowired
    MannschaftRepository mannschaftRepo;


    @Autowired
    _3_MannschaftenAufteiler aufteiler;

    @Autowired
    _4_GeneratePaarungenAndSpiele spiele;

    @Autowired
    _5_Spielverteiler spieleVerteiler;

    @After
    public void cleanup() {

    }


    @SuppressWarnings("unchecked")
    @Test
    @Ignore
    public void mannschaftenZuordnen2012Test() throws SpielPhasenException {


        List<Mannschaft> result2 = business.getMannschaften();
        //Assert.assertTrue(result2.size() > 0);

        generateMannschaften(5, GeschlechtEnum.K, 1);
        generateMannschaften(6, GeschlechtEnum.K, 2);
        generateMannschaften(7, GeschlechtEnum.K, 3);
        generateMannschaften(5, GeschlechtEnum.K, 4);
        generateMannschaften(9, GeschlechtEnum.K, 5);
        generateMannschaften(8, GeschlechtEnum.K, 6);
        generateMannschaften(4, GeschlechtEnum.K, 7);
        generateMannschaften(4, GeschlechtEnum.K, 8);
        generateMannschaften(5, GeschlechtEnum.K, 9);

        generateMannschaften(2, GeschlechtEnum.M, 1);
        generateMannschaften(3, GeschlechtEnum.M, 2);
        generateMannschaften(5, GeschlechtEnum.M, 3);
        generateMannschaften(5, GeschlechtEnum.M, 4);
        generateMannschaften(4, GeschlechtEnum.M, 5);
        generateMannschaften(6, GeschlechtEnum.M, 6);
        generateMannschaften(4, GeschlechtEnum.M, 7);
        generateMannschaften(4, GeschlechtEnum.M, 8);
        generateMannschaften(2, GeschlechtEnum.M, 9);


        List<Mannschaft> result = business.getMannschaften();

        MannschaftenNummerierer num = new MannschaftenNummerierer();
        num.mannschaftenNummerieren(result);
        //Collections.shuffle(result);
        Collections.sort((List) result, new MannschaftsNamenComperator());

//		Map<String, Kategorie> map = zuordner.getKategorien();
//		
//		SysoutHelper.printKategorieMap(map);
//		
//		Assert.assertNull(map.get("M1"));
//		Assert.assertNull(map.get("M9"));
//		
//		Assert.assertNotNull(map.get("M5"));
//		Assert.assertNotNull(map.get("M5").getGruppeA());
//		Assert.assertNotNull(map.get("M5").getGruppeA().getMannschaften());
//		
//		Assert.assertTrue(map.get("M5").getGruppeA().getMannschaften().size() == 4);
//		
//		// Korrektur testen
//		manuelleZuordnungskorrektur.manuelleZuordnungHinzufuegen("K701", "K8");
//		manuelleZuordnungskorrektur.manuelleZuordnungHinzufuegen("K702", "K8");
//		manuelleZuordnungskorrektur.manuelleZuordnungHinzufuegen("K703", "K8");
//		manuelleZuordnungskorrektur.manuelleZuordnungHinzufuegen( "K704", "K8");
//		manuelleZuordnungskorrektur.manuelleZuordnungHinzufuegen( "K706", "K8");
//		manuelleZuordnungskorrektur.manuelleZuordnungHinzufuegen( "M704", "M8");
//		
//		
//		
//		manuelleZuordnungskorrektur.manuelleZuordnungDurchziehen(map);
//		
//		SysoutHelper.printKategorieMap(map);
//		
//		aufteiler.mannschaftenVerteilen(map);
//		
//		SysoutHelper.printKategorieMap(map);
//		
//		List<Paarung> paarungen = spiele.generatPaarungenAndSpiele(map);
//		
//		SysoutHelper.printKategorieMap(map);
//		
//		System.out.println(paarungen.size());
//		
//		for (Paarung paarung : paarungen) {
//			System.out.println(paarung.getSpiel());
//		}
//		

//		spieleVerteiler.spieleAutomatischVerteilen(map, paarungen.size());

    }


    private void print(Map<String, Kategorie> map) {
        Set<String> keys = map.keySet();
        List<String> str = new ArrayList<String>();
        for (String string : keys) {
            str.add(string);
        }
        Collections.sort(str);

        for (String key : str) {
            System.out.println(key);
            System.out.println("   " + map.get(key).getName() + " --> " + map.get(key).getGruppeA().getMannschaften());
            if (map.get(key).getGruppeB() != null) {
                System.out.println("           --> " + map.get(key).getGruppeB().getMannschaften());
            } else {
                System.out.println("        --> ");
            }
        }
    }

    void generateMannschaften(int zahl, GeschlechtEnum gesch,

                              Integer klasse) throws SpielPhasenException {

        for (int i = 0; i < zahl; i++) {
            final Mannschaft m = new Mannschaft();
            m.setAnzahlSpieler(10);
            //m.setTeamNummer(i+1);
            m.setGeschlecht(gesch);
            m.setKlasse(klasse);

            mannschaftRepo.save(m);

        }

    }

}
