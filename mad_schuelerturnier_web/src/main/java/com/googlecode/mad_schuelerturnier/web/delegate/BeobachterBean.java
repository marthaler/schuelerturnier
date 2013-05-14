package com.googlecode.mad_schuelerturnier.web.delegate;

import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLOutConverter;
import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLSchiriConverter;
import com.googlecode.mad_schuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.mad_schuelerturnier.model.Kategorie;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
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
        String result = this.schiri.getTable(this.spielRepo.findGruppenSpielAsc());
        return result;

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
