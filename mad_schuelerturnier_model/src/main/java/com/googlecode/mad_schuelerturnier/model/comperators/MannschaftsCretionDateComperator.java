/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.comperators;

import com.googlecode.mad_schuelerturnier.model.Mannschaft;

import java.util.Comparator;

/**
 * vergleicht mannschaften aufgrund deren namen und anschliessend aufgrund des erstellungsdatums (fuer nummerierung)
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class MannschaftsCretionDateComperator implements Comparator<Mannschaft> {

    public int compare(Mannschaft arg0, Mannschaft arg1) {

        MannschaftsNamenComperator comp = new MannschaftsNamenComperator();

        int res = comp.compare(arg0, arg1);

        if (res == 0) {
            if (arg0.getCreationdate().isBefore(
                    arg1.getCreationdate().getMillis())) {
                return -1;
            }

            if (arg0.getCreationdate().isAfter(
                    arg1.getCreationdate().getMillis())) {
                return 1;
            }
        }

        return 0;
    }

}
