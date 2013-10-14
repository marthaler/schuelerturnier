package com.googlecode.madschuelerturnier.web.controllers.util;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.Override;
import java.lang.String;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
public class HalloBean  {

    private String name;

    private int zahl;


    public int getZahl() {
        return zahl;
    }

    public void setZahl(int zahl) {
        this.zahl = zahl;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HalloBean{" +
                "name='" + name + '\'' +
                ", zahl=" + zahl +
                '}';
    }
}
