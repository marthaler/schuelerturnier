/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.compusw;

import com.googlecode.madschuelerturnier.model.enums.RangierungsgrundEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Auslagerung der Logik, welche nach mehr Toren sortiert
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */

public final class MehrToreSortierer {

    private MehrToreSortierer() {

    }

    public static void sortNachMehrToren(RanglisteneintragHistorie ranglisteneintragHistorie) {

        List<RanglisteneintragZeile> zeilen = ranglisteneintragHistorie.getZeilen();

        final List<RanglisteneintragZeile> su = new ArrayList<RanglisteneintragZeile>();
        int startindex = -1;
        RanglisteneintragZeile last = null;
        for (int i = 0; i < zeilen.size(); i++) {
            final RanglisteneintragZeile temp = zeilen.get(i);

            if ((su.size() > 1) && (last != null) && ((last.getTordifferenz() != temp.getTordifferenz()) || (last.getPunkte() != temp.getPunkte()))) {
                subSortNachMehrToren(su, startindex, ranglisteneintragHistorie);
                startindex = -1;
                su.clear();
            }

            if (temp.getRangierungsgrund().equals(RangierungsgrundEnum.WEITERSUCHEN)) {

                temp.setRangierungsgrund(RangierungsgrundEnum.MEHRTORE);

                su.add(temp);

                if (su.size() == 1) {
                    startindex = i;
                }
            }
            last = temp;
        }
        subSortNachMehrToren(su, startindex, ranglisteneintragHistorie);

    }

    private static void subSortNachMehrToren(final List<RanglisteneintragZeile> su, int startindexIn, RanglisteneintragHistorie ranglisteneintragHistorie) {

        int startindex = startindexIn;
        if (su.size() > 1) {

            Collections.sort(su, new MehrToreComperator());
            RanglisteneintragZeile last = null;

            for (final RanglisteneintragZeile ranglisteneintragZeile : su) {

                if (last != null) {
                    if (last.getToreErziehlt() == ranglisteneintragZeile.getToreErziehlt()) {
                        ranglisteneintragZeile.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
                        last.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
                    }
                }
                last = ranglisteneintragZeile;
            }

            for (final RanglisteneintragZeile ranglisteneintragZeile : su) {
                ranglisteneintragHistorie.getZeilen().set(startindex, ranglisteneintragZeile);
                startindex = startindex + 1;
            }

            su.clear();
        }
    }
}