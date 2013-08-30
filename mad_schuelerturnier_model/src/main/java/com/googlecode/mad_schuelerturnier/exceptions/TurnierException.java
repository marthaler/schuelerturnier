package com.googlecode.mad_schuelerturnier.exceptions;

/**
 * Generelle Schuelerturnier Exception
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public class TurnierException extends Exception {

    private static final long serialVersionUID = 1L;

    public TurnierException(String text, Exception e) {
        super(text, e);
    }

    public TurnierException(String text) {
        super(text);
    }
}
