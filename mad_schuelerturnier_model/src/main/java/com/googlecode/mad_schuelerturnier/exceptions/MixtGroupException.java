package com.googlecode.mad_schuelerturnier.exceptions;

/**
 * validierungsfehler, falls eine gemischte gruppe erzeugt werden soll
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class MixtGroupException extends Exception {
    private static final long serialVersionUID = 1L;

    public MixtGroupException() {
        super("!!! achtung, gemischte gruppen sind nicht erlaubt");
    }
}
