/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.KorrekturPersistence;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Hilft die Korrekturen einerseits zu speichern und andererseits wieder vorzunehmen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class KorrekturenHelper {

    private static final Logger LOG = Logger.getLogger(KorrekturenHelper.class);

    @Autowired
    private KorrekturPersistence persistence;

    @Autowired
    private SpielZeilenRepository repo;

    public void spielZeileKorrigieren(String id) {

        // sofort ausfuehren
        zeileKorrigieren(id);

        // in den korrekturen speichern
        persistence.save("spielzeile", id);
    }

    private void zeileKorrigieren(String id) {
        // sofort ausfuehren
        SpielZeile zeile = repo.findOne(Long.valueOf(id));
        zeile.setPause(!zeile.isPause());
        repo.save(zeile);
    }

    public void spielzeilenkorrekturAusDbAnwenden() {
        List<String> korrekturen = persistence.getKorrekturen("spielzeile");
        for (String id : korrekturen) {
            zeileKorrigieren(id);
        }
    }


}