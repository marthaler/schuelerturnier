/** 
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.comperators;

import java.util.Comparator;
import java.util.List;

import com.googlecode.mad_schuelerturnier.model.Kategorie;

/**
 * vergleicht mannschaften aufgrund deren namen
 * 
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class KategorieComperator implements Comparator<Kategorie> {

	public int compare(Kategorie arg0, Kategorie arg1) {
		int geschlecht = arg0.getGruppeA().getGeschlecht().compareTo(arg1.getGruppeA().getGeschlecht());
		if(geschlecht == 0){
			
			List<Integer> klassenA = arg0.getKlassen();
			Integer klA = 0;
			for (Integer klasseEnum : klassenA) {
				if(klA < klasseEnum){
					klA = klasseEnum;
				}
			}
			List<Integer> klassenB = arg1.getKlassen();
			Integer klB= 0;
			for (Integer klasseEnum : klassenB) {
				if(klB < klasseEnum){
					klB = klasseEnum;
				}
			}
			
			return klA.compareTo(klB);
		}
		return 0;	
	}
}
