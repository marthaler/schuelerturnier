package com.googlecode.mad_schuelerturnier.business.spielkorrektur;

import com.googlecode.mad_schuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.mad_schuelerturnier.model.Text;
import com.googlecode.mad_schuelerturnier.model.comperators.SpielZeitComperator;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 20.06.13
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
@Component
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
    public Spiel findSpiel(String id) {
       Spiel sp =  repo.findOne(Long.parseLong(id));

        if(sp.getNotizen() == null){
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
