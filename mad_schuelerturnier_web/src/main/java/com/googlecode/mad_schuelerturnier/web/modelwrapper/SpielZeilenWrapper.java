package com.googlecode.mad_schuelerturnier.web.modelwrapper;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.googlecode.mad_schuelerturnier.model.spiel.tabelle.SpielZeile;

public class SpielZeilenWrapper extends ListDataModel<SpielZeile> implements SelectableDataModel<SpielZeile> {

	SpielZeilenWrapper() {

	}

	SpielZeilenWrapper(List<SpielZeile> zeilen) {
		super(zeilen);
	}

	public SpielZeile getRowData(String arg0) {
		List<SpielZeile> cars = (List<SpielZeile>) getWrappedData();

		for (SpielZeile car : cars) {
			if (car.getStart().toString().equals(arg0))
				return car;
		}

		return null;
	}

	public Object getRowKey(SpielZeile arg0) {
		return arg0.getStart().toString();
	}

}
