package com.googlecode.mad_schuelerturnier.model;

import com.googlecode.mad_schuelerturnier.model.comperators.MannschaftsCretionDateComperator;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestMannschaftsCretionDateComperator {
    @Test
    @Ignore
    public void TestCreationDateComperator() throws InterruptedException {

        List<Mannschaft> list = new ArrayList<Mannschaft>();

        Mannschaft m1 = new Mannschaft();
        m1.setAnzahlSpieler(1);

        Thread.sleep(30);

        Mannschaft m2 = new Mannschaft();
        m2.setAnzahlSpieler(2);

        Thread.sleep(30);

        Mannschaft m3 = new Mannschaft();
        m3.setAnzahlSpieler(3);

        list.add(m3);
        list.add(m2);
        list.add(m1);

        Collections.sort(list, new MannschaftsCretionDateComperator());

        String end = "";
        for (Mannschaft man : list) {
            end = end + man.getAnzahlSpieler();
        }
        Assert.assertEquals("123", end);
    }

}
