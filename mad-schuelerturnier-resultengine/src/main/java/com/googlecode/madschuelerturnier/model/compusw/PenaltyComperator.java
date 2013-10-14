/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.compusw;

import com.googlecode.madschuelerturnier.model.Penalty;
import org.apache.log4j.Logger;

import java.util.Comparator;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class PenaltyComperator implements Comparator<RanglisteneintragZeile> {

    private static final Logger LOG = Logger.getLogger(PenaltyComperator.class);

    private Penalty p = null;

    PenaltyComperator(final Penalty p) {
        this.p = p;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final RanglisteneintragZeile spA, final RanglisteneintragZeile spB) {

        try {
            if (this.p.getRang(spA.getMannschaft()) < this.p.getRang(spB.getMannschaft())) {
                return -1;
            }
        } catch (final Exception e) {
            PenaltyComperator.LOG.error(e.getMessage(), e);
        }

        try {
            if (this.p.getRang(spA.getMannschaft()) == this.p.getRang(spB.getMannschaft())) {
                return 0;
            }
        } catch (final Exception e) {
            PenaltyComperator.LOG.error(e.getMessage(), e);
        }

        return 1;
    }
}
