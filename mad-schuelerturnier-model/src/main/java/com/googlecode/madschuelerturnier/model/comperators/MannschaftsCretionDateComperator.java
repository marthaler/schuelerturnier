/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.comperators;

import com.googlecode.madschuelerturnier.model.Mannschaft;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Vergleicht Mannschaften aufgrund deren Namen und anschliessend aufgrund des Erstellungsdatums (fuer nummerierung)
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class MannschaftsCretionDateComperator implements Comparator<Mannschaft>, Serializable {

    public int compare(Mannschaft arg0, Mannschaft arg1) {

        MannschaftsNamenComperator comp = new MannschaftsNamenComperator();

        int res = comp.compare(arg0, arg1);

        if (res == 0) {
            if (arg0.getCreationdate().before(
                    arg1.getCreationdate())) {
                return -1;
            }

            if (arg0.getCreationdate().after(
                    arg1.getCreationdate())) {
                return 1;
            }
        }

        return 0;
    }

}
