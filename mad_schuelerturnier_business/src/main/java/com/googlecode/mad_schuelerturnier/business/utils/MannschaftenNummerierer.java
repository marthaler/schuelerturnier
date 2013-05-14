/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.utils;

import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.comperators.MannschaftsCretionDateComperator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class MannschaftenNummerierer {

    private static final Logger LOG = Logger.getLogger(MannschaftenNummerierer.class);

    public List<Mannschaft> mannschaftenNummerieren(List<Mannschaft> list) {

        // sortieren
        Collections.sort(list, new MannschaftsCretionDateComperator());

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (Mannschaft mannschaft : list) {
            String key = "" + mannschaft.getGeschlecht()
                    + mannschaft.getKlasse();
            Integer nummer = map.get(key);
            if (nummer == null) {
                nummer = Integer.valueOf(1);
            } else {
                nummer = nummer + 1;
            }

            mannschaft.setTeamNummer(nummer);
            map.put(key, nummer);
        }
        return list;
    }
}
