/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

package com.googlecode.madschuelerturnier.business.websiteinfo.model;

import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;

import java.util.ArrayList;
import java.util.List;

public class KlassenrangZeile {

    private int klasse;

    private GeschlechtEnum geschlecht;

    private List<MannschaftEintrag> mannschaften = new ArrayList<MannschaftEintrag>();

    public KlassenrangZeile() {
        mannschaften.add(new MannschaftEintrag());
        mannschaften.add(new MannschaftEintrag());
        mannschaften.add(new MannschaftEintrag());
        mannschaften.add(new MannschaftEintrag());
    }



    public List<List<String>> getAsZeilen(){

List<List<String>> zeilen = new ArrayList<List<String>>();

        List<String>  erste = new ArrayList<String>();
        erste.add("");
        erste.add(mannschaften.get(0).getName());
        erste.add(mannschaften.get(1).getName());
        erste.add(mannschaften.get(2).getName());
        erste.add(mannschaften.get(3).getName());

        zeilen.add(erste);

        List<String>  zweite = new ArrayList<String>();
        zweite.add("Schulhaus");
        zweite.add(mannschaften.get(0).getSchulhaus());
        zweite.add(mannschaften.get(1).getSchulhaus());
        zweite.add(mannschaften.get(2).getSchulhaus());
        zweite.add(mannschaften.get(3).getSchulhaus());

        zeilen.add(zweite);

        List<String>  dritte = new ArrayList<String>();
        dritte.add("Captain");
        dritte.add(mannschaften.get(0).getCaptain());
        dritte.add(mannschaften.get(1).getCaptain());
        dritte.add(mannschaften.get(2).getCaptain());
        dritte.add(mannschaften.get(3).getCaptain());

        zeilen.add(dritte);

        List<String>  vierte = new ArrayList<String>();
        vierte.add("Begleitperson");
        vierte.add(mannschaften.get(0).getBegleitperson());
        vierte.add(mannschaften.get(1).getBegleitperson());
        vierte.add(mannschaften.get(2).getBegleitperson());
        vierte.add(mannschaften.get(3).getBegleitperson());

        zeilen.add(vierte);

        return zeilen;
    }


    public String getName() {

        StringBuilder builder = new StringBuilder();

        if (geschlecht == com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum.K) {
            builder.append("Knaben ");
        } else {
            builder.append("Mädchen ");
        }
        builder.append(klasse);
        builder.append(". Klasse");
        return builder.toString();
    }

    public List<MannschaftEintrag> getMannschaften() {
        return this.mannschaften;
    }

    public void addNext(com.googlecode.madschuelerturnier.model.Mannschaft mannschaft) {
        for (MannschaftEintrag m : mannschaften) {
            if (!m.hasMannschaft()) {
                m.setMannschaft(mannschaft);
                return;
            }
        }
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public GeschlechtEnum getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(GeschlechtEnum geschlecht) {
        this.geschlecht = geschlecht;
    }



}



