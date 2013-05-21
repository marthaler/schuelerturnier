package com.googlecode.mad_schuelerturnier.business.controller.resultate;

import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLConverterRangliste;
import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLOutConverter;
import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLSpielMatrixConverter;
import com.googlecode.mad_schuelerturnier.business.out.OutToWebsitePublisher;
import com.googlecode.mad_schuelerturnier.business.print.SpielPrintManager;
import com.googlecode.mad_schuelerturnier.model.Kategorie;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.mad_schuelerturnier.model.compusw.RanglisteneintragHistorie;
import com.googlecode.mad_schuelerturnier.model.enums.SpielEnum;
import com.googlecode.mad_schuelerturnier.model.spiel.Penalty;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 04.01.13
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ResultateVerarbeiter {

    @Autowired
    private HTMLSpielMatrixConverter matrix;

    @Autowired
    private HTMLOutConverter historieGenerator;

    @Autowired
    private HTMLConverterRangliste rangliste;

    @Autowired
    private SpielRepository repo;

    @Autowired
    private KategorieRepository katRepo;

    @Autowired
    private OutToWebsitePublisher ftpPublisher;

    @Autowired
    private SpielPrintManager printer;

    private static final Logger LOG = Logger.getLogger(ResultateVerarbeiter.class);

    private Map<String, RanglisteneintragHistorie> map = new HashMap<String, RanglisteneintragHistorie>();

    public void signalPenalty(Penalty p) {

        //nachladen num sichergehen, dass die penaltys aktuelle sind
        String katName = p.getKategorie().getKategorie().getName();

        LOG.info("verarbeite penalty: " + p);

        map.remove(katName);
        map.remove(katName + "_A");
        map.remove(katName + "_B");

        this.initialisierenKategorie(p.getKategorie().getKategorie());
    }

    public void signalFertigesSpiel(Long id) {

        LOG.info("verarbeite fertiges spiel: " + id);
        Spiel spiel = repo.findOne(id);
        Kategorie kat = spiel.getMannschaftA().getKategorie();
        String katName = spiel.getMannschaftA().getKategorie().getName();

        RanglisteneintragHistorie rangListe = null;

        // hat a und b  = mehr als 7 mannschaften
        if (!spiel.getGruppe().getKategorie().hasVorUndRueckrunde() && spiel.getGruppe().getKategorie().getGruppeB().getMannschaften().size() > 0) {

            // mannschaft a des spiels ist in gruppe a
            if (spiel.getGruppe().getKategorie().getGruppeA().getMannschaften().contains(spiel.getMannschaftA()) && spiel.getTyp() == SpielEnum.GRUPPE) {
                rangListe = map.get(katName + "_A");

                if (rangListe == null) {
                    rangListe = new RanglisteneintragHistorie(spiel, null, Boolean.TRUE);
                } else {
                    rangListe = new RanglisteneintragHistorie(spiel, rangListe, Boolean.TRUE);
                }

                if (rangListe.isPenaltyAuswertungNoetig()) {
                    rangListe = new RanglisteneintragHistorie(null, rangListe, Boolean.TRUE);
                }

                map.put(katName + "_A", rangListe);

            }

            // mannschaft a des spiels ist in gruppe b
            if (spiel.getGruppe().getKategorie().getGruppeB().getMannschaften().contains(spiel.getMannschaftA()) && spiel.getTyp() == SpielEnum.GRUPPE) {

                rangListe = map.get(katName + "_B");

                if (rangListe == null) {
                    rangListe = new RanglisteneintragHistorie(spiel, null, Boolean.FALSE);
                } else {
                    rangListe = new RanglisteneintragHistorie(spiel, rangListe, Boolean.FALSE);
                }
                if (rangListe.isPenaltyAuswertungNoetig()) {
                    rangListe = new RanglisteneintragHistorie(null, rangListe, Boolean.FALSE);
                }


                map.put(katName + "_B", rangListe);

            }
        }

        // normalen eintrag

        rangListe = map.get(katName);

        if (rangListe == null) {
            rangListe = new RanglisteneintragHistorie(spiel, null, null);
        } else {
            rangListe = new RanglisteneintragHistorie(spiel, rangListe, null);
        }

        if (rangListe.isPenaltyAuswertungNoetig()) {
            rangListe = new RanglisteneintragHistorie(null, rangListe, null);
        }

        map.put(katName, rangListe);

        // pruefen ob penalty noetig !!!
        Penalty penA = rangListe.getPenaltyA();
        Penalty penB = rangListe.getPenaltyB();

        // in kategorie setzen, falls neue penalty
        if (spiel.getGruppe().getKategorie().getPenaltyA() == null && penA != null) {
            spiel.getGruppe().getKategorie().setPenaltyA(penA);
        }
        if (spiel.getGruppe().getKategorie().getPenaltyB() == null && penB != null) {
            spiel.getGruppe().getKategorie().setPenaltyA(penB);
        }

        kat = katRepo.save(spiel.getGruppe().getKategorie());
        spiel = repo.findOne(id);

        pruefeUndSetzeFinale(spiel, kat, katName, rangListe);

        uploadKat(kat);

        printer.saveSpiel(spiel);

    }

    private void pruefeUndSetzeFinale(Spiel spiel, Kategorie kat, String katName, RanglisteneintragHistorie rangListe) {
        // pruefen ob gruppenspiel fertig und gruppenspiel fertig, dann finalmannschaften eintragen
        if (rangListe.isFertigGespielt() && spiel.getTyp() == SpielEnum.GRUPPE) {

            if (spiel.getGruppe().getKategorie().getGrosserFinal().getMannschaftA() != null) {
                LOG.info("achtung grosser finale, mannschaft wurde bereits zugeordnet");
                return;
            }

            List<Mannschaft> gross = new ArrayList<Mannschaft>();
            List<Mannschaft> klein = new ArrayList<Mannschaft>();

            // nur 3
            if (kat.hasVorUndRueckrunde()) {
                RanglisteneintragHistorie rl = map.get(katName);
                gross.add(rl.getZeilen().get(0).getMannschaft());
                gross.add(rl.getZeilen().get(1).getMannschaft());
            } else

                // normal
                if (!kat.hasVorUndRueckrunde() && !kat.has2Groups()) {
                    RanglisteneintragHistorie rl = map.get(katName);
                    gross.add(rl.getZeilen().get(0).getMannschaft());
                    gross.add(rl.getZeilen().get(1).getMannschaft());

                    klein.add(rl.getZeilen().get(2).getMannschaft());
                    klein.add(rl.getZeilen().get(3).getMannschaft());
                } else

                    // 2 gruppen
                    if (!kat.hasVorUndRueckrunde() && kat.has2Groups()) {
                        RanglisteneintragHistorie rla = map.get(katName + "_A");
                        RanglisteneintragHistorie rlb = map.get(katName + "_B");

                        gross.add(rla.getZeilen().get(0).getMannschaft());
                        gross.add(rlb.getZeilen().get(0).getMannschaft());

                        klein.add(rla.getZeilen().get(1).getMannschaft());
                        klein.add(rlb.getZeilen().get(1).getMannschaft());
                    }

            // die richtige reihenfolge
            Collections.sort(klein, new MannschaftsNamenComperator());
            Collections.sort(gross, new MannschaftsNamenComperator());

            kat.getGrosserFinal().setMannschaftA(gross.get(0));
            kat.getGrosserFinal().setMannschaftB(gross.get(1));

            if (!kat.hasVorUndRueckrunde()) {
                kat.getKleineFinal().setMannschaftA(klein.get(0));
                kat.getKleineFinal().setMannschaftB(klein.get(1));
            }

            this.katRepo.save(kat);
        }
    }

    @Async
    public void uploadAllKat() {
        for (Kategorie kat : this.katRepo.findAll()) {
            uploadKat(kat);
        }
    }

    private void uploadKat(Kategorie kat) {
        // hochladen der index seite
        this.ftpPublisher.addPage("index.html", this.historieGenerator.generatePageIndex());

        // hochladen der kategorie
        String page = this.historieGenerator.generatePageKategorie(kat);
        this.ftpPublisher.addPage(kat.getName() + ".html".toLowerCase(), page);

        // hochladen der resultate
        this.ftpPublisher.addPage("rangliste.html", this.rangliste.printOutGere(map.values(), true));
    }

    @PostConstruct
    public void initialisieren() {

        // neuberechnen der historien nach dem neustart
        map = new HashMap<String, RanglisteneintragHistorie>();

        List<Spiel> spiele = this.repo.findGruppenSpielAsc();

        spiele.addAll(this.repo.findFinalSpielAsc());

        for (Spiel spiel : spiele) {
            if (spiel.isFertigBestaetigt()) {
                this.signalFertigesSpiel(spiel.getId());
            }
        }

        // hochladen der index seite
        this.ftpPublisher.addPage("index.html", this.historieGenerator.generatePageIndex());
    }

    public void initialisierenKategorie(Kategorie kat) {

        List<Spiel> spiele = this.repo.findGruppenSpielAsc();

        for (Spiel spiel : spiele) {
            if (spiel.isFertigBestaetigt() && spiel.getMannschaftA().getKategorie().getName().equals(kat.getName())) {
                this.signalFertigesSpiel(spiel.getId());
            }
        }
        if (kat.getGrosserFinal().isFertigBestaetigt()) {
            this.signalFertigesSpiel(kat.getGrosserFinal().getId());
        }
        if (kat.getGrosserFinal().isFertigBestaetigt()) {
            this.signalFertigesSpiel(kat.getGrosserFinal().getId());
        }

        // hochladen der index seite
        this.ftpPublisher.addPage("index.html", this.historieGenerator.generatePageIndex());
    }


    public String generateSpieleMatrix() {
        List<Kategorie> kategorien = katRepo.findAll();
        return matrix.generateSpieleTable(kategorien);
    }

    public String generateRanglistenHistorieForKategorieName(String kategorieName) {
        RanglisteneintragHistorie h = this.map.get(kategorieName);
        return historieGenerator.getRangliste(h);
    }

    public String getRangliste() {
        return rangliste.printOutGere(map.values(), false);
    }

    @Deprecated
    public String generateRanglistenHistorie(final RanglisteneintragHistorie ranglistenHistorie) {
        return historieGenerator.getRangliste(ranglistenHistorie);
    }

    @Deprecated
    public Collection<RanglisteneintragHistorie> getAllHystorien() {
        return this.map.values();
    }

    public Set<String> getKeys() {
        return this.map.keySet();
    }

}
