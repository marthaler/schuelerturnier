package com.googlecode.madschuelerturnier.exceptions;

import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;

/**
 * fehler, welcher anzeigt, dass eine methode aufgerufen wurde, welche in der angegebenen phase nicht zulaessig war
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SpielPhasenException extends Exception {

    private static final long serialVersionUID = 1L;

    public SpielPhasenException(final SpielPhasenEnum enu) {
        super("achtung aufruf nur zulaessig in der phase: " + enu);
    }
}