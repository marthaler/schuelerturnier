/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu.modelwrapper;

import ch.emad.model.schuetu.model.SpielZeile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SpieleContainer implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<SpielZeile> zeilen = new ArrayList<SpielZeile>();

    private SpielZeilenWrapper dataModel;

    public List<SpielZeile> getZeilen() {
        return zeilen;
    }

    public void setZeilen(List<SpielZeile> zeilen) {
        dataModel = new SpielZeilenWrapper(zeilen);
        this.zeilen = zeilen;
    }

    public SpielZeile[] getSelectedZeilen() {

        List<SpielZeile> ret = new ArrayList<SpielZeile>();

        for (SpielZeile ze : zeilen) {
            if (!ze.isPause()) {
                ret.add(ze);
            }
        }
        SpielZeile[] retArr = new SpielZeile[ret.size()];
        for (int i = 0; i < retArr.length; i++) {
            retArr[i] = ret.get(i);

        }

        return retArr;
    }

    public void setSelectedZeilen(Object[] selectedZeilen) {
        List<SpielZeile> zel = new ArrayList<SpielZeile>();
        for (Object spielZeile : selectedZeilen) {
            zel.add((SpielZeile) spielZeile);
        }

        for (SpielZeile ze : zeilen) {
            if (zel.contains(ze)) {
                ze.setPause(false);
            } else {
                ze.setPause(true);
            }
        }
    }

    public SpielZeilenWrapper getDataModel() {
        return dataModel;
    }

    public void setDataModel(SpielZeilenWrapper dataModel) {
        this.dataModel = dataModel;
    }

}
