/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.compusw;

import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.enums.RangierungsgrundEnum;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Auslagerung der Logik, welche nach mehr Toren sortiert
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public final class DirektbegegnungSortierer {

    private static final Logger LOG = Logger.getLogger(DirektbegegnungSortierer.class);

    private DirektbegegnungSortierer() {

    }

    public static void sortNachDirektbegegnung(RanglisteneintragHistorie history) {
        List<RanglisteneintragZeile> zeilen = history.getZeilen();
        final List<RanglisteneintragZeile> su = new ArrayList<RanglisteneintragZeile>();
        int startindex = -1;
        RanglisteneintragZeile last = null;
        for (int i = 0; i < zeilen.size(); i++) {
            final RanglisteneintragZeile temp = zeilen.get(i);

            if ((last != null) && (last.getTordifferenz() != temp.getTordifferenz())) {
                DirektbegegnungSortierer.subSortNachDirektbegegnung(su, startindex, history);
                startindex = -1;
                su.clear();
            }

            if ((temp.getRangierungsgrund().equals(RangierungsgrundEnum.WEITERSUCHEN))) {

                temp.setRangierungsgrund(RangierungsgrundEnum.DIREKTBEGEGNUNG);

                su.add(temp);

                if (su.size() == 1) {
                    startindex = i;
                }
            }
            last = temp;
        }
        DirektbegegnungSortierer.subSortNachDirektbegegnung(su, startindex, history);
    }

    private static void subSortNachDirektbegegnung(final List<RanglisteneintragZeile> su, int startindexIn, RanglisteneintragHistorie history) {

        int startindex = startindexIn;

        if (su.size() > 1) {

            if (su.size() != 2) {
                DirektbegegnungSortierer.LOG.warn("ACHTUNG, ES WIRD VERSUCHT DIE DIREKTBEGEGNUNG VON " + su.size() + " HERAUSZUFINDEN! FEHLER");
                for (final RanglisteneintragZeile ranglisteneintragZeile : su) {
                    ranglisteneintragZeile.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
                }
                return;
            }

            Collections.sort(su, new MehrToreComperator());
            final RanglisteneintragZeile mA = su.get(0);
            final RanglisteneintragZeile mB = su.get(1);

            final List<Spiel> s = history.getSpiel().getMannschaftA().getKategorie().getDirektbegegnungen(mA.getMannschaft(), mB.getMannschaft());

            // TODO achtung, hier ev fuer solche mit vor-und rueckrunde suche nach 2 direktbegegnungen !!!

            DirektbegegnungSortierer.LOG.debug("DIREKTBEGEGNUNGEN: " + s.size());

            int a = -1;
            if (s.size() == 1) {
                a = s.get(0).getPunkteVonMannschaft(mA.getMannschaft());
            }

            int b = -1;
            if (s.size() == 1) {
                b = s.get(0).getPunkteVonMannschaft(mB.getMannschaft());
            }

            if ((a > -1) && (b > -1)) {

                if (a < b) {
                    mA.setRangierungsgrund(RangierungsgrundEnum.DIREKTBEGEGNUNG);
                    mB.setRangierungsgrund(RangierungsgrundEnum.DIREKTBEGEGNUNG);
                    su.set(0, mB);
                    su.set(1, mA);
                }
            }

            if (a == b) {
                mA.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
                mB.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
            }

            for (final RanglisteneintragZeile ranglisteneintragZeile : su) {
                history.getZeilen().set(startindex, ranglisteneintragZeile);
                startindex = startindex + 1;
            }

            su.clear();
        }
    }
}