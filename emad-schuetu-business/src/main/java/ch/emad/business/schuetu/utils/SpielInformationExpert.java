/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.utils;

import ch.emad.model.schuetu.model.SpielZeile;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import ch.emad.persistence.schuetu.repository.SpielZeilenRepository;
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
    private SpielRepository spielRepo;

    @Autowired
    private SpielZeilenRepository spielZeilenRepo;

    public SpielInformationExpert() {
        LOG.info("instanziert: SpielInformationExpert");
    }

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
