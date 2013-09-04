/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;

import java.util.List;

/**
 * Dient dazu zuvor gespeicherte XLS Spiele zu laden
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface DataLoader {

    List<Mannschaft> loadMannschaften();

    List<Spiel> loadSpiele();

    List<Mannschaft> loadMannschaften(boolean knaben, boolean maedchen, Integer... klassenIn);

}