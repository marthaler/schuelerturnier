/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Color Objekt
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
public class Colors implements Serializable {

    private List<String> colors = new ArrayList<String>();
    private List<String> foreground = new ArrayList<String>();
    private List<String> namen = new ArrayList<String>();

    public Colors(){
        colors.add("Yellow");
        colors.add("Orange");
        colors.add("Pink");
        colors.add("Red");
        colors.add("Gray");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Black");
        colors.add("White");
        colors.add("DodgerBlue");

        namen.add("Gelb");
        namen.add("Orange");
        namen.add("Pink");
        namen.add("Rot");
        namen.add("Grau");
        namen.add("Grün");
        namen.add("Blau");
        namen.add("Schwarz");
        namen.add("Weiss");
        namen.add("Hellblau");

        foreground.add("Black");
        foreground.add("Black");
        foreground.add("Black");
        foreground.add("White");
        foreground.add("White");
        foreground.add("White");
        foreground.add("White");
        foreground.add("White");
        foreground.add("Black");
        foreground.add("Whyte");
    }

    public String getBackground(String farbe){
        int i = namen.indexOf(farbe);
        return foreground.get(i);
    }
    public String getColor(String farbe){
        int i = namen.indexOf(farbe);
        return colors.get(i);
    }

    public List<String> getColors(){
        return namen;
    }

    public String getNextFarbe(String farbe){
        int i = namen.indexOf(farbe);
        i++;
        if(i == namen.size()){
            return namen.get(0);
        }
        return namen.get(i);

    }

}