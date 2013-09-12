/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.vorbereitung.helper.SpielverteilerHelper;
import com.googlecode.madschuelerturnier.business.vorbereitung.helper.SpielzeilenValidator;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.enums.PlatzEnum;
import com.googlecode.madschuelerturnier.model.enums.SpielTageszeit;
import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
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
public class E5Spielverteiler {

    private static final Logger LOG = Logger.getLogger(E5Spielverteiler.class);

    @Autowired
    private SpielZeilenRepository spielzeilenRepo;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private SpielzeilenValidator val;

    @Autowired
    private SpielverteilerHelper helper;

    @Autowired
    private Business business;


    public void spieleAutomatischVerteilen() {

        behandleFinalspielzeilen();

        SpielZeile vorher = null;

        List<SpielZeile> gruppenSpieleZeilen = spielzeilenRepo.findGruppenSpielZeilen();

        List<Spiel> gruppenSpiele = spielRepo.findGruppenSpiel();

        helper.init(gruppenSpiele);

        for (SpielZeile zeilen : gruppenSpieleZeilen) {

            if (zeilen.isPause() && !isSammstagNachNeuekategoriesperre(zeilen)) {
                vorher = zeilen;
                continue;
            }

            stopA(vorher, gruppenSpiele, zeilen);

            stopB(vorher, gruppenSpiele, zeilen);

            stopC(vorher, gruppenSpiele, zeilen);

            vorher = spielzeilenRepo.save(zeilen);

        }


        LOG.info("spiele verteilt ");
        if (gruppenSpiele.size() > 0) {
            LOG.fatal("NICHT ZUGEORDNETE SPIELE !!! " + gruppenSpiele.size());
            LOG.fatal("NICHT ZUGEORDNETE SPIELE --> " + gruppenSpiele);
        }

        LOG.info("werde jetzt ueberzaehlige pausen loeschen");
        // sichern und Anfang und Schluss Pausen entfernen
        List<SpielZeile> zeilenSo = business.getSpielzeilen(true);
        List<SpielZeile> zeilenSa = business.getSpielzeilen(false);
        removeUeberschuss(zeilenSo);
        removeUeberschuss(zeilenSa);

    }

    private void stopA(SpielZeile vorher, List<Spiel> gruppenSpiele, SpielZeile zeilen) {
        boolean stopA = false;

        int iA = 0;
        while (!stopA) {

            Spiel tempSpiel = this.getNextSpiel(zeilen, gruppenSpiele, iA);

            if (tempSpiel == null) {
                break;
            }

            tempSpiel.setPlatz(PlatzEnum.A);
            zeilen.setA(tempSpiel);
            tempSpiel.setStart(zeilen.getStart());
            zeilen.setKonfliktText(null);
            zeilen.setPause(false);

            String ret = val.validateSpielZeilen(vorher, zeilen);
            if (ret == null || ret.equals("")) {
                gruppenSpiele.remove(tempSpiel);
                helper.consumeSpiel(tempSpiel);
                spielRepo.save(tempSpiel);
                stopA = true;
            } else {
                zeilen.setA(null);
            }
            iA++;
        }
    }

    private void stopC(SpielZeile vorher, List<Spiel> gruppenSpiele, SpielZeile zeilen) {
        boolean stopC = false;

        int iC = 0;
        while (!stopC) {

            Spiel tempSpiel = this.getNextSpiel(zeilen, gruppenSpiele, iC);
            if (tempSpiel == null) {
                break;
            }

            tempSpiel.setPlatz(PlatzEnum.C);
            zeilen.setC(tempSpiel);

            tempSpiel.setStart(zeilen.getStart());
            zeilen.setKonfliktText(null);
            zeilen.setPause(false);

            String ret = val.validateSpielZeilen(vorher, zeilen);
            if (ret == null || ret.equals("")) {
                helper.consumeSpiel(tempSpiel);
                gruppenSpiele.remove(tempSpiel);
                spielRepo.save(tempSpiel);
                stopC = true;
            } else {
                zeilen.setC(null);
            }
            iC++;
        }
    }

