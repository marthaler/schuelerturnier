/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */
package ch.emad.persistence.schuetu.repository;

import ch.emad.model.schuetu.model.SpielEinstellungen;

/**
 * Trick fuer die Speicherung und den Zugriff auf das Spieleinstellungsobjekt, welches bloss als
 * Xstream Text abgespeichert ist
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.0
 */
public interface SpielEinstellungenRepository2 {

    public SpielEinstellungen getEinstellungen();

    public void save(SpielEinstellungen einstellung);

    public boolean isInitialized();

}