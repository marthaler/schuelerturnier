/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business;

import com.googlecode.mad_schuelerturnier.model.Kategorie;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.model.spiel.Penalty;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface IBusiness {

    public abstract List<String> getSchulhausListe(String query);

    public abstract List<String> getPersonenListe(String query);

    public abstract List<Mannschaft> getMannschaften();

    public abstract List<Kategorie> getKategorien();

    public abstract SpielEinstellungen getSpielEinstellungen();

    public abstract SpielEinstellungen saveEinstellungen(SpielEinstellungen einstellungen);

    public abstract void saveVertauschungen(String vertauschungen);

    public abstract List<Kategorie> getKategorienMList();

    public abstract List<Kategorie> getKategorienKList();

    public abstract void toggleSpielwunschOnKategorie(Long id);

    public void startClock();

    public void stopClock();

    /**
     * funktioniert nur, wenn eine effektive verspaetung vorhanden ist
     */
    public void spielzeitEinholen(int seconds);

    public String spielzeitVerspaetung();

    public void resumeSpiel();

    public List<Penalty> anstehendePenalty();

    public void penaltyEintragen(List<Penalty> list);

    public List<Penalty> gespieltePenalty();

    public List<Penalty> eingetragenePenalty();

    public boolean isDBInitialized();

    public void initializeDB();


}
