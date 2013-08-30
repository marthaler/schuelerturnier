package com.googlecode.mad_schuelerturnier.web;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ReloadBackingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int i = 0;

    private boolean state = false; // NOSONAR

    public boolean getState() {
        i++;
        state = false;
        if (i % 3 == 0) {

            return false;
        }
        return true;
    }
}
