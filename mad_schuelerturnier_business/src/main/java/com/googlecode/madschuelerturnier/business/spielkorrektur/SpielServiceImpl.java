package com.googlecode.madschuelerturnier.business.spielkorrektur;

import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.model.Text;
import com.googlecode.madschuelerturnier.model.comperators.SpielZeitComperator;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
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
        Spiel sp = repo.findOne(Long.parseLong(id));

        if (sp.getNotizen() == null) {
            sp.setNotizen(new Text());
            sp = repo.save(sp);
        }
        return sp;
    }

    @Override
    public void doKorrektur(Spiel spiel) {
        repo.save(spiel);
        verarbeiter.neuberechnenDerKategorie(spiel.getGruppe().getKategorie());
    }
}
