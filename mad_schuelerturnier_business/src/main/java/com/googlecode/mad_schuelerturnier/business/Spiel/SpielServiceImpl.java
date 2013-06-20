package com.googlecode.mad_schuelerturnier.business.Spiel;

import com.googlecode.mad_schuelerturnier.business.SpielzeilenValidator;
import com.googlecode.mad_schuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.mad_schuelerturnier.model.comperators.SpielZeitComperator;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielEinstellungenRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielZeilenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 20.06.13
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
public class SpielServiceImpl implements  SpielService {

    @Autowired
    private SpielRepository repo;

    @Autowired
    private ResultateVerarbeiter verarbeiter;

    @Override
    public List<Spiel> readAllSpiele() {
        List<Spiel> spiele = repo.findAll();
        Collections.sort(spiele,new SpielZeitComperator());
        return spiele;
    }

    @Override
    public void saveKorrigiertesSpiel(Spiel spiel) {
        this.repo.save(spiel);
    }
}
