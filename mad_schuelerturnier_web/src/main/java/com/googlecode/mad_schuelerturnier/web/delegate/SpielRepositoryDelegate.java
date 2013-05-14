package com.googlecode.mad_schuelerturnier.web.delegate;

import com.googlecode.mad_schuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 01.01.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */   //
@Component
public class SpielRepositoryDelegate {

    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private ResultateVerarbeiter resultateVerarbeiter;

    public List<Spiel> findAllEinzutragende() {
        return spielRepository.findAllEinzutragende();
    }

    public List<Spiel> findAllZuBestaetigen() {
        return spielRepository.findAllZuBestaetigen();
    }

    //
    public synchronized void eintragen(List<Spiel> spiele, String id) {
        long idl = Long.parseLong(id);
        for (Spiel spiel : spiele) {

            if (spiel.getId() == idl && spiel.getToreA() > -1 && spiel.getToreB() > -1) {
                spiel.setFertigEingetragen(true);
                spielRepository.save(spiel);
            }
        }
    }

    public synchronized void bestaetigen(List<Spiel> spiele, String id, String ok) {
        long idl = Long.parseLong(id);
        for (Spiel spiel : spiele) {

            if (spiel.getId() == idl) {

                if (ok.equals("ok")) {
                    spiel.setFertigBestaetigt(true);
                    spiel.setToreABestaetigt(spiel.getToreA());
                    spiel.setToreBBestaetigt(spiel.getToreB());
                    spielRepository.save(spiel);
                    resultateVerarbeiter.signalFertigesSpiel(spiel.getId());
                } else {
                    spiel.setZurueckgewiesen(true);
                    spiel.setFertigEingetragen(false);
                }
                spielRepository.save(spiel);

            }
        }
    }
}
