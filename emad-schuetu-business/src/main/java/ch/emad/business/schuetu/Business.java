/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu;

import ch.emad.model.schuetu.model.*;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface Business {

    List<String> getSchulhausListe(String query);

    List<String> getPersonenListe(String query);

    List<String> getEmailsListe(String query);

    List<Mannschaft> getMannschaften();

    List<Kategorie> getKategorien();

    SpielEinstellungen getSpielEinstellungen();

    SpielEinstellungen saveEinstellungen(SpielEinstellungen einstellungen);

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

    void initializeDropbox(String file);

    void initZeilen(boolean sonntag);

    List<SpielZeile> getSpielzeilen(final boolean sonntag);

    void manuelleZuordnungDurchziehen(final String mannschaftName, final String zielKategorieKey);

    void generateSpielFromXLS(byte[] xlsIn);

    void updateAutocompletesMannschaften(List<Mannschaft> mannschaften);

    void updateAutocompletesMannschaft(Mannschaft mannschaft);

}
