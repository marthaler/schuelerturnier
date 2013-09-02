/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.model.Gruppe;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Paarung;
import com.googlecode.madschuelerturnier.model.comperators.KategorieNameComperator;
import com.googlecode.madschuelerturnier.model.helper.IDGeneratorContainer;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.*;
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
public class _4_GeneratePaarungenAndSpiele {

    private static final Logger LOG = Logger.getLogger(_4_GeneratePaarungenAndSpiele.class);

    @Autowired
    private KategorieRepository kategorieRepo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private PaarungRepository paarungRepo;

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

                Paarung paarung = new Paarung();
                paarung = paarungRepo.save(paarung);
                paarung.setMannschaftA(kandidat);
                // gruppe zu paarung
                paarung.setGruppe(gruppeKandidat);
                paarung.setMannschaftB(mannschaften.get(k));
                //kandidat.getPaarungen().add(paarung);


                // todo gegenseitiges aufloesen
                //Mannschaft gegner = paarung.getMannschaftB();
                //gegner.getPaarungen().add(paarung);

                //mannschaftRepo.save(gegner);

                Spiel spiel = new Spiel();
                spiel.setIdString(IDGeneratorContainer.getNext());
                spiel = spielRepo.save(spiel);

                spiel.setPaarung(paarung);


                spiel.setMannschaftA(kandidat);
                spiel.setMannschaftB(mannschaften.get(k));


                spiel = spielRepo.save(spiel);
                paarung.setSpielId(spiel.getIdString());
                paarung.setSpiel(spiel);

                if (toBGruppe) {
                    gruppeKandidat.getKategorie().getGruppeB().getSpiele().add(spiel);
                } else {
                    gruppeKandidat.getSpiele().add(spiel);
                }
                this.gruppeRepo.save(gruppeKandidat);
                paarungRepo.save(paarung);
            }
        }
        mannschaftRepo.save(mannschaften);
    }
}
