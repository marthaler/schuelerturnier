/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu;

import ch.emad.model.common.model.DBAuthUser;
import ch.emad.model.common.model.Text;
import ch.emad.model.schuetu.model.Kontakt;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Penalty;
import ch.emad.model.schuetu.model.Spiel;

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

    List<ch.emad.model.common.model.File> loadAttachements();

    List<Mannschaft> loadMannschaften(boolean knaben, boolean maedchen, Integer... klassenIn);

    List<Text> loadTexte();

    byte[] loadFile();

    List<Penalty> loadPenalty();

    List<Kontakt> loadKontakte();

}
