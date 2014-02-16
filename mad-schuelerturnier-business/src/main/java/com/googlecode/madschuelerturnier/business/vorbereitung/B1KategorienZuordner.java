/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.model.Gruppe;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.model.enums.SpielTageszeit;
import com.googlecode.madschuelerturnier.persistence.repository.GruppeRepository;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class B1KategorienZuordner {

    private static final Logger LOG = Logger.getLogger(B1KategorienZuordner.class);

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private GruppeRepository gruppeRepo;

    @Autowired
    private KategorieRepository kategorieRepo;

    @Autowired
    private Business business;

    public Map<String, Kategorie> automatischeZuordnung() {

        // 1
        // jede Mannschaft wird der Gruppe A ihrer Kategorie zugeordent
        Map<String, Kategorie> map = zuordnungVornehmen(business.getMannschaften());

        // 2
        // gruppen mit nur einer oder 2 Mannschaften werden der naechst hoeheren
        // kategorie zugeordnet
        // im falle, das die naechste kategorie nun mehr als 7 mannschaften hat
        // oder nicht vorhanden ist
        // wird versucht eine zuordnung zur tieferen
        // kategorie zu realisieren

        Set<String> keys = map.keySet();
        List<String> mKeys = new ArrayList<String>();
        List<String> kKeys = new ArrayList<String>();

        for (String key : keys) {
            if (key.contains("M")) {
                mKeys.add(key);
            } else {
                kKeys.add(key);
            }
        }

        // knaben
        Collections.sort(kKeys);
        zuKleineGruppenInAndereKategorieSchieben(map, kKeys);

        // maedchen
        Collections.sort(mKeys);
        zuKleineGruppenInAndereKategorieSchieben(map, mKeys);

        for (Kategorie kat : map.values()) {

            //Kategorie kategorie = kat;
            Kategorie kategorie = kategorieRepo.findOne(kat.getId());
            Gruppe gruppe = gruppeRepo.findOne(kategorie.getGruppeA().getId());

            String hintS = null;

            List<Mannschaft> m = kategorie.getGruppeA().getMannschaften();

            for (Mannschaft mann : m) {

                // SpielwunschHint beruecksichtigen, beispielsweise aus der Import Liste
                if (mann.getSpielWunschHint() != null && !mann.getSpielWunschHint().isEmpty()) {
                    String hint = mann.getSpielWunschHint().toLowerCase();

                    if (hint.contains("onn")) {
                        kategorie.setSpielwunsch(SpielTageszeit.SONNTAGMORGEN);
                        hintS = "sonntag";
                    }

                    if (hint.contains("orge")) {
                        kategorie.setSpielwunsch(SpielTageszeit.SAMSTAGMORGEN);
                        hintS = "morgen";
                    }

                    if (hint.contains("nach")) {
                        kategorie.setSpielwunsch(SpielTageszeit.SAMMSTAGNACHMITTAG);
                        hintS = "nachmittag";
                    }

                }

                mann.setGruppe(kategorie.getGruppeA());
                // todo pruefon ob persistence noch funktioniert

            }



            if (hintS != null) {
                for (Mannschaft mann : m) {
                    mann.setSpielWunschHint(hintS);
                    // todo pruefon ob persistence noch funktioniert

                }
            }
            mannschaftRepo.save(m);
            this.kategorieRepo.save(kategorie);
        }




        return map;
    }

    private void zuKleineGruppenInAndereKategorieSchieben(Map<String, Kategorie> map, List<String> kKeys) {
        for (int i = 0; i < kKeys.size(); i++) {
            String keyActual = kKeys.get(i);
            Kategorie temp = kategorieRepo.findOne(map.get(keyActual).getId());

            if (temp == null || temp.getGruppeA() == null || temp.getGruppeA().getMannschaften().size() == 0) {
                map.remove(keyActual);
            }

            boolean moved = false;

            if (temp.getGruppeA().getMannschaften().size() < 3) {
                String keyPlusOne = "";
                try {
                    keyPlusOne = kKeys.get(i + 1);
                } catch (IndexOutOfBoundsException e) {
                    LOG.debug("es wurde versucht auf eine hoehere kategorie zuzugreifen, obwohl keine mehr existiert: " + keyActual + " +1");
                }
                Kategorie kategoriePlusOne = null;
                if (map.get(keyPlusOne) != null) {
                    kategoriePlusOne = kategorieRepo.findOne(map.get(keyPlusOne).getId());
                }
                if (kategoriePlusOne != null && kategoriePlusOne.getGruppeA().getMannschaften().size() > 0) {

                    moved = plusOneBigger(map, keyActual, temp, kategoriePlusOne);
                }

                if (!moved) {

                    String keyMinusOne;
                    try {
                        keyMinusOne = kKeys.get(i - 1);
                    } catch (IndexOutOfBoundsException e) {
                        LOG.debug("es wurde versucht auf eine tiefere kategorie zuzugreifen, obwohl keine mehr existiert: " + keyActual + " -1");
                        continue;

                    }
                    Kategorie kategorieMinusOne = kategorieRepo.findOne(map.get(keyMinusOne).getId());
                    if (kategorieMinusOne != null && kategorieMinusOne.getGruppeA().getMannschaften().size() > 0) {


                        List<Mannschaft> manns = temp.getGruppeA().getMannschaften();
                        for (Mannschaft mannschaft : manns) {
                            mannschaft.setGruppe(kategorieMinusOne.getGruppeA());
                            kategorieMinusOne.getGruppeA().getMannschaften().add(mannschaft);
                        }

                        Collections.sort(kategorieMinusOne.getGruppeA().getMannschaften(), new MannschaftsNamenComperator());
                        map.remove(keyActual);

                       for( Mannschaft mt : temp.getGruppeA().getMannschaften()){
                           mt.setGruppe(null);
                           this.mannschaftRepo.save(mt);
                       }

                        for( Mannschaft mt : temp.getGruppeB().getMannschaften()){
                            mt.setGruppe(null);
                            this.mannschaftRepo.save(mt);
                        }

                   // todo vereinfachen
                        /*
                        temp.getGruppeA().setMannschaften(null);
                        temp.getGruppeA().setKategorie(null);
                        temp.getGruppeB().setKategorie(null);

                        gruppeRepo.delete(temp.getGruppeA());
                        gruppeRepo.delete(temp.getGruppeB());

                        temp.getGruppeB().setMannschaften(null);
                        LOG.info("vor sichern der unnoetigen kategorie ");
                        temp = kategorieRepo.saveAndFlush(temp);
                        List <Mannschaft> mm = temp.getMannschaften();
                        LOG.info("mannschaften: " + mm.size());
*/
                        // todo vereinfachen -> hier anchauen
                        kategorieRepo.delete(temp);
                        kategorieRepo.save(kategorieMinusOne);
                    }
                }
            }
        }
    }

    private boolean plusOneBigger(Map<String, Kategorie> map, String keyActual, Kategorie temp, Kategorie kategoriePlusOne) {
        boolean moved;
        List<Mannschaft> manns = temp.getGruppeA().getMannschaften();
        for (Mannschaft mannschaft : manns) {
            mannschaft.setGruppe(kategoriePlusOne.getGruppeA());
            kategoriePlusOne.getGruppeA().getMannschaften().add(mannschaft);
        }

        Collections.sort(kategoriePlusOne.getGruppeA().getMannschaften(), new MannschaftsNamenComperator());
        map.remove(keyActual);
        moved = true;

        Gruppe a = temp.getGruppeA();
        Gruppe b = temp.getGruppeB();

        a.setKategorie(null);
        b.setKategorie(null);

        a = gruppeRepo.save(a);
        b = gruppeRepo.save(b);

        temp.setGruppeA(null);
        temp.setGruppeB(null);

        gruppeRepo.save(a);
        gruppeRepo.save(b);

        kategorieRepo.save(temp);



        gruppeRepo.delete(a.getId());
        gruppeRepo.delete(b.getId());

        kategorieRepo.save(kategoriePlusOne);

        return moved;
    }

    private Map<String, Kategorie> zuordnungVornehmen(List<Mannschaft> mannschaften) {

        Map<String, Kategorie> map = new HashMap<String, Kategorie>();

        for (Mannschaft mannschaft : mannschaften) {

            String key = "" + mannschaft.getGeschlecht() + mannschaft.getKlasse();
            Kategorie tempKategorie = map.get(key);

            // neu, wenn noch nicht vorhanden
            if (tempKategorie == null) {
                tempKategorie = new Kategorie();
                tempKategorie = this.kategorieRepo.save(tempKategorie);

                // Gruppe A
                Gruppe tempGr = new Gruppe();
                // wichtig fuer die suche nach kategorien
                tempGr.setGeschlecht(mannschaft.getGeschlecht());
                tempGr = this.gruppeRepo.save(tempGr);
                tempGr.setKategorie(tempKategorie);

                tempKategorie.setGruppeA(tempGr);
                tempKategorie = this.kategorieRepo.save(tempKategorie);

                gruppeRepo.save(tempGr);
                // Gruppe B
                Gruppe tempGrB = new Gruppe();
                // wichtig fuer die suche nach kategorien
                tempGrB.setGeschlecht(mannschaft.getGeschlecht());
                tempGrB = this.gruppeRepo.save(tempGrB);
                tempGrB.setKategorie(tempKategorie);
                tempKategorie.setGruppeB(tempGrB);
                tempKategorie = this.kategorieRepo.save(tempKategorie);

                gruppeRepo.save(tempGrB);

            }

            // zuordnung
            tempKategorie.getGruppeA().getMannschaften().add(mannschaftRepo.findOne(mannschaft.getId()));
            LOG.info(mannschaft);
            tempKategorie = this.kategorieRepo.save(tempKategorie);

            map.put(key, tempKategorie);
        }
        return map;
    }
}
