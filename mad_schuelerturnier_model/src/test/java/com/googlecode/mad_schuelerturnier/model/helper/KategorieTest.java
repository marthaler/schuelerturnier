package com.googlecode.mad_schuelerturnier.model.helper;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * TestSpielKontrollerTest
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */

public class KategorieTest {

    @Test
    public void TestSpielKontroller() {
        MannschaftTageskorrektur korr = new MannschaftTageskorrektur();
        SpielEinstellungen einst = new SpielEinstellungen();

        Map<String, String> map = new HashMap<String, String>();

        map.put("sa,9:24,a", "so,9:24,a");
        map.put("sa,9:00,a", "so,9:00,a");

        korr.setVertauschungen(map);
        einst.placeMannschaftsTageskorrekturen(korr);
        System.out.println(einst.getSpielVertauschungen());


    }
}
