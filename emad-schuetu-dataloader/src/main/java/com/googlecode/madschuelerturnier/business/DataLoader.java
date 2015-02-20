/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.model.support.File;

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

    List<File> loadAttachements();

    List<Mannschaft> loadMannschaften(boolean knaben, boolean maedchen, Integer... klassenIn);

   List<Text> loadTexte();

    byte[] loadFile();

    List<Penalty> loadPenalty();

    List<Kontakt> loadKontakte();

}
