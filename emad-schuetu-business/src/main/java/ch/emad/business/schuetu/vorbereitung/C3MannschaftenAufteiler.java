/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.vorbereitung;

import ch.emad.business.schuetu.Business;
import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.comperators.MannschaftsNamenComperator;
import ch.emad.model.schuetu.model.enums.SpielEnum;
import ch.emad.model.schuetu.model.helper.IDGeneratorContainer;
import ch.emad.persistence.schuetu.repository.KategorieRepository;
import ch.emad.persistence.schuetu.repository.SpielRepository;
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
public class C3MannschaftenAufteiler {

    private static final Logger LOG = Logger.getLogger(C3MannschaftenAufteiler.class);

    @Autowired
    private KategorieRepository kategorieRepo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private Business business;

    public void mannschaftenVerteilen() {

        leereKategorienLoeschen();

        // falls die gruppe a genau 3 mannschaften hat gibt es eine vor- und eine rueckrunde
        // bei mehr als 7 werden die gruppen geteilt
        Iterable<Kategorie> kat = kategorieRepo.findAll();

        List<Long> kategorieIDs = new ArrayList<Long>();

        for (Kategorie kategorie : kat) {
            kategorieIDs.add(kategorie.getId());
        }

        for (Long id : kategorieIDs) {

            Kategorie kategorie = kategorieRepo.findOne(id);

            // todo problem loesen
            // wegen dem jpa problem, dass kategorie nicht mehr gelöscht werden kann
            if (kategorie.getGruppeA() == null) {
                continue;
            }
            int mannschaftenSize = kategorie.getGruppeA().getMannschaften().size();

            if (mannschaftenSize < 3) {
                LOG.fatal("!!! achtung, es wurde versucht eine manschaftsverteilung vorzunehmen mit einer gruppe, die weniger als 3 mannschaften hat");
            } else if (mannschaftenSize == 3) {

                kategorie = genau3Mannschaften(kategorie);

            } else if (mannschaftenSize > 7) {

                kategorie = groesserAls7Mannschaften(kategorie, mannschaftenSize);

            } else {

                dreiBis7Mannschaften(kategorie);

            }

            kategorieRepo.save(kategorie);
        }
    }

    private void dreiBis7Mannschaften(Kategorie kategorie) {

        // Grosser Final
        Spiel gf = new Spiel();
        gf.setTyp(SpielEnum.GFINAL);
        gf.setIdString(IDGeneratorContainer.getNext());
        gf.setKategorieName(kategorie.getName());

        // selber setzen des Namen, bei kategorien, mit 2 klassen
        if (kategorie.isMixedKlassen() && business.getSpielEinstellungen().isBehandleFinaleProKlassebeiZusammengefuehrten()) {
            gf.setRealName("GrFin-" + kategorie.getMannschaften().get(0).getGeschlecht() + "Kl" + kategorie.getKlassen().get(0));
        }

        gf = spielRepo.save(gf);
        kategorie.setGrosserFinal(gf);

        Spiel kf = new Spiel();
        // den Fall behandeln wenn es 2 Grosse Finale gibt bei zusammengefassten Mannschaften
        if (kategorie.isMixedKlassen() && business.getSpielEinstellungen().isBehandleFinaleProKlassebeiZusammengefuehrten()) {
            kf.setTyp(SpielEnum.GFINAL);
            kf.setRealName("GrFin-" + kategorie.getMannschaften().get(0).getGeschlecht() + "Kl" + kategorie.getKlassen().get(1));
        } else {
            kf.setTyp(SpielEnum.KFINAL);
        }
        kf.setIdString(IDGeneratorContainer.getNext());
        kf.setKategorieName(kategorie.getName());
        kf = spielRepo.save(kf);
        kategorie.setKleineFinal(kf);
    }

    private Kategorie genau3Mannschaften(Kategorie kategorieIn) {

        Kategorie kategorie = kategorieIn;
        LOG.debug("vor und rueckrunge stehen an: " + kategorie.getName());

        assignGrosserFinalToKategorie(kategorie);

        kategorie = kategorieRepo.save(kategorie);
        return kategorie;
    }

    private void leereKategorienLoeschen() {
        // leere kategorien loeschen
        Iterable<Kategorie> all = kategorieRepo.findAll();
        for (Kategorie kategorie : all) {
            if (kategorie.getGruppeA() == null) {
                LOG.info("_1_KategorieZuordner hat Kategorie ohne Gruppe A: loeschen");
                kategorieRepo.delete(kategorie);
            }
        }
    }

    private Kategorie groesserAls7Mannschaften(Kategorie kategorieIn, int mannschaftenSize) {

        Kategorie kategorie = kategorieIn;

        LOG.debug("aufteilen einer gruppe von: " + kategorie.getName() + " anzahl mannschaften: " + mannschaftenSize);

        kategorie = kategorieRepo.findOne(kategorie.getId());

        Float f = ((float) mannschaftenSize / 2);
        int ersteHaelfte = Math.round(f);


        for (int i = mannschaftenSize - 1; i > ersteHaelfte - 1; i--) {

            Mannschaft mtemp = kategorie.getGruppeA().getMannschaften().remove(i);
            mtemp.setGruppe(kategorie.getGruppeB());
            kategorie.getGruppeB().getMannschaften().add(mtemp);
        }

        Collections.sort(kategorie.getGruppeB().getMannschaften(), new MannschaftsNamenComperator());
        Collections.sort(kategorie.getGruppeA().getMannschaften(), new MannschaftsNamenComperator());

        dreiBis7Mannschaften(kategorie);

        kategorie = kategorieRepo.save(kategorie);

        return kategorie;
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
