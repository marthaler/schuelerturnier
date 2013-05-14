/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.web.modelwrapper;

import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.model.spiel.tabelle.SpielZeile;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class SpielBusiness {

    private static final Logger LOG = Logger.getLogger(SpielBusiness.class);

    @Autowired
    private Business business;



	/*
     * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.mad_schuelerturnier.business.sdfdf#getSchulhausListe(java
	 * .lang.String)
	 */

    public SpieleContainer getSpielzeilen(boolean sonntag) {


        DateTime start = new DateTime(business.getSpielEinstellungen().getStarttag());

        if (sonntag) {
            //start = start.plusDays(1);
        }

        start = start.plusHours(7);
        DateTime end = start.plusHours(12);

        List<SpielZeile> zeilen = new ArrayList<SpielZeile>();
        while (start.isBefore(end.getMillis())) {
            SpielZeile zeile = new SpielZeile();
            zeile.setStart(start.toDate());
            zeilen.add(zeile);

            start = start.plusMinutes(business.getSpielEinstellungen().getPause() + business.getSpielEinstellungen().getSpiellaenge());

        }
        SpieleContainer c = new SpieleContainer();
        c.setSelectedZeilen(zeilen.toArray());
        return c;
    }
}