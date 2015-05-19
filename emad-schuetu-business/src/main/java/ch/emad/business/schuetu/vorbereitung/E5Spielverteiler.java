/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.vorbereitung;

import ch.emad.business.schuetu.Business;
import ch.emad.business.schuetu.vorbereitung.helper.SpielverteilerHelper;
import ch.emad.business.schuetu.vorbereitung.helper.SpielzeilenValidator;
import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielZeile;
import ch.emad.model.schuetu.model.comperators.KategorieKlasseUndGeschlechtComperator;
import ch.emad.model.schuetu.model.enums.PlatzEnum;
import ch.emad.model.schuetu.model.enums.SpielTageszeit;
import ch.emad.persistence.schuetu.repository.KategorieRepository;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import ch.emad.persistence.schuetu.repository.SpielZeilenRepository;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
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
    private KategorieRepository katRepo;

    @Autowired
    private SpielzeilenValidator val;

    @Autowired
    private SpielverteilerHelper helper;

    @Autowired
    private Business business;


    public String spieleAutomatischVerteilen() {

        StringBuilder result = new StringBuilder();

        behandleFinalspielzeilen();

        SpielZeile vorherVorher = null;

        SpielZeile vorher = null;

        List<SpielZeile> gruppenSpieleZeilen = spielzeilenRepo.findGruppenSpielZeilen();

        List<Spiel> gruppenSpiele = spielRepo.findGruppenSpiel();

        helper.init(gruppenSpiele);

        for (SpielZeile zeileJetzt : gruppenSpieleZeilen) {

            if (zeileJetzt.isPause() && !isSamstagNachNeuekategoriesperre(zeileJetzt)) {
                vorherVorher = vorher;
                vorher = zeileJetzt;
                continue;
            }

            stopA(vorher, vorherVorher, gruppenSpiele, zeileJetzt);

            stopB(vorher, vorherVorher, gruppenSpiele, zeileJetzt);

            stopC(vorher, vorherVorher, gruppenSpiele, zeileJetzt);

            vorherVorher = vorher;

            vorher = spielzeilenRepo.save(zeileJetzt);

        }


        LOG.info("spiele verteilt ");
        if (gruppenSpiele.size() > 0) {
            result.append("NICHT ZUGEORDNETE SPIELE !!! " + gruppenSpiele.size());
            result.append("NICHT ZUGEORDNETE SPIELE --> " + gruppenSpiele);
            LOG.fatal(result.toString());
        }

        LOG.info("werde jetzt ueberzaehlige pausen loeschen");
        // sichern und Anfang und Schluss Pausen entfernen
        List<SpielZeile> zeilenSo = business.getSpielzeilen(true);
        List<SpielZeile> zeilenSa = business.getSpielzeilen(false);
        removeUeberschuss(zeilenSo);
        removeUeberschuss(zeilenSa);
        return result.toString();
    }

    private void stopA(SpielZeile vorher, SpielZeile vorherVorher, List<Spiel> gruppenSpiele, SpielZeile zeilen) {
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

            String ret = "";
            ret = val.validateSpielZeilen(vorher, vorherVorher, zeilen);

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

    private void stopC(SpielZeile vorher, SpielZeile vorherVorher, List<Spiel> gruppenSpiele, SpielZeile zeilen) {
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

            String ret = "";
            ret = val.validateSpielZeilen(vorher, vorherVorher, zeilen);

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

    private void stopB(SpielZeile vorher, SpielZeile vorherVorher, List<Spiel> gruppenSpiele, SpielZeile zeilen) {
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

            String ret = "";
            ret = val.validateSpielZeilen(vorher, vorherVorher, zeilen);

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

        List<Kategorie> kategorien = katRepo.findAll();
        Collections.sort(kategorien, new KategorieKlasseUndGeschlechtComperator());

        List<SpielZeile> finalspieleZeilen = spielzeilenRepo.findFinalSpielZeilen();
        List<SpielZeile> finalspieleZeilenGeaendert = new ArrayList<SpielZeile>();
        List<Spiel> spieleGeaendert = new ArrayList<Spiel>();

        boolean first = true;
        Spiel letztesFinale = null;

        for (Kategorie k : kategorien) {

            SpielZeile zeile = finalspieleZeilen.remove(0);

            if (first) {
                mergeKleinerFinalToSpielzeile(spieleGeaendert, k, zeile);
                first = false;
            } else {
                mergeKleinerFinalToSpielzeile(spieleGeaendert, k, zeile);
                mergeGrosserFinalToSpielzeile(spieleGeaendert, zeile, letztesFinale);
            }

            letztesFinale = k.getGrosserFinal();
            finalspieleZeilenGeaendert.add(zeile);

        }

        SpielZeile zeile = finalspieleZeilen.remove(0);
        mergeGrosserFinalToSpielzeile(spieleGeaendert, zeile, letztesFinale);
        finalspieleZeilenGeaendert.add(zeile);

        spielRepo.save(spieleGeaendert);
        spielzeilenRepo.save(finalspieleZeilenGeaendert);

    }

    private void mergeKleinerFinalToSpielzeile(List<Spiel> spieleGeaendert, Kategorie k, SpielZeile zeile) {
        // kleiner finale
        if (k.getKleineFinal() != null) {
            zeile.setB(k.getKleineFinal());
            k.getKleineFinal().setPlatz(PlatzEnum.B);
            k.getKleineFinal().setStart(zeile.getStart());

            spieleGeaendert.add(k.getKleineFinal());
        }
    }

    private void mergeGrosserFinalToSpielzeile(List<Spiel> spieleGeaendert, SpielZeile zeile, Spiel grosserFinal) {
        // grosser finale
        zeile.setA(grosserFinal);
        grosserFinal.setPlatz(PlatzEnum.A);
        grosserFinal.setStart(zeile.getStart());

        spieleGeaendert.add(grosserFinal);
    }

    private void behandleFinalspielzeilenAlt() {
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

    private Spiel getNextSpiel(SpielZeile zeile, List<Spiel> gruppenSpiele, int iB) {      // NOSONAR

        Spiel firstEgal = null;
        int ieg = 0;
        int ineg = 0;
        for (Spiel spiel : gruppenSpiele) {

            if (firstEgal == null && spiel.getMannschaftA().getKategorie().getSpielwunsch() == SpielTageszeit.EGAL) {

                if (ieg == iB) {

                    // falls bereits spaeter als 16 uhr am Samstag, keine neue Kategorie mehr verplanen
                    if (isSamstagNachNeuekategoriesperre(zeile)) {
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

                    // falls bereits spaeter als 16 uhr am Samstag, keine neue Kategorie mehr verplanen
                    if (isSamstagNachNeuekategoriesperre(zeile)) {
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


    private boolean isSamstagNachNeuekategoriesperre(SpielZeile zeile) {
        DateTime start = new DateTime(zeile.getStart());
        return zeile.getSpieltageszeit() == SpielTageszeit.SAMSTAGNACHMITTAG && start.getHourOfDay() > 17;
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
