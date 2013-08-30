package com.googlecode.mad_schuelerturnier.business.vorbereitung;

import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.business.utils.MannschaftenNummerierer;
import com.googlecode.mad_schuelerturnier.exceptions.SpielPhasenException;
import com.googlecode.mad_schuelerturnier.model.Kategorie;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.mad_schuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class TestKategorienZuordnerTest {

    private static final Logger LOG = Logger.getLogger(TestKategorienZuordnerTest.class);

    @Autowired
    private Business business;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private _3_MannschaftenAufteiler aufteiler;

    @Autowired
    private _4_GeneratePaarungenAndSpiele spiele;

    @Autowired
    private _5_Spielverteiler spieleVerteiler;

    @Test
    @Ignore
    public void mannschaftenZuordnen2012Test() throws SpielPhasenException {

        List<Mannschaft> result2 = business.getMannschaften();

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

        Collections.sort((List) result, new MannschaftsNamenComperator());

    }


    private void print(Map<String, Kategorie> map) {
        Set<String> keys = map.keySet();
        List<String> str = new ArrayList<String>();
        for (String string : keys) {
            str.add(string);
        }
        Collections.sort(str);

        for (String key : str) {
            LOG.info("" + key);
            LOG.info("" + "   " + map.get(key).getName() + " --> " + map.get(key).getGruppeA().getMannschaften());
            if (map.get(key).getGruppeB() != null) {
                LOG.info("" + "           --> " + map.get(key).getGruppeB().getMannschaften());
            } else {
                LOG.info("" + "        --> ");
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
