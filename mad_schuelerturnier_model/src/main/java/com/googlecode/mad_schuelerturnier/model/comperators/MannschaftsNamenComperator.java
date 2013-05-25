/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.comperators;

import com.googlecode.mad_schuelerturnier.model.Mannschaft;

import java.util.Comparator;

/**
 * vergleicht mannschaften aufgrund deren namen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class MannschaftsNamenComperator implements Comparator<Mannschaft> {

    public int compare(Mannschaft arg0, Mannschaft arg1) {
        return arg0.getName().compareTo(arg1.getName());
    }

}
