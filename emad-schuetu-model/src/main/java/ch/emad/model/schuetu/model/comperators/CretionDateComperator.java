/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.comperators;

import ch.emad.model.schuetu.model.Persistent;

import java.io.Serializable;
import java.util.Comparator;

/**
 * vergleicht Persistent aufgrund ihres erstellungsdatums
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class CretionDateComperator implements Comparator<Persistent>, Serializable {

    public int compare(Persistent arg0, Persistent arg1) {

        if (arg0.getCreationdate().before(
                arg1.getCreationdate())) {
            return -1;
        }

        if (arg0.getCreationdate().after(
                arg1.getCreationdate())) {
            return 1;
        }

        return 0;
    }

}
