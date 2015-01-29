package com.googlecode.madschuelerturnier.business.pdf;/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */

import ch.emad.schuetu.reports.interfaces.CouvertReportable;


/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class Mannschaft2  implements CouvertReportable {

    private int count;

    public Mannschaft2(int count){
        this.count = count;
    }

    @Override
    public String getAnrede() {
        return "Anrede " + count;
    }

    @Override
    public String getNameVorname() {
        return "Name Vorname " + count;
    }

    @Override
    public String getStrasse() {
        return "Strasse " + count;
    }

    @Override
    public String getPLZOrt() {
        return "PLZ Ort " + count;
    }
}
