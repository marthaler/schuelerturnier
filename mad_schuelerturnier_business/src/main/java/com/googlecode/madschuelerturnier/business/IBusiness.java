/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.spiel.Penalty;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface IBusiness {

    List<String> getSchulhausListe(String query);

    List<String> getPersonenListe(String query);

    List<Mannschaft> getMannschaften();

    List<Kategorie> getKategorien();

    SpielEinstellungen getSpielEinstellungen();

    SpielEinstellungen saveEinstellungen(SpielEinstellungen einstellungen);

    void saveVertauschungen(String vertauschungen);

    List<Kategorie> getKategorienMList();

    List<Kategorie> getKategorienKList();

    void toggleSpielwunschOnKategorie(Long id);

    void startClock();

    void stopClock();

    /**
     * funktioniert nur, wenn eine effektive verspaetung vorhanden ist
     */
    void spielzeitEinholen(int seconds);

    String spielzeitVerspaetung();

    void resumeSpiel();

    List<Penalty> anstehendePenalty();

    void penaltyEintragen(List<Penalty> list);

    List<Penalty> gespieltePenalty();

    List<Penalty> eingetragenePenalty();

    boolean isDBInitialized();

    void initializeDB();


}
