/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.business.controller.resultate.ResultateVerarbeiter;
import com.googlecode.madschuelerturnier.business.vorbereitung.helper.SpielzeilenValidator;
import com.googlecode.madschuelerturnier.business.zeit.Zeitgeber;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Penalty;
import com.googlecode.madschuelerturnier.model.comperators.KategorieNameComperator;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.model.enums.SpielTageszeit;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component(value = "business")
public class BusinessImpl implements Business {

    private static final Logger LOG = Logger.getLogger(BusinessImpl.class);

    private static final int MITTAG = 12;
    private static final int SEC_PRO_MINUTE = 60;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private KategorieRepository kategorieRepo;

    @Autowired
    private PenaltyRepository penaltyRepo;

    @Autowired
    private SpielZeilenRepository spielzeilenRepo;

    @Autowired
    private SpielEinstellungenRepository spielEinstellungenRepo;

    @Autowired
    private SpielzeilenValidator val;

    @Autowired
    private ResultateVerarbeiter verarbeiter;

    @Autowired
    private Zeitgeber zeitgeber;

    private SpielEinstellungen einstellungen;

    public BusinessImpl() {

    }

    @PostConstruct //NOSONAR
    private void init() {
        einstellungen = getSpielEinstellungen();
        if (einstellungen != null && einstellungen.isStartJetzt()) {
            this.startClock();
        }

        // Zeitgeber initialisieren

        if (isDBInitialized()) {
            zeitgeber.sendPuls();
        } else {
            LOG.info("sende keinen puls, da db noch nicht initialisiert ist [01]");
        }
    }


    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.madschuelerturnier.business.sdfdf#getSchulhausListe(java .lang.String)
     */
    public List<String> getSchulhausListe(final String query) {
        final Set<String> strings = new HashSet<String>();

        final List<Mannschaft> mannschaften = getMannschaften();
        for (final Mannschaft mannschaft : mannschaften) {
            if (mannschaft.getSchulhaus().contains(query)) {
                strings.add(mannschaft.getSchulhaus());
            }
        }
        final List<String> lStr = new ArrayList<String>();
        for (final String str : strings) {
            lStr.add(str);
        }

        return lStr;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.madschuelerturnier.business.sdfdf#getPersonenListe(java .lang.String)
     */
    public List<String> getPersonenListe(final String query) {
        final Set<String> strings = new HashSet<String>();

        final List<Mannschaft> mannschaften = getMannschaften();
        for (final Mannschaft mannschaft : mannschaften) {
            if (mannschaft.getBegleitpersonName().contains(query)) {
                strings.add(mannschaft.getBegleitpersonName());
            }
            if (mannschaft.getCaptainName().contains(query)) {
                strings.add(mannschaft.getCaptainName());
            }
        }
        final List<String> lStr = new ArrayList<String>();
        for (final String str : strings) {
            lStr.add(str);
        }

        return lStr;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.madschuelerturnier.business.sdfdf#getMannschaften()
     */
    public List<Mannschaft> getMannschaften() {

        final Iterable<Mannschaft> temp = this.mannschaftRepo.findAll();
        final List<Mannschaft> liste = new ArrayList<Mannschaft>();
        for (final Mannschaft mannschaft : temp) {
            liste.add(mannschaft);
        }
        return liste;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.madschuelerturnier.business.sdfdf#getKategorien()
     */
    public List<Kategorie> getKategorien() {
        final Iterable<Kategorie> temp = this.kategorieRepo.findAll();
        final List<Kategorie> liste = new ArrayList<Kategorie>();
        for (final Kategorie mannschaft : temp) {
            liste.add(mannschaft);
        }
        return liste;
    }


    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.madschuelerturnier.business.sdfdf#getSpielEinstellungen()
     */
    public SpielEinstellungen getSpielEinstellungen() {

        // einstellungen bereits im cache
        if (this.einstellungen != null) {

            if (verarbeiter.isFertig()) {
                einstellungen.setPhase(SpielPhasenEnum.G_ABGESCHLOSSEN);
                this.saveEinstellungen(einstellungen);
            }
            return einstellungen;
        }

        // noch nicht aus der db geladen, laden
        if (this.spielEinstellungenRepo.count() > 1) {
            einstellungen = this.spielEinstellungenRepo.findAll().iterator().next();
            return einstellungen;
        }

        // noch nicht initialisiert
        return null;

    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.madschuelerturnier.business.sdfdf#saveEinstellungen(com .googlecode.madschuelerturnier.model.helper.SpielEinstellungen)
     */
    public SpielEinstellungen saveEinstellungen(SpielEinstellungen einstellungenNeu) {

        if (verarbeiter.isFertig()) {
            einstellungen.setPhase(SpielPhasenEnum.G_ABGESCHLOSSEN);
        }

        // falls keine aenderung wird einstellung zurueckgegeben
        if (this.einstellungen != null && einstellungenNeu.equals(this.einstellungen)) {
            return einstellungenNeu;
        }

        // spieldatum auf 0 Uhr zuruecksetzen
        DateTime time = new DateTime(einstellungenNeu.getStarttag());
        final int millis = time.getMillisOfDay();
        time = time.minusMillis(millis);
        einstellungenNeu.setStarttag(new Date(time.getMillis()));
        this.einstellungen = this.spielEinstellungenRepo.save(einstellungenNeu);
        return this.einstellungen;
    }


    public List<Kategorie> getKategorienMList() {
        final List<Kategorie> list = this.kategorieRepo.getKategorienMList();
        Collections.sort(list, new KategorieNameComperator());
        return list;
    }

    public List<Kategorie> getKategorienKList() {
        final List<Kategorie> list = this.kategorieRepo.getKategorienKList();
        Collections.sort(list, new KategorieNameComperator());
        return list;
    }

    public void toggleSpielwunschOnKategorie(final Long id) {
        final Kategorie k = this.kategorieRepo.findOne(id);
        final SpielTageszeit wunsch = k.getSpielwunsch();
        String spielwunsch = null;
        if (wunsch.equals(SpielTageszeit.EGAL)) {
            k.setSpielwunsch(SpielTageszeit.SAMSTAGMORGEN);
            spielwunsch = "morgen";
        } else if (wunsch.equals(SpielTageszeit.SAMSTAGMORGEN)) {
            k.setSpielwunsch(SpielTageszeit.SAMMSTAGNACHMITTAG);
            spielwunsch = "nachmittag";
        } else if (wunsch.equals(SpielTageszeit.SAMMSTAGNACHMITTAG)) {
            k.setSpielwunsch(SpielTageszeit.SONNTAGMORGEN);
            spielwunsch = "sonntag";
        }
        if (wunsch.equals(SpielTageszeit.SONNTAGMORGEN)) {
            k.setSpielwunsch(SpielTageszeit.EGAL);
            spielwunsch = "";
        }

        // nachfuehren der spielwunschhints auf den mannschaften der kategorie
        for (Mannschaft m : k.getMannschaften()) {
            m.setSpielWunschHint(spielwunsch);
            this.mannschaftRepo.save(m);
        }
        this.kategorieRepo.save(k);
    }

    public void manuelleZuordnungDurchziehen(final String mannschaftName, final String zielKategorieKey) {

        List<Kategorie> katListe;

        if (mannschaftName.toLowerCase().contains("m")) {
            katListe = this.getKategorienMList();
        } else {
            katListe = this.getKategorienKList();
        }

        Kategorie quelle = null;
        Kategorie ziel = null;

        Collections.sort(katListe, new KategorieNameComperator());

        Mannschaft verschieben = null;

        for (int i = 0; i < katListe.size(); i++) {
            final List<Mannschaft> mannschaften = katListe.get(i).getMannschaften();
            for (final Mannschaft mannschaft : mannschaften) {
                if (mannschaft.getName().equals(mannschaftName)) {
                    verschieben = mannschaft;
                    quelle = katListe.get(i);

                    if (zielKategorieKey.equals("+")) {
                        ziel = katListe.get(i + 1);
                    } else {
                        ziel = katListe.get(i - 1);
                    }

                }
            }
        }

        if (quelle == null) {
            BusinessImpl.LOG.fatal("!!! bei mannschaftszuordnungs korrektur quelle nicht gefunden: " + mannschaftName);
        }

        quelle.getGruppeA().getMannschaften().remove(verschieben);
        if (verschieben != null) {
            verschieben.setGruppe(ziel.getGruppeA());
        }
        verschieben = this.mannschaftRepo.save(verschieben);
        if (ziel != null) {
            ziel.getGruppeA().getMannschaften().add(verschieben);
        }

        quelle = this.kategorieRepo.save(quelle);
        this.kategorieRepo.save(ziel);

        if ((quelle.getGruppeA().getMannschaften() == null) || (quelle.getGruppeA().getMannschaften().size() < 1)) {
            quelle.getGruppeA().setKategorie(null);
            this.kategorieRepo.delete(quelle);
        }

    }

    public List<SpielZeile> getSpielzeilen(final boolean sonntag) {

        List<SpielZeile> ret;
        if (!sonntag) {
            ret = this.spielzeilenRepo.findSpieleSammstag();

        } else {
            ret = this.spielzeilenRepo.findSpieleSonntag();
        }

        SpielZeile vorher = null;
        for (final SpielZeile spielZeile : ret) {
            this.val.validateSpielZeilen(vorher, spielZeile);
            vorher = spielZeile;
        }

        return ret;

    }

    public void initZeilen(final boolean sonntag) {

        DateTime start;

        List<SpielZeile> zeilen;

        BusinessImpl.LOG.info("date: starttag -->" + this.getSpielEinstellungen().getStarttag());
        final DateTime start2 = new DateTime(this.getSpielEinstellungen().getStarttag(), DateTimeZone.forID("Europe/Zurich"));
        BusinessImpl.LOG.info("date: starttag Europe/Zurich -->" + start2);

        if (!sonntag) {
            start = new DateTime(start2);

            zeilen = createZeilen(start, false);
        } else {
            start = new DateTime(start2);
            start = start.plusDays(1);
            zeilen = createZeilen(start, true);
        }

        BusinessImpl.LOG.info("-->" + zeilen);

        this.spielzeilenRepo.save(zeilen);

    }

    private List<SpielZeile> createZeilen(DateTime startIn, final boolean sonntag) {
        DateTime start = startIn;

        final int millis = start.getMillisOfDay();

        start = start.minusMillis(millis);

        start = start.plusHours(8);

        final DateTime end = start.plusHours(11);

        final List<SpielZeile> zeilen = new ArrayList<SpielZeile>();
        while (start.isBefore(end.getMillis())) {
            final SpielZeile zeile = new SpielZeile();

            if (start.getHourOfDay() == 8) {
                zeile.setPause(true);
            }

            if (start.getHourOfDay() == MITTAG) {
                zeile.setPause(true);
            }

            if ((start.getHourOfDay() > MITTAG) && sonntag) {
                zeile.setFinale(true);
            }

            // wunsch enum wird gesetzt um spaeter die kategorie gegenpruefen zu koennen
            if (sonntag && (start.getHourOfDay() <= MITTAG)) {
                zeile.setSpieltageszeit(SpielTageszeit.SONNTAGMORGEN);
            }
            if (!sonntag && (start.getHourOfDay() < MITTAG)) {
                zeile.setSpieltageszeit(SpielTageszeit.SAMSTAGMORGEN);
            }
            if (!sonntag && (start.getHourOfDay() > MITTAG)) {
                zeile.setSpieltageszeit(SpielTageszeit.SAMMSTAGNACHMITTAG);
            }

            zeile.setStart(start.toDate());
            zeilen.add(zeile);

            final DateTimeZone zone = start.getZone();
            BusinessImpl.LOG.info("zone: " + zone + " date: " + start.toDate());

            zeile.setSonntag(sonntag);

            start = start.plusMinutes(this.getSpielEinstellungen().getPause() + this.getSpielEinstellungen().getSpiellaenge());
        }
        return zeilen;
    }

    public void startClock() {

        if (this.getSpielEinstellungen().getPhase() == SpielPhasenEnum.E_SPIELBEREIT || this.getSpielEinstellungen().getPhase() == SpielPhasenEnum.F_SPIELEN) {
            if (this.getSpielEinstellungen().isStartJetzt()) {
                this.zeitgeber.startClock(null, null, 1);
            } else {
                this.zeitgeber.startClock(new DateTime(this.getSpielEinstellungen().getStart()), new DateTime(this.getSpielEinstellungen().getStart()), this.getSpielEinstellungen().getVerschnellerungsFaktor());
            }
            final SpielEinstellungen einst = this.getSpielEinstellungen();
            einst.setPhase(SpielPhasenEnum.F_SPIELEN);
            this.saveEinstellungen(einst);

        } else {
            BusinessImpl.LOG.error("starten nicht moeglich, falsch phase: " + this.getSpielEinstellungen().getPhase());
        }

        this.zeitgeber.startGame(0, "nach neustart der clock");

    }

    public void stopClock() {

        this.zeitgeber.stopGame("stopSpiel wurde aufgerufen");

        final SpielEinstellungen einst = this.getSpielEinstellungen();
        einst.setPhase(SpielPhasenEnum.E_SPIELBEREIT);
        this.zeitgeber.stopClock();
        this.saveEinstellungen(einst);
        BusinessImpl.LOG.info("spiel angehalten ");

    }


    public void spielzeitEinholen(int seconds) {
        int effVerspaetung = this.zeitgeber.getVerspaetung();
        effVerspaetung = Math.abs(effVerspaetung);
        if (effVerspaetung < 1) {
            return;
        }

        if (effVerspaetung >= seconds) {
            this.zeitgeber.startGame(seconds, "einholung: " + seconds);
        } else {
            this.zeitgeber.startGame(effVerspaetung, "einholung effektiv: " + effVerspaetung);
        }

    }

    public String spielzeitVerspaetung() {
        int sekunden = Math.abs(this.zeitgeber.getVerspaetung());

        int rest = sekunden % SEC_PRO_MINUTE;
        int minuten = sekunden / SEC_PRO_MINUTE;

        DecimalFormat df2 = new DecimalFormat("00");

        return df2.format(minuten) + ":" + df2.format(rest);
    }

    public void resumeSpiel() {

        this.zeitgeber.startGame(0, "resumeSpiel wurde aufgerufen");

        final SpielEinstellungen einst = this.getSpielEinstellungen();
        einst.setPhase(SpielPhasenEnum.F_SPIELEN);
        this.saveEinstellungen(einst);
        BusinessImpl.LOG.info("spiel resumed ");

    }

    public List<Penalty> anstehendePenalty() {
        List<Penalty> alle = penaltyRepo.findAll();
        List<Penalty> result = new ArrayList<Penalty>();
        for (Penalty p : alle) {
            if (!p.isBestaetigt() && !p.isGespielt()) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Penalty> gespieltePenalty() {
        List<Penalty> alle = penaltyRepo.findAll();
        List<Penalty> result = new ArrayList<Penalty>();
        for (Penalty p : alle) {
            if (!p.isBestaetigt() && p.isGespielt()) {
                result.add(p);
                // sofort zurueckgeben, weil immer nur ein eintrag in der liste erscheinen soll
                return result;
            }
        }
        return result;
    }

    public void penaltyEintragen(List<Penalty> list) {

        for (Penalty p : list) {
            if (p.getReihenfolge() != null && !p.getReihenfolge().isEmpty()) {

                if (p.isBestaetigt() && p.isGespielt()) {
                    continue;
                }

                if (p.getReihenfolge().equals(Penalty.getLeer())) {
                    continue;
                }

                p.setGespielt(true);
                p.setBestaetigt(true);
                p = this.penaltyRepo.save(p);

                this.verarbeiter.signalPenalty(p);

            }
        }
    }

    public List<Penalty> eingetragenePenalty() {
        List<Penalty> alle = penaltyRepo.findAll();
        List<Penalty> result = new ArrayList<Penalty>();
        for (Penalty p : alle) {
            if (p.isBestaetigt() && p.isGespielt()) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public boolean isDBInitialized() {
        return this.getSpielEinstellungen() != null;
    }

    @Override
    public void initializeDB() {
        if (this.spielEinstellungenRepo.count() > 0) {
            BusinessImpl.LOG.fatal("achtung versuch spiel einstellungen zu initialisieren obwohl bereits in der db vorhanden ");
            this.einstellungen = spielEinstellungenRepo.findAll().get(0);
        } else {
            SpielEinstellungen eins = new SpielEinstellungen();
            this.einstellungen = this.spielEinstellungenRepo.save(eins);
        }

    }
}