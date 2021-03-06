/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.spielkorrektur;

import ch.emad.model.schuetu.model.Spiel;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface SpielService {

    List<Spiel> readAllSpiele();

    Spiel findSpiel(String id);

    void doKorrektur(Spiel spiel);
}
