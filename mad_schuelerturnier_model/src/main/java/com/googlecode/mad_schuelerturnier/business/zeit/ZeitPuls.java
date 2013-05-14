/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.zeit;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * Zeit Event, mit der aktuellen Zeit und einem alfaelligen verschnellerungsfaktor
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class ZeitPuls extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 5743058377815147529L;

    private DateTime zeit = null;
    private int verschnellerungsFaktor = 1;
    private boolean unterbruch = false;
    private long abweichungDerSpielzeit = 0;

    public ZeitPuls(final Object source, final DateTime zeit, final int verschnellerungsFaktor, final boolean gameStarted, final long abweichungZuSpielzeit) {
        super(source);
        this.zeit = zeit;
        this.verschnellerungsFaktor = verschnellerungsFaktor;
        this.unterbruch = !gameStarted;
        this.abweichungDerSpielzeit = abweichungZuSpielzeit;
    }

    public DateTime getEchteZeit() {
        return this.zeit;
    }

    public DateTime getSpielZeit() {
        return new DateTime(this.zeit.getMillis() + this.abweichungDerSpielzeit);
    }

    public int getVerschnellerungsFaktor() {
        return this.verschnellerungsFaktor;
    }

    public boolean isUnterbruch() {
        return this.unterbruch;
    }

    public long getSpielzeitAbweichungZuEchteZeitInSekunden() {
        return (this.abweichungDerSpielzeit / 1000);
    }

    public boolean habenWirVerspaetung() {
        if (this.abweichungDerSpielzeit < 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Zeitpuls [zeit=" + this.zeit + ", verschnellerungsFaktor=" + this.verschnellerungsFaktor + ", unterbruch=" + this.unterbruch + ", abweichungZuSpielzeit=" + this.abweichungDerSpielzeit / 1000 + "]";
    }

}