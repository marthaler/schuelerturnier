/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.vorbereitung;

import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielZeile;
import ch.emad.model.schuetu.model.enums.PlatzEnum;
import ch.emad.persistence.schuetu.KorrekturPersistence;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import ch.emad.persistence.schuetu.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fuehrt die manuellen Korrekturen aus, welche Spiele in den Spiel Zeilen vertauschen
 *
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
    private KorrekturPersistence korrekturPersistence;

    public void korrekturenVornehmen() {

        // wenn keine korrektur in den einstellungen zu finden ist, mache nichts
        List<String> korr = korrekturPersistence.getKorrekturen("spielvertauschung");
        if (korr == null || korr.isEmpty()) {
            LOG.info("starte manuelle Korrektur nicht, keine werte vorhanden");
            return;
        }

        LOG.info("starte manuelle korrektur: " + korr);

        Map<String, String> vertauschungen = new HashMap<String, String>();

        for (String ko : korr) {
            ko = ko.replace(";", "");
            String[] sp = ko.split("-");
            vertauschungen.put(sp[0], sp[1]);
        }

        for (String key : vertauschungen.keySet()) {

            String value = vertauschungen.get(key);

            LOG.info("vertauschung: " + key + " " + value);

            SpielZeile quelle = findZeile(convertSonntag(key), convertTime(key));
            LOG.info("vertauschung quellezeile: " + quelle);
            SpielZeile ziel = findZeile(convertSonntag(value), convertTime(value));
            LOG.info("vertauschung zielzeile: " + ziel);

            Spiel quelleSpiel = getSpielFromZeile(quelle, key);
            Spiel zielSpiel = getSpielFromZeile(ziel, value);

            LOG.info("vertauschung quellespiel: " + quelleSpiel);
            setSpielToZeile(quelle, key, zielSpiel);
            LOG.info("vertauschung zielspiel: " + zielSpiel);
            setSpielToZeile(ziel, value, quelleSpiel);
            LOG.info("vertauschung fertig.");
        }

        LOG.info("starte manuelle korrektur: ende");

    }


    private SpielZeile findZeile(boolean sonntag, String zeit) {
        Iterable<SpielZeile> zeilen = spielzeilenRepo.findAll();
        for (SpielZeile spielZeile : zeilen) {
            LOG.debug("found:" + spielZeile);
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


    private Spiel getSpielFromZeile(SpielZeile zeileIn, String key) {

        SpielZeile zeile = this.spielzeilenRepo.findOne(zeileIn.getId());

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

    private void setSpielToZeile(SpielZeile zeileIn, String key, Spiel spiel) {

        SpielZeile zeile = this.spielzeilenRepo.findOne(zeileIn.getId());

        String platz = convertPlatz(key);

        if (platz.equals("a")) {
            zeile.setA(spiel);
            if (spiel != null) {
                spiel.setPlatz(PlatzEnum.A);
            }
        }

        if (platz.equals("b")) {
            zeile.setB(spiel);
            if (spiel != null) {
                spiel.setPlatz(PlatzEnum.B);
            }
        }

        if (platz.equals("c")) {
            zeile.setC(spiel);
            if (spiel != null) {
                spiel.setPlatz(PlatzEnum.C);
            }
        }

        if (spiel != null) {
            spiel.setStart(zeile.getStart());
            this.spielRepo.save(spiel);
        }

        this.spielzeilenRepo.save(zeile);

    }

}
