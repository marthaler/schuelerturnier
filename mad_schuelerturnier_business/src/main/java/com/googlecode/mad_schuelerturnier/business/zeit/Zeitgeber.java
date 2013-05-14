/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.zeit;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Zeitgeber implements ApplicationEventPublisherAware {

    private static final Logger LOG = Logger.getLogger(Zeitgeber.class);

    private ApplicationEventPublisher applicationEventPublisher = null;

    private long zeitJetzt = System.currentTimeMillis();

    private long abweichungZuSpielzeit;

    private int verschnellerungsfaktor = 1;

    private boolean clockRunning = false;

    private boolean gameRunning = false;


    public Zeitgeber() {

    }

    @PostConstruct
    private void init(){
        stopClock();
        sendPuls();
    }

    //
    @Scheduled(fixedRate = 1000)
    public void run() {

        // falls nicht "started" weiter

        if(!clockRunning){
              return;
        }

        if (!gameRunning) {
            this.abweichungZuSpielzeit = this.abweichungZuSpielzeit - (this.verschnellerungsfaktor * 1000);
        }

        sendPuls();
    }



    public synchronized void stopGame(String grund) {

        if (!isGameStarted()) {
            Zeitgeber.LOG.info("zeitgeber: stopGame() -> ohne effekt, weil bereits gestoppt:" + grund);
        } else{
            this.stopGame();
            Zeitgeber.LOG.info("zeitgeber: stopGame() -> mit Grund: " + grund);
            // eine einheit zurueck, weil stoppevent immer eine einheit zu spaet kommt
            abweichungZuSpielzeit = abweichungZuSpielzeit + (this.verschnellerungsfaktor * 1000);
            Zeitgeber.LOG.info("zeitgeber: pause: " + this.abweichungZuSpielzeit / 1000 + " sekunden abweichung");

        }

    }




    public synchronized void startGame(int seconds, String grund) {
        Zeitgeber.LOG.info("zeitgeber: startGame() -> aufholung:" + grund + " -> " + seconds);
        this.abweichungZuSpielzeit = this.abweichungZuSpielzeit + seconds * 1000;
        Zeitgeber.LOG.info("zeitgeber: startGame() -> aufholung von " + seconds + " sekunden = abweichung: " + this.abweichungZuSpielzeit / 1000+ " sekunden");
        startGame();
    }

    public synchronized void startClock(final DateTime richtigeZeit, final DateTime spielzeit, final Integer verschnellerung) {

        if(verschnellerung != null){
            this.verschnellerungsfaktor = verschnellerung;
        } else {
            this.verschnellerungsfaktor = 1;
        }

        if (richtigeZeit != null) {
            zeitJetzt = richtigeZeit.getMillis();
        } else {
            zeitJetzt = new DateTime().getMillis();
        }

        if (spielzeit != null) {
            abweichungZuSpielzeit = spielzeit.getMillis() - this.zeitJetzt;
        } else {
            abweichungZuSpielzeit = 0;
        }

        this.startClock();
        this.startGame();

        Zeitgeber.LOG.info("zeitgeber: startClock() gestartet mit abweichung zur spielzeit: " + this.abweichungZuSpielzeit / 1000 + " sekunden");

    }


    // start & stop game
    public boolean isGameStarted() {
        return gameRunning;
    }

    public void stopGame(){
        gameRunning = false;
    }

    private void startGame(){
        gameRunning = true;
    }

    // start & stop ganze clock
    public boolean isClockStarted(){
        return clockRunning;
    }

    public void stopClock(){
        clockRunning = false;
    }

    private void startClock(){
        clockRunning = true;
    }


    private void sendPuls() {

        this.zeitJetzt = this.zeitJetzt + (this.verschnellerungsfaktor * 1000);

        final ZeitPuls puls = new ZeitPuls(this, new DateTime(this.zeitJetzt), this.verschnellerungsfaktor, gameRunning, this.abweichungZuSpielzeit);

        try {
            this.applicationEventPublisher.publishEvent(puls);
        } catch (final Exception e) {
            Zeitgeber.LOG.fatal(e, e);
        }
    }


    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    public int getVerspaetung(){
         return (int) abweichungZuSpielzeit / 1000;
    }


}