/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.spieldurchfuehrung;

import ch.emad.business.schuetu.Business;
import ch.emad.business.schuetu.controller.resultate.ResultateVerarbeiter;
import ch.emad.business.schuetu.print.PrintAgent;
import ch.emad.business.schuetu.print.SpielPrintManager;
import ch.emad.business.schuetu.websiteinfo.WebsiteInfoService;
import ch.emad.business.schuetu.zeit.Countdown;
import ch.emad.business.schuetu.zeit.ZeitPuls;
import ch.emad.business.schuetu.zeit.Zeitgeber;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielZeile;
import ch.emad.model.schuetu.model.enums.SpielEnum;
import ch.emad.model.schuetu.model.enums.SpielPhasenEnum;
import ch.emad.model.schuetu.model.enums.SpielZeilenPhaseEnum;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import ch.emad.persistence.schuetu.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class SpielDurchfuehrung implements ApplicationListener<ZeitPuls> {

    private static final Logger LOG = Logger.getLogger(SpielDurchfuehrung.class);


    private int wartendSize = 3;
    private int minutenZumVorbereiten = 3;

    private String delim = System.getProperty("file.separator");

    @Autowired
    private WebsiteInfoService infoservice;

    @Autowired
    private ResultateVerarbeiter verarbeiter;

    @Autowired
    private SpielZeilenRepository spielzeilenRepo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private Business business;

    @Autowired
    private Zeitgeber zeitgeber;

    @Autowired
    private PrintAgent agent;


    @Autowired
    private SpielPrintManager spielPrinter;

    private boolean endranglistegedruckt = false;

    private Countdown countdown;

    private Countdown countdownToStart;

    private ZeitPuls jetzt;

    private List<String> text = new ArrayList<String>();

    @Autowired
    @Qualifier("durchfuehrungDataDatabase")
    private SpielDurchfuehrungData data;


	private boolean init = false;

    public void onApplicationEvent(final ZeitPuls event) {

        //beim ersten aufruf, initialisierung
        if (!init) {
            init();
            init = true;
        }

        this.jetzt = event;

        if (this.countdown != null) {
            this.countdown.signalTime(event.getSpielZeit());
        }

        if (this.countdownToStart != null) {
            this.countdownToStart.signalTime(event.getSpielZeit());
        }

        if (this.business.getSpielEinstellungen().getPhase().equals(SpielPhasenEnum.F_SPIELEN)) {

            prepare1Wartend();

            prepare2ZumVorbereiten();

            prepare3WartenAufStart();

            prepare4Spielend();

            checkSpielende();

        }

        if (this.business.getSpielEinstellungen().getPhase().equals(SpielPhasenEnum.G_ABGESCHLOSSEN)) {

            checkSpielende();

        }


    }

    /*
     * dient dazu beim ersten aufruf die liste zu fuellen mit den zeilen aus der db
     */
    private synchronized void init() {
        data.getList2ZumVorbereiten().addAll(spielzeilenRepo.findBZurVorbereitung());
        data.getList3Vorbereitet().addAll(spielzeilenRepo.findCVorbereitet());
        data.getList4Spielend().addAll(spielzeilenRepo.findDSpielend());
    }

    private synchronized void prepare1Wartend() {
        // wartende auffuellen

    }

    private boolean pruefeObMannschaftenBeiFinalenSind(SpielZeile spielZeile) {
        // pruefung ob breits Mannschaften eingetragen sind bei den Finalen
        if (spielZeile.getA() != null && spielZeile.getA().getTyp() != SpielEnum.GRUPPE) {
            if (spielZeile.getA() != null) {
                if (spielZeile.getA().getMannschaftA() == null) {
                    return true;
                }
            }
            if (spielZeile.getB() != null) {
                if (spielZeile.getB().getMannschaftA() == null) {
                    return true;
                }
            }
            if (spielZeile.getC() != null) {
                if (spielZeile.getC().getMannschaftA() == null) {
                    return true;
                }
            }

        }
        return false;
    }

    private void prepare2ZumVorbereiten() {

        if (!this.data.getList1Wartend(this.wartendSize).isEmpty()) {
            // zum vorbereiten einstellen
            if ((this.data.getList2ZumVorbereiten().isEmpty())) {

                // pruefung ob nachste zeile bereits zur vorbereitung
                long naechste = this.data.getList1Wartend(wartendSize).get(this.data.getList1Wartend(wartendSize).size() - 1).getStart().getTime() - (60L * minutenZumVorbereiten * 1000);
                long now = jetzt.getSpielZeit().getMillis();

                if (naechste < now) {
                    SpielZeile temp = this.data.getList1Wartend(wartendSize).remove(this.data.getList1Wartend(wartendSize).size() - 1);
                    temp.setPhase(SpielZeilenPhaseEnum.B_ZUR_VORBEREITUNG);
                    temp = spielezeileUpdatenBetreffendZeilenphaseAndSave(temp);
                    this.data.getList2ZumVorbereiten().add(temp);

                    // automatisches vorbereiten
                    if (this.business.getSpielEinstellungen().isAutomatischesVorbereiten()) {
                        this.vorbereitet();
                    }

                }
            }
        }

        // zeit anhalten, falls die spielzeit des zum vorbereitenden abgelaufen ist
        if (stopBecauseZumVorbereiten() && this.zeitgeber.isGameStarted()) {
            this.zeitgeber.stopGame("spielzeit des zu_vorbereitenden ist abgelaufen");
        }

        // zeit wieder starten, falls zum vorbereiten leer ist oder eine zeile mit start in der zukunft hat, falls evnet auf pause ist
        if (jetzt.isUnterbruch() && !stopBecauseWartenAufStart()) {
            if (this.data.getList2ZumVorbereiten().isEmpty() || jetzt.getSpielZeit().isBefore(new DateTime(data.getList2ZumVorbereiten().get(0).getStart()))) {
                this.zeitgeber.startGame(0, "liste mit zu_vorbereiteten ist wieder leer");
            }
        }

    }

    private SpielZeile spielezeileUpdatenBetreffendZeilenphaseAndSave(SpielZeile temp){

        if(temp.getA() != null){
            temp.getA().setSpielZeilenPhase(temp.getPhase());
        }
        if(temp.getB() != null){
            temp.getB().setSpielZeilenPhase(temp.getPhase());
        }
        if(temp.getC() != null){
            temp.getC().setSpielZeilenPhase(temp.getPhase());
        }
        return this.spielzeilenRepo.save(temp);
    }

    private boolean stopBecauseZumVorbereiten() {
        return !this.data.getList2ZumVorbereiten().isEmpty() && jetzt.getSpielZeit().isAfter(new DateTime(data.getList2ZumVorbereiten().get(0).getStart()));
    }

    private boolean stopBecauseWartenAufStart() {
        return !this.data.getList3Vorbereitet().isEmpty() && jetzt.getSpielZeit().isAfter(new DateTime(data.getList3Vorbereitet().get(0).getStart()));
    }

    private void prepare3WartenAufStart() {

        // zeit anhalten, falls die spielzeit des zum vorbereiten abgelaufen ist
        if (stopBecauseWartenAufStart()) {
            this.zeitgeber.stopGame("spielzeit des vorbereitenden ist abgelaufen");
        }

        // zeit wieder starten, falls vorbereiten leer ist oder eine zeile mit start in der zukunft hat, falls evnet auf pause ist
        if (jetzt.isUnterbruch() && !stopBecauseZumVorbereiten()) {
            if (this.data.getList3Vorbereitet().isEmpty() || jetzt.getSpielZeit().isBefore(new DateTime(data.getList3Vorbereitet().get(0).getStart()))) {
                this.zeitgeber.startGame(0, "liste mit vorbereiteten ist wieder leer");
            }
        }
    }

    private void prepare4Spielend() {

        // keine spielenden
        if (this.getList4Spielend().isEmpty()) {
            return;
        }

        // beenden, falls zeit abgelaufen ist
        if (this.countdown != null && this.getCountdown().isFertig()) {
            this.beenden();
        }

    }

    private void checkSpielende() {
        LOG.debug("checkSpielende: start");
        if (!endranglistegedruckt && business.getSpielEinstellungen().getPhase() == SpielPhasenEnum.G_ABGESCHLOSSEN) {

            agent.saveFileToPrint("rangliste", verarbeiter.getRangliste());

            // letzte resultate drucken
            spielPrinter.printPage();

            String jahr = ""+ new DateTime(this.business.getSpielEinstellungen().getStarttag()).getYear();

            this.infoservice.dumpJetzt(jahr);

            endranglistegedruckt = true;
            LOG.debug("checkSpielende: spiel abgeschlossen");
        }

    }

    public void vorbereitet() {
        SpielZeile temp = this.data.getList2ZumVorbereiten().remove(0);
        this.countdownToStart = new Countdown(this.jetzt.getSpielZeit(), new DateTime(temp.getStart()));
        temp.setPhase(SpielZeilenPhaseEnum.C_VORBEREITET);
        temp = spielezeileUpdatenBetreffendZeilenphaseAndSave(temp);
        List<SpielZeile> zeile = this.data.getList3Vorbereitet();
        zeile.add(temp);
        this.data.setList3Vorbereitet(zeile);
        // tts get
        generateText(temp);
    }

    public void spielen() {

        gong();

        this.countdown = new Countdown(this.jetzt.getSpielZeit(), 10 * 60);
        SpielZeile temp = this.data.getList3Vorbereitet().remove(0);
        temp.setPhase(SpielZeilenPhaseEnum.D_SPIELEND);
        if (temp.getA() != null) {
            temp.getA().setAmSpielen(true);
        }
        if (temp.getB() != null) {
            temp.getB().setAmSpielen(true);
        }
        if (temp.getC() != null) {
            temp.getC().setAmSpielen(true);
        }
        temp = spielezeileUpdatenBetreffendZeilenphaseAndSave(temp);
        this.data.getList4Spielend().add(temp);

        // zeit aufholen, falls eingestellt auf automatisch
        if (this.business.getSpielEinstellungen().isAutomatischesAufholen()) {
            this.business.spielzeitEinholen(this.business.getSpielEinstellungen().getAufholzeitInSekunden());
        }
    }

    public synchronized void beenden() {
        SpielZeile temp = this.data.getList4Spielend().remove(0);
        temp.setPhase(SpielZeilenPhaseEnum.E_BEENDET);

        this.data.getList5Beendet().add(temp);

        // einzutragende spiele vorbereiten
        if (temp.getA() != null) {
            Spiel spiel = temp.getA();
            spiel.setAmSpielen(false);
            spiel.setFertigGespielt(true);
            spielRepo.save(spiel);
        }
        if (temp.getB() != null) {
            Spiel spiel = temp.getB();
            spiel.setAmSpielen(false);
            spiel.setFertigGespielt(true);
            spielRepo.save(spiel);
        }
        if (temp.getC() != null) {
            Spiel spiel = temp.getC();
            spiel.setAmSpielen(false);
            spiel.setFertigGespielt(true);
            spielRepo.save(spiel);
        }
        temp = spielezeileUpdatenBetreffendZeilenphaseAndSave(temp);
        gong();
    }

    public synchronized List<SpielZeile> getList1Wartend() {
        return this.data.getList1Wartend(this.wartendSize);
    }

    public String getText() {
        if (text.size() > 0) {
            return this.text.remove(0);
        }
        return null;
    }

    private void gong() {
        // bell setzen
        if (business.getSpielEinstellungen().isGongEinschalten()) {
            text.add("resources" + delim + "static" + delim + "sound" + delim + "bell01.mov");
        }
    }

    public int countText() {
        if (text.size() > 0) {
            return this.text.get(0).length();
        }
        return 0;
    }

    public String getWait() {

        // 15 Sekunden default
        int millis = 15000;

        // countdown
        if (countdown != null && !countdown.isFertig()) {
            if (millis > (countdown.getSecondsPlus2() * 1000)) {
                millis = (countdown.getSecondsPlus2() * 1000);
            }
        }

        // countdownToStart
        if (countdownToStart != null && !countdownToStart.isFertig()) {
            if (millis > (countdownToStart.getSecondsPlus2() * 1000)) {
                millis = (countdownToStart.getSecondsPlus2() * 1000);
            }
        }

        // ansagetext fuer platze
        if (isTextAvailable() && this.text.get(0).contains(".mp3")) {
            if (millis < 25000) {
                millis = 25000;
            }
        }

        // bell
        if (isTextAvailable() && this.text.get(0).contains("bell")) {
            millis = 6000;
        }

        if (millis < 5000) {
            millis = 5000;
        }

        return "" + millis;
    }

    public boolean isTextAvailable() {
        if (this.text.size() > 0) {
            return true;
        }
        return false;
    }

    public void generateText(SpielZeile zeile) {
        if (this.business.getSpielEinstellungen().isAutomatischesAnsagen()) {
            text.add("/app/tts/spielansage/id/" + zeile.getId());
        }
    }

    // getter & setter

    public boolean getReadyToVorbereiten() {
        return this.data.getList3Vorbereitet().isEmpty();
    }

    public boolean getReadyToSpielen() {
        return this.data.getList4Spielend().isEmpty();
    }

    public List<SpielZeile> getList2ZumVorbereiten() {
        return this.data.getList2ZumVorbereiten();
    }

    public List<SpielZeile> getList3Vorbereitet() {
        return this.data.getList3Vorbereitet();
    }

    public List<SpielZeile> getList4Spielend() {
        return this.data.getList4Spielend();
    }

    public List<SpielZeile> getList5Beendet() {
        return this.data.getList5Beendet();
    }

    public Countdown getCountdown() {
        return this.countdown;
    }

    public Countdown getCountdownToStart() {
        return countdownToStart;
    }

    public void fertigesSpiel(Spiel spiel) {
        this.verarbeiter.signalFertigesSpiel(spiel.getId());
    }

    public int getWartendSize() {
        return wartendSize;
    }

    public void setWartendSize(int wartendSize) {
        this.wartendSize = wartendSize;
    }

    public int getMinutenZumVorbereiten() {
        return minutenZumVorbereiten;
    }

    public void setMinutenZumVorbereiten(int minutenZumVorbereiten) {
        this.minutenZumVorbereiten = minutenZumVorbereiten;
    }

}
