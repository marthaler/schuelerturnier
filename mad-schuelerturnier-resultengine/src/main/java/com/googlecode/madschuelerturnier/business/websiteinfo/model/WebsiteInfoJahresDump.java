/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

package com.googlecode.madschuelerturnier.business.websiteinfo.model;

import com.googlecode.madschuelerturnier.model.Spiel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u203244 on 24.03.14.
 */
public class WebsiteInfoJahresDump {

    private List<Spiel> gruppenspiele = null;

    private List<Spiel> finalspiele = null;

    private List<TeamGruppen> knabenMannschaften = null;

    private List<TeamGruppen> maedchenMannschaften = null;

    private List<KlassenrangZeile> rangliste = new ArrayList<KlassenrangZeile>();

    public List<Spiel> getGruppenspiele() {
        return gruppenspiele;
    }

    public void setGruppenspiele(List<Spiel> gruppenspiele) {
        this.gruppenspiele = gruppenspiele;
    }

    public List<Spiel> getFinalspiele() {
        return finalspiele;
    }

    public void setFinalspiele(List<Spiel> finalspiele) {
        this.finalspiele = finalspiele;
    }

    public List<TeamGruppen> getKnabenMannschaften() {
        return knabenMannschaften;
    }

    public void setKnabenMannschaften(List<TeamGruppen> knabenMannschaften) {
        this.knabenMannschaften = knabenMannschaften;
    }

    public List<TeamGruppen> getMaedchenMannschaften() {
        return maedchenMannschaften;
    }

    public void setMaedchenMannschaften(List<TeamGruppen> maedchenMannschaften) {
        this.maedchenMannschaften = maedchenMannschaften;
    }

    public List<KlassenrangZeile> getRangliste() {
        return rangliste;
    }

    public void setRangliste(List<KlassenrangZeile> rangliste) {
        this.rangliste = rangliste;
    }
}
