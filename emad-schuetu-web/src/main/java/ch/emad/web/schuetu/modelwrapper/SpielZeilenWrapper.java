package ch.emad.web.schuetu.modelwrapper;

import ch.emad.model.schuetu.model.SpielZeile;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class SpielZeilenWrapper extends ListDataModel<SpielZeile> implements SelectableDataModel<SpielZeile> {

    public SpielZeilenWrapper() {

    }

    SpielZeilenWrapper(List<SpielZeile> zeilen) {
        super(zeilen);
    }

    public SpielZeile getRowData(String arg0) {
        List<SpielZeile> cars;
        cars = (List<SpielZeile>) getWrappedData();

        for (SpielZeile car : cars) {
            if (car.getStart().toString().equals(arg0)) {
                return car;
            }
        }

        return null;
    }

    public Object getRowKey(SpielZeile arg0) {
        return arg0.getStart().toString();
    }

}
