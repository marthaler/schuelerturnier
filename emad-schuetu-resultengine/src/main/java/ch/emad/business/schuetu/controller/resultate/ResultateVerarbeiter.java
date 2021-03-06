/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.controller.resultate;

import ch.emad.business.schuetu.controller.leiter.converter.HTMLConverterRangliste;
import ch.emad.business.schuetu.controller.leiter.converter.HTMLOutConverter;
import ch.emad.business.schuetu.controller.leiter.converter.HTMLSpielMatrixConverter;
import ch.emad.business.schuetu.controller.leiter.converter.ModelConverterRangliste;
import ch.emad.business.schuetu.print.SpielPrintManager;
import ch.emad.business.schuetu.websiteinfo.model.KlassenrangZeile;
import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Penalty;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.comperators.MannschaftsNamenComperator;
import ch.emad.model.schuetu.model.compusw.RanglisteneintragHistorie;
import ch.emad.model.schuetu.model.compusw.RanglisteneintragZeile;
import ch.emad.model.schuetu.model.enums.SpielEnum;
import ch.emad.persistence.schuetu.repository.KategorieRepository;
import ch.emad.persistence.schuetu.repository.SpielEinstellungenRepository2;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class ResultateVerarbeiter {

    private static final int WAITTIME = 1000 * 15;
    private static final Logger LOG = Logger.getLogger(ResultateVerarbeiter.class);
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
    private SpielEinstellungenRepository2 eRepo;
    @Autowired
    private ModelConverterRangliste ranglisteConverter;

    @Autowired
    private SpielPrintManager printer;

    private boolean init = false;
    private boolean uploadAllKat = false;
    private Map<String, Boolean> beendet = new HashMap<String, Boolean>();
    private Queue<Long> spielQueue = new ConcurrentLinkedQueue<Long>();
    private Queue<Penalty> penaltyQueue = new ConcurrentLinkedQueue<Penalty>();
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

    @Scheduled(fixedRate = WAITTIME) //NOSONAR
    private void verarbeiten() {
        try {
            if (!init) {
                initialisieren();
                init = true;
            }

            Long id = null;
            try {
                id = spielQueue.remove();
            } catch (NoSuchElementException e) {
                id = null;
            }

            while (id != null) {

                // map mit den fertig flags initialisieren
                initFertigMap();

                verarbeitePenalty();

                verarbeiteSpiel(id);
                try {
                    id = spielQueue.remove();
                } catch (NoSuchElementException e) {
                    id = null;
                }
            }
        } catch (Exception e) {
            LOG.fatal(e.getMessage(), e);
        }
    }

    private void verarbeiteSpiel(Long id) {

        LOG.info("verarbeite fertiges spiel: " + id);
        Spiel spiel = repo.findOne(id);

        Kategorie kat;
        String katName = "";

        try {
            kat = spiel.getMannschaftA().getKategorie();
            katName = spiel.getMannschaftA().getKategorie().getName();
        } catch (Exception e) {
            LOG.fatal(spiel.getTyp() + ":" + spiel.getIdString());
            LOG.fatal(e.getMessage(), e);
        }

        RanglisteneintragHistorie rangListe = null;

        // hat a und b  = mehr als 7 mannschaften
        try {
            if (spiel.getTyp() == SpielEnum.GRUPPE && !spiel.getGruppe().getKategorie().hasVorUndRueckrunde() && spiel.getGruppe().getKategorie().getGruppeB().getMannschaften().size() > 0) {

                aIstInGruppeA(spiel, katName);

                aIstInGruppeB(spiel, katName);
            }
        } catch (Exception e) {
            LOG.fatal(e.getMessage(), e);
        }

        rangListe = normalerEintrag(spiel, katName);

        kat = pruefePenalty(spiel, rangListe);


        spiel = repo.findOne(id);

        pruefeUndSetzeFinale(spiel, kat, katName, rangListe);

        printer.saveSpiel(spiel);

        pruefeEnde(kat, rangListe);

    }

    private Kategorie pruefePenalty(Spiel spiel, RanglisteneintragHistorie rangListe) {
        // pruefen ob penalty noetig !!!
        Kategorie kat;

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
        return kat;
    }

    private void pruefeEnde(Kategorie kat, RanglisteneintragHistorie rangListe) {
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

    private RanglisteneintragHistorie normalerEintrag(Spiel spiel, String katName) {
        // normalen eintrag
        RanglisteneintragHistorie rangListe;

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
        return rangListe;
    }

    private void aIstInGruppeA(Spiel spiel, String katName) {
        // mannschaft a des spiels ist in gruppe a
        RanglisteneintragHistorie rangListe;
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
    }

    private void aIstInGruppeB(Spiel spiel, String katName) {
        // mannschaft a des spiels ist in gruppe b
        RanglisteneintragHistorie rangListe;
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

    private void pruefeUndSetzeFinale(Spiel spiel, Kategorie kat, String katName, RanglisteneintragHistorie rangListe) {
        // pruefen ob gruppenspiel fertig und gruppenspiel fertig, dann finalmannschaften eintragen
        if (rangListe.isFertigGespielt() && spiel.getTyp() == SpielEnum.GRUPPE) {

            List<Mannschaft> gross = new ArrayList<Mannschaft>();
            List<Mannschaft> klein = new ArrayList<Mannschaft>();

            // nur 3
            if (kat.hasVorUndRueckrunde()) {
                RanglisteneintragHistorie rl = map.get(katName);
                gross.add(rl.getZeilen().get(0).getMannschaft());
                gross.add(rl.getZeilen().get(1).getMannschaft());
                // 8.2.2014: else if, damit auch die 3 er richtig gespeichert werden
            } else if (kat.isMixedKlassen() && eRepo.getEinstellungen().isBehandleFinaleProKlassebeiZusammengefuehrten()) {
                finaleSuchenNachKlasse(kat, gross, klein);
            } else {
                finaleSuchenNormal(kat, gross, klein);
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

    private void finaleSuchenNormal(Kategorie kat, List<Mannschaft> gross, List<Mannschaft> klein) {
        // normal
        if (!kat.has2Groups()) {
            RanglisteneintragHistorie rl = map.get(kat.getName());
            gross.add(rl.getZeilen().get(0).getMannschaft());
            gross.add(rl.getZeilen().get(1).getMannschaft());

            klein.add(rl.getZeilen().get(2).getMannschaft());
            klein.add(rl.getZeilen().get(3).getMannschaft());

        } else {
            // 2 gruppen
            RanglisteneintragHistorie rla = map.get(kat.getName() + "_A");
            RanglisteneintragHistorie rlb = map.get(kat.getName() + "_B");

            gross.add(rla.getZeilen().get(0).getMannschaft());
            gross.add(rlb.getZeilen().get(0).getMannschaft());

            klein.add(rla.getZeilen().get(1).getMannschaft());
            klein.add(rlb.getZeilen().get(1).getMannschaft());

        }
    }

    private void finaleSuchenNachKlasse(Kategorie kat, List<Mannschaft> gross, List<Mannschaft> klein) {

        // feststellen der beiden klassen
        int klasseTief = 0;
        int klasseHoch = 0;

        List<Mannschaft> listeTief = new ArrayList<Mannschaft>();
        List<Mannschaft> listeHoch = new ArrayList<Mannschaft>();

        RanglisteneintragHistorie rl = map.get(kat.getName());
        klasseTief = rl.getZeilen().get(0).getMannschaft().getKlasse();
        for (RanglisteneintragZeile temp : rl.getZeilen()) {
            if (klasseTief != temp.getMannschaft().getKlasse()) {
                klasseHoch = temp.getMannschaft().getKlasse();
                listeHoch.add(temp.getMannschaft());
            } else {
                listeTief.add(temp.getMannschaft());
            }
        }
        if (klasseTief > klasseHoch) {
            int temp = klasseTief;
            List<Mannschaft> listeTemp = listeTief;
            klasseTief = klasseHoch;
            listeTief = listeHoch;
            klasseHoch = temp;
            listeHoch = listeTemp;
        }

        LOG.info("klassen gefunden: tief " + klasseTief + " und hoch " + klasseHoch);

        gross.add(listeTief.get(0));
        gross.add(listeTief.get(1));

        klein.add(listeHoch.get(0));
        klein.add(listeHoch.get(1));

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

    public List<KlassenrangZeile> getRanglisteModel() {

        List<RanglisteneintragHistorie> ranglisten = new ArrayList<RanglisteneintragHistorie>();

        // *_A und *_B wegfiltern, damit nur die echte Historien verwendet wird
        Set<String> set = map.keySet();

        for(String key: set){
            if(!key.contains("_A") && !key.contains("_B")){
                ranglisten.add(map.get(key));
            }
        }

        return this.ranglisteConverter.convertKlassenrangZeile(ranglisten);
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
