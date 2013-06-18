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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    private boolean init = false;

    boolean uploadAllKat = false;

    private Map<String, Boolean> beendet = new HashMap<String, Boolean>();

    private Queue<Long> spielQueue = new ConcurrentLinkedQueue<Long>();

    private Queue<Penalty> penaltyQueue = new ConcurrentLinkedQueue<Penalty>();

    private static final Logger LOG = Logger.getLogger(ResultateVerarbeiter.class);

    private Map<String, RanglisteneintragHistorie> map = new HashMap<String, RanglisteneintragHistorie>();

    public void signalPenalty(Penalty p) {
        penaltyQueue.offer(p);
    }

    public void verarbeitePenalty() {
        Penalty p = null;
        try {

            if (penaltyQueue.size() > 0) {
                p = penaltyQueue.remove();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        if (p == null) {
            return;
        }

        LOG.info("verarbeite penalty: " + p);

        this.neuberechnenDerKategorie(p.getKategorie().getKategorie());
    }


    public boolean isFertig() {

        if (this.spielQueue.size() > 0) {
            return false;
        }

        if (this.penaltyQueue.size() > 0) {
            return false;
        }

        beendet.remove("invalide");
        if (beendet.size() < 1) {
            return false;
        }

        for (boolean ok : beendet.values()) {
            if (!ok) {
                return false;
            }
        }

        return true;
    }

    public void initFertigMap() {
        if (beendet.size() > 0) {
            return;
        }
        List<Kategorie> katList = this.katRepo.findAll();

        for (Kategorie kat : katList) {
            this.beendet.put(kat.getName(), false);
        }
    }


    public void signalFertigesSpiel(Long id) {
        spielQueue.offer(id);
        LOG.info("spiel signalisiert: " + id + " queuesize: " + spielQueue.size());
    }

    public int getQueueSize() {
        int count = spielQueue.size();
        count = count + penaltyQueue.size();
        if (uploadAllKat) {
            count = count + 1;
        }
        return count;
    }

    @Scheduled(fixedRate = 1000 * 15)
    private void verarbeiten() {

        if (!init) {
            initialisieren();
            init = true;
        }

        Long id = null;
        try {
            id = spielQueue.remove();
        } catch (NoSuchElementException e) {

        }

        while (id != null) {


            // map mit den fertig flags initialisieren
            initFertigMap();

            verarbeitePenalty();

            verarbeiteUploadAllKat();


            verarbeiteSpiel(id);
            try {
                id = spielQueue.remove();
            } catch (NoSuchElementException e) {
                id = null;
            }
        }

    }

    private void verarbeiteSpiel(Long id) {

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


        // pruefe ob rangliste kategorie fertig
        if (rangListe.isFertigGespielt()) {
            boolean fertig = false;
            if (kat.getGrosserFinal() != null && kat.getGrosserFinal().isFertigBestaetigt()) {
                fertig = true;
            }

            if (kat.getKleineFinal() != null && !kat.getKleineFinal().isFertigBestaetigt()) {
                fertig = false;
            }
            this.beendet.put(kat.getName(), fertig);
        }

    }

    private void pruefeUndSetzeFinale(Spiel spiel, Kategorie kat, String katName, RanglisteneintragHistorie rangListe) {
        // pruefen ob gruppenspiel fertig und gruppenspiel fertig, dann finalmannschaften eintragen
        if (rangListe.isFertigGespielt() && spiel.getTyp() == SpielEnum.GRUPPE) {

            // 18.06.13 auskommentiert, soll immer gesetzt werden wegen korrekturen
            //if (spiel.getGruppe().getKategorie().getGrosserFinal().getMannschaftA() != null) {
            //    LOG.info("achtung grosser finale, mannschaft wurde bereits zugeordnet");
            //    return;
            //}

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


    public void uploadAllKat() {
        uploadAllKat = true;
    }

    private void verarbeiteUploadAllKat() {

        if (!uploadAllKat) {
            return;
        }

        LOG.info("ftpPublisher: reconnect");
        this.ftpPublisher.reconnect();
        for (Kategorie kat : this.katRepo.findAll()) {
            uploadKat(kat);
        }
        uploadAllKat = false;
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

    public void neuberechnenDerKategorie(Kategorie kat) {

        String katName = kat.getName();

        map.remove(katName);
        map.remove(katName + "_A");
        map.remove(katName + "_B");

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
