/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

package ch.emad.business.schuetu.websiteinfo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u203244 on 24.03.14.
 */
public class TeamGruppen {

    private String name;

    private int total;

    private List<Mannschaft> mannschaften = new ArrayList<Mannschaft>();

    public List<Mannschaft> getMannschaften() {
        return mannschaften;
    }

    public void addMannschaft(Mannschaft mannschaften) {
        this.mannschaften.add(mannschaften);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
