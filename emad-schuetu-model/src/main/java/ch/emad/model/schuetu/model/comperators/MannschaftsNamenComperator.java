/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.comperators;

import ch.emad.model.schuetu.model.Mannschaft;

import java.io.Serializable;
import java.util.Comparator;

/**
 * vergleicht mannschaften aufgrund deren namen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class MannschaftsNamenComperator implements Comparator<Mannschaft>, Serializable {

    public int compare(Mannschaft arg0, Mannschaft arg1) {
        return arg0.getName().compareTo(arg1.getName());
    }

}
