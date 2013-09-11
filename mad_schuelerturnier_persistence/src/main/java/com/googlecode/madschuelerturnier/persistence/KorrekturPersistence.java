/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence;

import com.googlecode.madschuelerturnier.model.Korrektur;
import com.googlecode.madschuelerturnier.persistence.repository.KorrekturRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Sortiert die gewuenschten Korrektur Eintraege heraus und gibt eine Liste zurueck, welche nach
 * der Erstellung sortiert eingetragen wird
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@Component
public class KorrekturPersistence {

    @Autowired
    private KorrekturRepository repo;

    public void save(String typ, String value) {
        Korrektur korr = new Korrektur();
        korr.setTyp(typ);
        korr.setWert(value);
        korr.setReihenfolge(repo.count() + 1);
        repo.save(korr);
    }

    public List<String> getKorrekturen(String typ) {
        List<String> result = new ArrayList<String>();
        List<Korrektur> korrList = repo.findAll();
        for (Korrektur ko : korrList) {
            if (ko.getTyp().equalsIgnoreCase(typ)) {
                result.add(ko.getWert());
            }
        }
        return result;
    }
}
