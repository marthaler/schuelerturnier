/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;

import java.util.List;

/**
 * Dient dazu zuvor gespeicherte XLS Dumps zu laden
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.5
 */
public interface DataLoader {

    List<Mannschaft> loadMannschaften();

    List<Spiel> loadSpiele();

    List<DBAuthUser> loadDBUser();

    List<Mannschaft> loadMannschaften(boolean knaben, boolean maedchen, Integer... klassenIn);

    byte[] loadFile();
}
