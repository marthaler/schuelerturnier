package com.googlecode.madschuelerturnier.business.zeit;
/**
 * Apache License 2.0
 */

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;

/**
 * Zaehlt rueckwaerts bis zu einem bestimmen Zeitpunkt
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class Countdown {

    private DateTime ablauf;
    private DateTime letzte;
    private int dauer;
    private int sekundenToGo = 0;

    public Countdown(final DateTime jetzt, final int dauerInSeconds) {
        super();
        this.dauer = dauerInSeconds;
        this.ablauf = jetzt.plusSeconds(dauerInSeconds);
        this.letzte = jetzt;

        this.sekundenToGo = dauerInSeconds;

    }

    public Countdown(final DateTime jetzt, final DateTime bis) {
        super();
        this.dauer = bis.getSecondOfDay() - jetzt.getSecondOfDay();
        this.ablauf = bis;
        this.letzte = jetzt;
        this.sekundenToGo = this.dauer;
    }

    public void signalTime(final DateTime jetzt) {

        sekundenToGo = (int) (sekundenToGo - (jetzt.getMillis() - letzte.getMillis()) / 1000);

        this.letzte = jetzt;
    }

    public String getZeit() {
        final DateTime t = this.ablauf.minus(this.letzte.getMillis());

        if (sekundenToGo < 0) {
            return "00:00";
        }

        final SimpleDateFormat fmt = new SimpleDateFormat("mm:ss");
        return fmt.format(t.toDate());
    }

    public int getSecondsPlus2() {
        final DateTime t = this.ablauf.minus(this.letzte.getMillis());
        final int minutes = t.getMinuteOfHour();
        final int seconds = t.getSecondOfMinute();
        return (minutes * 60 * 1000) + (seconds * 1000) + 2000;
    }

    public boolean isFertig() {
        return getZeit().equals("00:00");
    }
}
