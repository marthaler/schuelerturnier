/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.compusw;

import java.util.Comparator;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class MehrToreComperator implements Comparator<RanglisteneintragZeile> {

    /*
     * (non-Javadoc)
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final RanglisteneintragZeile spA, final RanglisteneintragZeile spB) {
        if (spA.getToreErziehlt() > spB.getToreErziehlt()) {
            return -1;
        }

        if (spA.getToreErziehlt() == spB.getToreErziehlt()) {
            return 0;
        }

        return 1;
    }
}
