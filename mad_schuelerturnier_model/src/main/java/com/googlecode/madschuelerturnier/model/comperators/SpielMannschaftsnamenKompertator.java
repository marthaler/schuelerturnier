/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.comperators;

import com.googlecode.madschuelerturnier.model.spiel.Spiel;

import java.util.Comparator;

/**
 * vergleicht spiele Aufgrund ihrer beiden Mannschaftsnamen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SpielMannschaftsnamenKompertator implements Comparator<Spiel> {

    public int compare(Spiel arg0, Spiel arg1) {

        String aName = arg0.getMannschaftAName() + arg0.getMannschaftBName();
        String bName = arg1.getMannschaftAName() + arg1.getMannschaftBName();

        if (aName.compareTo(bName) < 0) {
            return -1;
        }

        if (aName.compareTo(bName) > 0) {
            return 1;

        }

        return 0;
    }

}
