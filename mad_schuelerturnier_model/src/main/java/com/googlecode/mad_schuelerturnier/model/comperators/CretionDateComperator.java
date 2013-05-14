/** 
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.comperators;

import java.util.Comparator;

import com.googlecode.mad_schuelerturnier.model.Persistent;

/**
 * vergleicht Persistent aufgrund ihres erstellungsdatums 
 * 
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class CretionDateComperator implements Comparator<Persistent> {

	public int compare(Persistent arg0, Persistent arg1) {

			if (arg0.getCreationdate().isBefore(
					arg1.getCreationdate().getMillis())) {
				return -1;
			}

			if (arg0.getCreationdate().isAfter(
					arg1.getCreationdate().getMillis())) {
				return 1;
			}

		return 0;
	}

}
