/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.utils;

import com.googlecode.mad_schuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.mad_schuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class SpielInformationExpert {

    private static final Logger LOG = Logger.getLogger(SpielInformationExpert.class);


    @Autowired
    public transient KategorieRepository kategorieRepo;


    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private SpielZeilenRepository spielZeilenRepo;

    public int getAnzahlSpiele() {
        return spielRepo.findGruppenSpiel().size();
    }

    public int getAnzahlFinale() {
        return spielRepo.findFinalSpiel().size();
    }


    public int getAnzahlNoetigeDurchfuehrungen(int spiele, int plaetze) {
        int mod = spiele % plaetze;

        int ret = spiele / plaetze;

        if (mod > 0) {
            ret++;
        }
        return ret;
    }

    public int durchfuehrungenAufteilen(int anzahl, int teiler) {
        return getAnzahlNoetigeDurchfuehrungen(anzahl, teiler);
    }

    public Integer getVorhandeneFinalplaetze() {
        int ret = 0;
        List<SpielZeile> zeilen = this.spielZeilenRepo.findFinalSpielZeilen();
        for (SpielZeile spielZeile : zeilen) {
            if (!spielZeile.isPause()) {
                ret = ret + 2;
            }
        }
        return ret;
    }

    public Integer getVorhandeneSpielplaetze() {
        int ret = 0;
        List<SpielZeile> zeilen = this.spielZeilenRepo.findGruppenSpielZeilen();
        for (SpielZeile spielZeile : zeilen) {
            if (!spielZeile.isPause()) {
                ret = ret + 3;
            }
        }
        return ret;
    }

}
