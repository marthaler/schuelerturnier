/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.spielkorrektur;

import ch.emad.business.schuetu.controller.resultate.ResultateVerarbeiter;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.comperators.SpielZeitComperator;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class SpielServiceImpl implements SpielService {

    @Autowired
    private SpielRepository repo;

    @Autowired
    private ResultateVerarbeiter verarbeiter;

    @Override
    public List<Spiel> readAllSpiele() {
        List<Spiel> spiele = repo.findAll();
        Collections.sort(spiele, new SpielZeitComperator());
        return spiele;
    }


    @Override
    public Spiel findSpiel(String id) {
        return repo.findOne(Long.parseLong(id));
    }

    @Override
    public void doKorrektur(Spiel spiel) {
        repo.save(spiel);
        verarbeiter.neuberechnenDerKategorie(spiel.getGruppe().getKategorie());
    }
}
