/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.compusw;

import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import org.apache.log4j.Logger;

import java.util.Comparator;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SpielComperator implements Comparator<Spiel> {

    private static final Logger LOG = Logger.getLogger(SpielComperator.class);

    /*
     * (non-Javadoc)
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final Spiel spA, final Spiel spB) {

        if ((spA == null) || (spB == null)) {
            return -1;
        }
        try {
            if (spA.getStart().getTime() < spB.getStart().getTime()) {
                return -1;
            }
        } catch (final Exception e) {
            SpielComperator.LOG.error(e.getMessage(), e);
        }


        if (spA.getStart().getTime() == spB.getStart().getTime()) {
            if (spA.getPlatz().ordinal() == spB.getPlatz().ordinal()) {
                return 0;
            }

            if (spA.getPlatz().ordinal() < spB.getPlatz().ordinal()) {
                return -1;
            }

            if (spA.getPlatz().ordinal() > spB.getPlatz().ordinal()) {
                return 1;
            }
        }

        return 1;
    }

}
