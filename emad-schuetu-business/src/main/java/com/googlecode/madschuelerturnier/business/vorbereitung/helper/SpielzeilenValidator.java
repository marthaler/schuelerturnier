/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung.helper;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Business business;

    public String validateSpielZeilen(SpielZeile zeileVorher,SpielZeile zeileVorVorher, SpielZeile zeileJetzt) {

        int zweipausenBisKlasse = business.getSpielEinstellungen().getZweiPausenBisKlasse();

        // Mannschafts konflikt reseten
        for (Mannschaft m : zeileJetzt.getAllMannschaften()) {
            m.setKonflikt(false);
        }

        String ret = "";
        Set<Mannschaft> konflikte = new HashSet<Mannschaft>();

        // pruefung durchfuehren
        ret = pruefeObInVorherigerZeileVorhanden(zeileVorVorher, zeileJetzt, ret, konflikte, true,zweipausenBisKlasse );

        ret = pruefeObInVorherigerZeileVorhanden(zeileVorher, zeileJetzt, ret, konflikte, false,zweipausenBisKlasse);

        ret = pruefeDoppelteIngleicherZeile(zeileJetzt, ret);

        zeileJetzt.setKonfliktText(ret);
        return ret;
    }

    private String pruefeDoppelteIngleicherZeile(SpielZeile zeileJetzt, String ret) {
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

            ret = ret + " in dieser zeile hat es doppelte mannschaften:";

            for (Mannschaft mannschaft : doppelte) {
                ret = ret + " " + mannschaft.getName();
            }

            ret = ret + "!";
        }
        return ret;
    }

    private String pruefeObInVorherigerZeileVorhanden(SpielZeile zeileVorher, SpielZeile zeileJetzt, String retIn, Set<Mannschaft> konflikte, boolean vorvorher,int zweipausenBisKlasse) {

        String ret = retIn;

        if (zeileVorher != null) {
            List<Mannschaft> vorherList = zeileVorher.getAllMannschaften();

            for (Mannschaft jetzt : zeileJetzt.getAllMannschaften()) {

                // skip wenn vorvorher und groesser als zweipausenBisKlasse
                if(vorvorher && zweipausenBisKlasse < jetzt.getKlasse()){
                    continue;
                }

                if (vorherList.contains(jetzt)) {
                    jetzt.setKonflikt(true);
                    konflikte.add(jetzt);
                }
            }
            if (konflikte.size() > 0) {
                if(vorvorher){
                    ret = ret + " Bereits in der vor-voherigen zeile vorhanden:";
                } else {
                    ret = ret + " Bereits in der voherigen zeile vorhanden:";
                }
                for (Mannschaft mannschaft : konflikte) {
                    ret = ret + " " + mannschaft.getName();
                }
                ret = ret + "!";
            }
        }
        return ret;
    }

}