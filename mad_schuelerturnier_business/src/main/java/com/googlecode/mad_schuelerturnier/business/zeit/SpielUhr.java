package com.googlecode.mad_schuelerturnier.business.zeit;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class SpielUhr implements ApplicationListener<ZeitPuls> {

    public SpielUhr() {
    }

    private String richtigeZeit = "hallo";
    private String spielZeit = "hallo";

    public void onApplicationEvent(final ZeitPuls event) {

        final ZeitPuls p = event;

        final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        this.richtigeZeit = fmt.format(p.getEchteZeit().toDate());
        this.spielZeit = fmt.format(p.getSpielZeit().toDate());

    }

    public String getRichtigeZeit() {
        return this.richtigeZeit;
    }

    public String getSpielZeit() {
        return this.spielZeit;
    }

    public void touch() {

    }

}