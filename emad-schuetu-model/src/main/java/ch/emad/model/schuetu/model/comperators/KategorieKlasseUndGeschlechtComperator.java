/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.comperators;

import ch.emad.model.schuetu.model.Kategorie;

import java.io.Serializable;
import java.util.Comparator;

/**
 * vergleicht mannschaften aufgrund deren namen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class KategorieKlasseUndGeschlechtComperator implements Comparator<Kategorie>, Serializable {

    public int compare(Kategorie a, Kategorie b) {

        if (a.evaluateLowestClass() < b.evaluateLowestClass()) {
            return -1;
        }

        if (a.evaluateLowestClass() > b.evaluateLowestClass()) {
            return 1;
        }

        return a.getGruppeA().getGeschlecht().compareTo(b.getGruppeA().getGeschlecht());


    }
}
