/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.comperators;

import com.googlecode.madschuelerturnier.model.Kategorie;

import java.io.Serializable;
import java.util.Comparator;

/**
 * vergleicht mannschaften aufgrund deren namen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class KategorieNameComperator implements Comparator<Kategorie>, Serializable {

    public int compare(Kategorie arg0, Kategorie arg1) {
        return arg0.getName().compareTo(arg1.getName());
    }
}