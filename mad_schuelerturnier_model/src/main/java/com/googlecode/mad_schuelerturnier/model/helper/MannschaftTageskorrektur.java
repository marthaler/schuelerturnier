package com.googlecode.mad_schuelerturnier.model.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * dient dazu nach der automatischen zuordnung des spielplanes einzelne spiele zu tauschen
 * <p/>
 * User: dama
 * Date: 01.04.13
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class MannschaftTageskorrektur {

    private Map<String, String> vertauschungen = new HashMap<String, String>();

    public Map<String, String> getVertauschungen() {
        return vertauschungen;
    }

    public void setVertauschungen(Map<String, String> vertauschungen) {
        this.vertauschungen = vertauschungen;
    }


}
