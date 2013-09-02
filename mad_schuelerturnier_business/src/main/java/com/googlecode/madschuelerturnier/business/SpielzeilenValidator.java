/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class SpielzeilenValidator {

    public String validateSpielZeilen(SpielZeile zeileVorher,
                                      SpielZeile zeileJetzt) {

        // Mannschafts konflikt reseten
        for (Mannschaft m : zeileJetzt.getAllMannschaften()) {
            m.setKonflikt(false);
        }

        String ret = "";
        Set<Mannschaft> konflikte = new HashSet<Mannschaft>();
        if (zeileVorher != null) {
            List<Mannschaft> vorherList = zeileVorher.getAllMannschaften();

            for (Mannschaft jetzt : zeileJetzt.getAllMannschaften()) {
                if (vorherList.contains(jetzt)) {
                    jetzt.setKonflikt(true);
                    konflikte.add(jetzt);

                }
            }
            if (konflikte.size() > 0) {
                ret = ret + " Bereits in der voherigen Zeile vorhanden:";
                for (Mannschaft mannschaft : konflikte) {
                    ret = ret + " " + mannschaft.getName();
                }
                ret = ret + "!";
            }
        }

        Set<Mannschaft> doppelte = new HashSet<Mannschaft>();
        for (Mannschaft jetzt : zeileJetzt.getAllMannschaften()) {
            int i = 0;
            for (Mannschaft vergleich : zeileJetzt.getAllMannschaften()) {
                if (jetzt.equals(vergleich)) {
                    i++;
                }
            }
            if (i > 1) {
                jetzt.setKonflikt(true);
                doppelte.add(jetzt);
            }
        }

        if (doppelte.size() > 0) {

            ret = ret + " In dieser Zeile hat es doppelte Mannschaften:";

            for (Mannschaft mannschaft : doppelte) {
                ret = ret + " " + mannschaft.getName();
            }

            ret = ret + "!";
        }
        zeileJetzt.setKonfliktText(ret);
        return ret;
    }

}