    private void stopB(SpielZeile vorher, List<Spiel> gruppenSpiele, SpielZeile zeilen) {
        boolean stopB = false;

        int iB = 0;
        while (!stopB) {

            Spiel tempSpiel = this.getNextSpiel(zeilen, gruppenSpiele, iB);

            if (tempSpiel == null) {
                break;
            }

            tempSpiel.setPlatz(PlatzEnum.B);
            zeilen.setB(tempSpiel);
            tempSpiel.setStart(zeilen.getStart());
            zeilen.setKonfliktText(null);
            zeilen.setPause(false);

            String ret = val.validateSpielZeilen(vorher, zeilen);
            if (ret == null || ret.equals("")) {

                helper.consumeSpiel(tempSpiel);
                gruppenSpiele.remove(tempSpiel);
                spielRepo.save(tempSpiel);
                stopB = true;
            } else {
                zeilen.setB(null);
            }
            iB++;
        }
    }

    private void behandleFinalspielzeilen() {
        int i = 0;
        List<Spiel> finalSpiele = spielRepo.findFinalSpiel();

        List<SpielZeile> finalspieleZeilen = spielzeilenRepo
                .findFinalSpielZeilen();

        for (SpielZeile zeilen : finalspieleZeilen) {
            if (zeilen.isPause()) {
                continue;
            }

            try {
                zeilen.setA(finalSpiele.get(i));
                finalSpiele.get(i).setPlatz(PlatzEnum.A);
                finalSpiele.get(i).setStart(zeilen.getStart());
                spielRepo.save(finalSpiele.get(i));
                i++;
                zeilen = spielzeilenRepo.save(zeilen);
                zeilen.setB(finalSpiele.get(i));
                finalSpiele.get(i).setPlatz(PlatzEnum.B);
                finalSpiele.get(i).setStart(zeilen.getStart());
                spielRepo.save(finalSpiele.get(i));
                i++;
                spielzeilenRepo.save(zeilen);
            } catch (IndexOutOfBoundsException e) {
                LOG.info("index wurde ueberschritten Finalspiel");
            }
        }
    }

    private Spiel getNextSpiel(SpielZeile zeile, List<Spiel> gruppenSpiele, int iB) {

        Spiel firstEgal = null;
        int ieg = 0;
        int ineg = 0;
        for (Spiel spiel : gruppenSpiele) {

            if (firstEgal == null && spiel.getMannschaftA().getKategorie().getSpielwunsch() == SpielTageszeit.EGAL) {

                if (ieg == iB) {

                    // falls bereits spaeter als 16 uhr am Sammstag, keine neue Kategorie mehr verplanen
                    if (isSammstagNachNeuekategoriesperre(zeile)) {
                        if (!helper.isFirstSpielInGruppe(spiel)) {
                            firstEgal = spiel;
                        }
                    } else {
                        firstEgal = spiel;
                    }
                }
                ieg++;
            }

            if (zeile.getSpieltageszeit() == spiel.getMannschaftA().getKategorie().getSpielwunsch()) {
                if (ineg == iB) {

                    // falls bereits spaeter als 16 uhr am Sammstag, keine neue Kategorie mehr verplanen
                    if (isSammstagNachNeuekategoriesperre(zeile)) {
                        if (!helper.isFirstSpielInGruppe(spiel)) {
                            return spiel;
                        }
                    } else {
                        firstEgal = spiel;
                    }
                }
                ineg++;
            }
        }
        return firstEgal;
    }


    private boolean isSammstagNachNeuekategoriesperre(SpielZeile zeile) {
        DateTime start = new DateTime(zeile.getStart());
        return zeile.getSpieltageszeit() == SpielTageszeit.SAMMSTAGNACHMITTAG && start.getHourOfDay() > 17;
    }

    private void removeUeberschuss(List<SpielZeile> zeilen) {

        List<SpielZeile> remove = new ArrayList<SpielZeile>();
        for (SpielZeile spielZeile : zeilen) {
            if (spielZeile.isPause()) {
                remove.add(spielZeile);
            } else {
                break;
            }
        }

        for (int i = zeilen.size() - 1; i > 0; i--) {
            if (zeilen.get(i).isPause()) {
                remove.add(zeilen.get(i));
            } else {
                break;
            }
        }

        spielzeilenRepo.delete(remove);

    }

}
