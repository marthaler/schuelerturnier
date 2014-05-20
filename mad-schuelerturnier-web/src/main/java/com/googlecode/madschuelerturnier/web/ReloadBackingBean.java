package com.googlecode.madschuelerturnier.web;
/**
 * Apache License 2.0
 */
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
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
