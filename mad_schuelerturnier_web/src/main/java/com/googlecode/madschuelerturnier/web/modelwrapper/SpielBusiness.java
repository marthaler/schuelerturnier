/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.modelwrapper;

import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
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

    public SpielBusiness() {
        LOG.info("Instanziert: SpielBusiness");
    }

    @Autowired
    private Business business;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.googlecode.madschuelerturnier.business.sdfdf#getSchulhausListe(java
     * .lang.String)
     */
    public SpieleContainer getSpielzeilen() {

        DateTime start = new DateTime(business.getSpielEinstellungen().getStarttag());

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