/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.business.IBusiness;
import com.googlecode.madschuelerturnier.model.enums.PlatzEnum;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class F6SpielverteilerManuelleKorrekturen {

    private static final Logger LOG = Logger.getLogger(F6SpielverteilerManuelleKorrekturen.class);

    @Autowired
    private SpielZeilenRepository spielzeilenRepo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private IBusiness business;

    public void korrekturenVornehmen() {


        // wenn keine korrektur in den einstellungen zu finden ist, mache nichts
        String korr = business.getSpielEinstellungen().getSpielVertauschungen();
        if (korr == null || korr.isEmpty()) {
            LOG.info("starte manuelle nicht, keine werte vorhanden");
            return;
        }

        LOG.info("starte manuelle korrektur: " + korr);

        String[] kos = korr.split(";");
        Map<String, String> vertauschungen = new HashMap<String, String>();

        for (String ko : kos) {
            String[] sp = ko.split("-");
            vertauschungen.put(sp[0], sp[1]);
        }

        for (String key : vertauschungen.keySet()) {


            String value = vertauschungen.get(key);

            LOG.info("vertauschung: " + key + " " + value);

            SpielZeile quelle = findZeile(convertSonntag(key), convertTime(key));
            SpielZeile ziel = findZeile(convertSonntag(value), convertTime(value));

            Spiel quelleSpiel = getSpielFromZeile(quelle, key);
            Spiel zielSpiel = getSpielFromZeile(ziel, value);

            setSpielToZeile(quelle, key, zielSpiel);
            setSpielToZeile(ziel, value, quelleSpiel);


        }

        LOG.info("starte manuelle korrektur: ende");

    }


    private SpielZeile findZeile(boolean sonntag, String zeit) {
        Iterable<SpielZeile> zeilen = spielzeilenRepo.findAll();
        for (SpielZeile spielZeile : zeilen) {
            if (spielZeile.isSonntag() == sonntag) {
                if (spielZeile.getZeitAsString().equals(zeit)) {
                    return spielZeile;
                }
            }
        }
        return null;
    }

    private boolean convertSonntag(String in) {
        return in.contains("so");
    }

    private String convertTime(String in) {
        String[] sp = in.split(",");
        return sp[1];
    }

    private String convertPlatz(String in) {
        String[] sp = in.split(",");
        return sp[2];
    }


    private Spiel getSpielFromZeile(SpielZeile zeile, String key) {

        zeile = this.spielzeilenRepo.findOne(zeile.getId());

        String platz = convertPlatz(key);
        Spiel ret = null;
        if (platz.equals("a")) {
            ret = zeile.getA();
            zeile.setA(null);
        }

        if (platz.equals("b")) {
            ret = zeile.getB();
            zeile.setB(null);
        }

        if (platz.equals("c")) {
            ret = zeile.getC();
            zeile.setC(null);
        }
        this.spielzeilenRepo.save(zeile);
        return ret;
    }

    private void setSpielToZeile(SpielZeile zeile, String key, Spiel spiel) {

        zeile = this.spielzeilenRepo.findOne(zeile.getId());

        String platz = convertPlatz(key);

        if (platz.equals("a")) {
            zeile.setA(spiel);
            spiel.setPlatz(PlatzEnum.A);
        }

        if (platz.equals("b")) {
            zeile.setB(spiel);
            spiel.setPlatz(PlatzEnum.B);
        }

        if (platz.equals("c")) {
            zeile.setC(spiel);
        }

        if (spiel != null) {
            spiel.setStart(zeile.getStart());
            this.spielRepo.save(spiel);
        }

        this.spielzeilenRepo.save(zeile);


    }


}
