/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.vorbereitung;

import ch.emad.business.schuetu.Business;
import ch.emad.model.schuetu.model.Gruppe;
import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.comperators.KategorieNameComperator;
import ch.emad.model.schuetu.model.helper.IDGeneratorContainer;
import ch.emad.persistence.schuetu.repository.GruppeRepository;
import ch.emad.persistence.schuetu.repository.KategorieRepository;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class D4GeneratePaarungenAndSpiele {

    private static final Logger LOG = Logger.getLogger(D4GeneratePaarungenAndSpiele.class);

    @Autowired
    private KategorieRepository kategorieRepo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private GruppeRepository gruppeRepo;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private Business business;

    public void generatPaarungenAndSpiele() {

        int spieleCount = 0;

        List<Kategorie> list = business.getKategorien();

        Collections.sort(list, new KategorieNameComperator());

        for (Kategorie kategorie : list) {

            if (kategorie.getGruppeA() == null) {
                continue;
            }

            final List<Mannschaft> a = kategorie.getGruppeA().getMannschaften();
            final List<Mannschaft> b = kategorie.getGruppeB().getMannschaften();

            // vor und rueckrunde = a 2*
            if (a.size() == 3) {
                assign(a, true);
            } else if
                //b vorhanden
                    (!b.isEmpty()) {
                assign(b, false);
            }

            // alle immer
            assign(a, false);

            kategorie = kategorieRepo.findOne(kategorie.getId());

            spieleCount += kategorie.getSpiele().size();
            LOG.info("paarungen und spiele zu kategorie " + kategorie.getName() + " zugeordnet: " + kategorie.getSpiele().size());

        }
        LOG.info(" zugeordnet total: " + spieleCount);
    }


    private void assign(final List<Mannschaft> mannschaften, boolean toBGruppe) {
        for (int i = 0; i < mannschaften.size(); i++) {
            final Mannschaft kandidat = mannschaften.get(i);

            Gruppe gruppeKandidat = kandidat.getGruppe();

            for (int k = i + 1; k < mannschaften.size(); k++) {

                Spiel spiel = new Spiel();
                spiel.setIdString(IDGeneratorContainer.getNext());
                spiel.setKategorieName(gruppeKandidat.getKategorie().getName());
                spiel = spielRepo.save(spiel);

                spiel.setMannschaftA(kandidat);
                spiel.setMannschaftB(mannschaften.get(k));

                spiel = spielRepo.save(spiel);


                // gruppe b holen falls gewuenscht
                if (toBGruppe) {
                    gruppeKandidat = gruppeKandidat.getKategorie().getGruppeB();
                }

                // scheint mit aktuellem jpa noetig zu sein um die zwischentabelle GRUPPE_SPIELE aufzuraeumen
                // Spiele wegnehmen und neu hinzufuegen
                List<Spiel> spiele = gruppeKandidat.getSpiele();
                gruppeKandidat.getSpiele().clear();
                gruppeKandidat = this.gruppeRepo.save(gruppeKandidat);

                for (Spiel sp : spiele) {
                    gruppeKandidat.getSpiele().add(sp);
                }

                // eigentliches Spiel hinzufuegen
                gruppeKandidat.getSpiele().add(spiel);

                this.gruppeRepo.save(gruppeKandidat);
            }
        }
        mannschaftRepo.save(mannschaften);
    }
}
