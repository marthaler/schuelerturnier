/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.web.delegate;

import ch.emad.business.schuetu.controller.leiter.converter.HTMLOutConverter;
import ch.emad.business.schuetu.controller.leiter.converter.HTMLSchiriConverter;
import ch.emad.business.schuetu.controller.resultate.ResultateVerarbeiter;
import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.persistence.schuetu.repository.KategorieRepository;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.bean.SessionScoped;
import java.util.*;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
@SessionScoped
public class BeobachterBean {

    private String auswahl = "";

    private String schiriAuswahl = "";

    @Autowired
    private KategorieRepository repo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private HTMLSchiriConverter schiri;

    @Autowired
    private HTMLOutConverter historieGenerator;

    @Autowired
    private ResultateVerarbeiter verarbeiter;

    private static final Logger LOG = Logger.getLogger(BeobachterBean.class);

    public List<String> getKategorien() {


        Set<String> result = new HashSet<String>();
        for (Kategorie kat : repo.findAll()) {
            if (kat.getGruppeA() != null) {
                result.add(kat.getName());
            } else {
                LOG.warn("achtung, kategorie ohne gruppe a: " + kat);
            }
        }

        result.addAll(this.verarbeiter.getKeys());

        List<String> list = new ArrayList<String>();
        list.addAll(result);
        Collections.sort(list);

        return list;
    }

    public String getMatrix() {
        return this.verarbeiter.generateSpieleMatrix();
    }

    public String getHistorie() {
        return this.verarbeiter.generateRanglistenHistorieForKategorieName(this.auswahl);
    }

    public String getRangliste() {
        return this.verarbeiter.getRangliste();
    }

    public String getSchiriZettel() {

        if (schiriAuswahl.contains("finale")) {
            List<Spiel> finale = this.spielRepo.findFinalSpielAsc();
            if (finale != null && finale.size() > 0) {
                return this.schiri.getTable(finale);
            }
            return "keine Finale";

        }
        return this.schiri.getTable(this.spielRepo.findGruppenSpielAsc());

    }

    public void dumpOutPages() {
        this.historieGenerator.dumpoutPages();
    }


    public String getAuswahl() {
        return auswahl;
    }

    public void setAuswahl(String auswahl) {
        this.auswahl = auswahl;
    }

    public String getSchiriAuswahl() {
        return schiriAuswahl;
    }

    public void setSchiriAuswahl(String schiriAuswahl) {
        this.schiriAuswahl = schiriAuswahl;
    }

    public void neuBerechnen() {
        this.verarbeiter.initialisieren();
    }
}
