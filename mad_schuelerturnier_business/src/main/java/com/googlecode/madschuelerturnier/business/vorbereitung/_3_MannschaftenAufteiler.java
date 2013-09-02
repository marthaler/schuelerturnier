/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.model.enums.SpielEnum;
import com.googlecode.madschuelerturnier.model.helper.IDGeneratorContainer;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class _3_MannschaftenAufteiler {

    private static final Logger LOG = Logger.getLogger(_3_MannschaftenAufteiler.class);

    @Autowired
    private KategorieRepository kategorieRepo;

    @Autowired
    private SpielRepository spielRepo;

    public void mannschaftenVerteilen() {

        // leere kategorien loeschen
        Iterable<Kategorie> all = kategorieRepo.findAll();
        for (Kategorie kategorie : all) {
            if (kategorie.getGruppeA() == null) {
                LOG.info("_1_KategorieZuordner hat Kategorie ohne Gruppe A: löschen");
                //   kategorieRepo.delete(kategorie);
            }
        }

        // falls die gruppe a genau 3 mannschaften hat gibt es eine vor- und eine rueckrunde
        // bei mehr als 7 werden die gruppen geteilt
        List<Kategorie> kategorienList = new ArrayList<Kategorie>();
        Iterable<Kategorie> kat = kategorieRepo.findAll();

        List<Long> kategorieIDs = new ArrayList<Long>();

        for (Kategorie kategorie : kat) {
            kategorieIDs.add(kategorie.getId());
        }

        for (Long id : kategorieIDs) {

            Kategorie kategorie = kategorieRepo.findOne(id);

            // todo problem loesen
            // wegen dem jpa problem, dass kategorie nicht mehr gelöscht erden kann
            if (kategorie.getGruppeA() == null) {
                continue;
            }
            int mannschaftenSize = kategorie.getGruppeA().getMannschaften().size();

            if (mannschaftenSize < 3) {
                LOG.fatal("!!! achtung, es wurde versucht eine manschaftsverteilung vorzunehmen mit einer gruppe, die weniger als 3 mannschaften hat");
            } else if (mannschaftenSize == 3) {
                LOG.debug("vor und rueckrunge stehen an: " + kategorie.getName());

                assignGrosserFinalToKategorie(kategorie);


                kategorie = kategorieRepo.save(kategorie);


            } else if (mannschaftenSize > 7) {
                LOG.debug("aufteilen einer gruppe von: " + kategorie.getName() + " anzahl mannschaften: " + mannschaftenSize);

                kategorie = kategorieRepo.findOne(kategorie.getId());

                Float f = (Float.valueOf(mannschaftenSize) / 2);
                int ersteHaelfte = Math.round(f);


                for (int i = mannschaftenSize - 1; i > ersteHaelfte - 1; i--) {

                    Mannschaft mtemp = kategorie.getGruppeA().getMannschaften().remove(i);
                    mtemp.setGruppe(kategorie.getGruppeB());
                    kategorie.getGruppeB().getMannschaften().add(mtemp);

                }

                Collections.sort(kategorie.getGruppeB().getMannschaften(), new MannschaftsNamenComperator());
                Collections.sort(kategorie.getGruppeA().getMannschaften(), new MannschaftsNamenComperator());

                assignGrosserFinalToKategorie(kategorie);


                Spiel kf = new Spiel();
                kf.setTyp(SpielEnum.KFINAL);
                kf.setIdString(IDGeneratorContainer.getNext());
                kf.setKategorieName(kategorie.getName());
                kf = spielRepo.save(kf);
                kategorie.setKleineFinal(kf);

                kategorie = kategorieRepo.save(kategorie);

            } else {

                assignGrosserFinalToKategorie(kategorie);

                Spiel kf = new Spiel();
                kf.setTyp(SpielEnum.KFINAL);
                kf.setIdString(IDGeneratorContainer.getNext());
                kf.setKategorieName(kategorie.getName());
                kf = spielRepo.save(kf);
                kategorie.setKleineFinal(kf);

            }

            kategorieRepo.save(kategorie);
        }
    }

    private void assignGrosserFinalToKategorie(Kategorie kategorie) {
        Spiel gf = new Spiel();
        gf.setTyp(SpielEnum.GFINAL);
        gf.setIdString(IDGeneratorContainer.getNext());
        gf.setKategorieName(kategorie.getName());
        gf = spielRepo.save(gf);
        kategorie.setGrosserFinal(gf);
    }

}
