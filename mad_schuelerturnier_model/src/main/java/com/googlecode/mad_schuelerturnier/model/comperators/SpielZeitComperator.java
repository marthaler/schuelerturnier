/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.comperators;

import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;

import java.util.Comparator;

/**
 * vergleicht spiele Aufgrund ihrer Startzeit
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SpielZeitComperator implements Comparator<Spiel> {

    public int compare(Spiel arg0, Spiel arg1) {


        if (arg0.getStart().getTime() < arg1.getStart().getTime()) {
            return -1;
        }

        if (arg0.getStart().getTime() > arg1.getStart().getTime()) {
            return 1;

        }

        return 0;
    }

}
