/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.spieldurchfuehrung;

import ch.emad.business.schuetu.Business;
import ch.emad.business.schuetu.controller.resultate.ResultateVerarbeiter;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielEinstellungen;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@Component
public class SpielRepositoryDelegate {

    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private ResultateVerarbeiter resultateVerarbeiter;

    @Autowired
    private Business business;

    public List<Spiel> findAllEinzutragende() {
        return spielRepository.findAllEinzutragende();
    }

    public List<Spiel> findAllZuBestaetigen() {
        return spielRepository.findAllZuBestaetigen();
    }

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

                    SpielEinstellungen einst = business.getSpielEinstellungen();
                    einst.setStarttag(spiel.getStart());
                    einst.setStart(spiel.getStart());
                    business.saveEinstellungen(einst);

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
