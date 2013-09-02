/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.zeit;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * Uhr fuer die Darstellung der Zeit und Verspaetung im GUI
 *
 * @author $Author: marthaler.worb@gmail.com $
 */
@Component
public class SpielUhr implements ApplicationListener<ZeitPuls> {

    public static String NOT_INIT = "noch_nicht_gesetzt";

    public SpielUhr() {
    }

    private String richtigeZeit = NOT_INIT;
    private String spielZeit = NOT_INIT;

    public void onApplicationEvent(final ZeitPuls event) {
        final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        this.richtigeZeit = fmt.format(event.getEchteZeit().toDate());
        this.spielZeit = fmt.format(event.getSpielZeit().toDate());
    }

    public String getRichtigeZeit() {
        return this.richtigeZeit;
    }

    public String getSpielZeit() {
        return this.spielZeit;
    }

    @Override
    public String toString() {
        return "SpielUhr{" +
                "richtigeZeit='" + richtigeZeit + '\'' +
                ", spielZeit='" + spielZeit + '\'' +
                '}';
    }

}