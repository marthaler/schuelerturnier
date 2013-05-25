/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.vorbereitung;

import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dient dazu den Ueberblick uber die bereits eingeplanten Spiele zu behalten
 * Es darf nicht mehr vorkommen, dass eine Kategorie am Samstag Abend und am Sonntag morgen spielt
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class SpielverteilerHelper {

    private final static String CONSUMED_KEY = "consumed";
    private final static String AVAILABLE_KEY = "available";

    private static final Logger LOG = Logger.getLogger(SpielverteilerHelper.class);

    private Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();

    public void init(List<Spiel> spiele) {
        map.clear();
        for (Spiel spiel : spiele) {
            String katname = spiel.getMannschaftA().getKategorie().getName();

            Map<String, Integer> counter = map.get(katname);
            if (counter == null) {
                counter = new HashMap<String, Integer>();
                counter.put(CONSUMED_KEY, 0);
                counter.put(AVAILABLE_KEY, 0);
            }

            Integer avn = counter.get(AVAILABLE_KEY);
            avn = avn + 1;
            counter.put(AVAILABLE_KEY, avn);

            map.put(katname, counter);
        }
    }

    public boolean isFirstSpielInGruppe(Spiel spiel) {
        String katname = spiel.getMannschaftA().getKategorie().getName();
        Integer res = map.get(katname).get(CONSUMED_KEY);
        if (res == 0) {
            return true;
        }
        return false;
    }

    public void consumeSpiel(Spiel spiel) {
        String katname = spiel.getMannschaftA().getKategorie().getName();

        Map<String, Integer> counter = map.get(katname);
        Integer avn = counter.get(AVAILABLE_KEY);
        avn = avn - 1;
        counter.put(AVAILABLE_KEY, avn);

        Integer con = counter.get(CONSUMED_KEY);
        con = con + 1;
        counter.put(CONSUMED_KEY, con);

        map.put(katname, counter);
    }
}
