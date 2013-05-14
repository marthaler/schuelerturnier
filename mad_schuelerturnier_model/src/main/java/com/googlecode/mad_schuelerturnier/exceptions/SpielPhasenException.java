package com.googlecode.mad_schuelerturnier.exceptions;

import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;

/**
 * fehler, welcher anzeigt, dass eine methode aufgerufen wurde, welche in der angegebenen phase nicht zulaessig war
 * 
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SpielPhasenException extends Exception {

	private static final long serialVersionUID = 1L;
	private SpielPhasenEnum enu;

	public SpielPhasenException(final SpielPhasenEnum enu) {
		super("achtung aufruf nur zulaessig in der phase: " + enu);
		this.enu = enu;
	}
}
