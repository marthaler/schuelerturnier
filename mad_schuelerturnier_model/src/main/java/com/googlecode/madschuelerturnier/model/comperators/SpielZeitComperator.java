/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.comperators;

import com.googlecode.madschuelerturnier.model.Spiel;

import java.io.Serializable;
import java.util.Comparator;

/**
 * vergleicht spiele Aufgrund ihrer Startzeit
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SpielZeitComperator implements Comparator<Spiel>, Serializable {

    public int compare(Spiel arg0, Spiel arg1) {

        if (arg0 == null || arg1 == null || arg0.getStart() == null || arg1.getStart() == null) {
            return 1;
        }

        if (arg0.getStart().getTime() < arg1.getStart().getTime()) {
            return -1;
        }

        if (arg0.getStart().getTime() > arg1.getStart().getTime()) {
            return 1;

        }

        return 0;
    }

}
