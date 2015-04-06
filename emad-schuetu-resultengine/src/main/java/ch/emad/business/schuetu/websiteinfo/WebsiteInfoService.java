/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.websiteinfo;

import ch.emad.business.schuetu.dropbox.DropboxConnector;
import ch.emad.business.schuetu.websiteinfo.model.MannschaftsComperator;
import ch.emad.business.schuetu.controller.resultate.ResultateVerarbeiter;
import ch.emad.business.schuetu.websiteinfo.model.KlassenrangZeile;
import ch.emad.business.schuetu.websiteinfo.model.TeamGruppen;
import ch.emad.business.schuetu.websiteinfo.model.WebsiteInfoJahresDump;
import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielEinstellungen;
import ch.emad.model.schuetu.model.comperators.KategorieKlasseUndGeschlechtComperator;
import ch.emad.model.schuetu.model.enums.GeschlechtEnum;
import ch.emad.model.schuetu.model.util.XstreamUtil;
import ch.emad.persistence.schuetu.repository.KategorieRepository;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;
import ch.emad.persistence.schuetu.repository.SpielEinstellungenRepository2;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Zugriff auf die Mannschaften, Kategorien und Resultate fuer die Darstellung auf der Webseite
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.0
 */
@Component
public final class WebsiteInfoService {

    private static final Logger LOG = Logger.getLogger(WebsiteInfoService.class);

    private WebsiteInfoJahresDump jetzt = new WebsiteInfoJahresDump();

    private Map<String,WebsiteInfoJahresDump> alt = new HashMap<String,WebsiteInfoJahresDump>();

    private boolean init = false;

    @Autowired
    private MannschaftRepository mannschaftRepo;

    @Autowired
    private KategorieRepository kategorieRepository;

    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private DropboxConnector dropboxConnector;

    @Autowired
    private ResultateVerarbeiter resultate;

    @Autowired
    private SpielEinstellungenRepository2 eRepo;

    private String jahrFuerDump;

    public void dumpJetzt(String jahr){
        this.getFinalspiele("now");
        this.getGruppenspiele("now");
        this.getKnabenMannschaften("now",false);
        this.getMaedchenMannschaften("now",false);
        this.getRangliste("now");
        this.getEinstellungen("now");
        dropboxConnector.saveOldGame(jahr,XstreamUtil.serializeToString(jetzt));
    }

    public void dumpJetztVonSeite(){
        if(jahrFuerDump == null || jahrFuerDump.length() != 4){
            return;
        }
        dumpJetzt(jahrFuerDump);
    }

    private WebsiteInfoService() {

    }

    public Collection<String> getOldJahre(){
        // zum initialisieren des dumps
        if(!init){
            getOldJahredump("2011");
            init = true;
        }

        return this.alt.keySet();
    }

    public List<Spiel> getGruppenspiele(String jahr){

        if(jahr.length() == 4){
            return getOldJahredump(jahr).getGruppenspiele();
        }

        jetzt.setGruppenspiele(spielRepository.findGruppenSpielAsc());
        return jetzt.getGruppenspiele();
    }

    public SpielEinstellungen getEinstellungen(String jahr){

        if(jahr.length() == 4){
            return getOldJahredump(jahr).getEinstellung();
        }

        jetzt.setEinstellung(eRepo.getEinstellungen());
        return jetzt.getEinstellung();
    }

    public WebsiteInfoJahresDump getOldJahredump(String jahr ){
        WebsiteInfoJahresDump dump = alt.get(jahr);
        if(dump == null){
            Map<String,String> str = dropboxConnector.loadOldGames();
            for(String key : str.keySet()){
                this.alt.put(key, (WebsiteInfoJahresDump) XstreamUtil.deserializeFromString(str.get(key)));
            }
        } else{
            return dump;
        }
        WebsiteInfoJahresDump dump2 = alt.get(jahr);
        if(dump2 != null){
            return dump2;
        }
        return dump;
    }

    public List<Spiel> getFinalspiele(String jahr){

        if(jahr.length() == 4){
            return getOldJahredump(jahr).getFinalspiele();
        }

        jetzt.setFinalspiele(spielRepository.findFinalSpielAsc());
        return jetzt.getFinalspiele();

    }

    public List<KlassenrangZeile> getRangliste(String jahr){
        if(jahr.length() == 4){
            return getOldJahredump(jahr).getRangliste();
        }
        this.jetzt.setRangliste(this.resultate.getRanglisteModel());
        return jetzt.getRangliste();
    }

    public List<TeamGruppen> getKnabenMannschaften(String jahr, boolean ganzeliste) {

        if(jahr.length() == 4){
            return getOldJahredump(jahr).getKnabenMannschaften();
        }

        if (ganzeliste) {
            return convertToGanzeGruppe(mannschaftRepo.findAll(), true);
        }
        return convertToKategorienGruppen(kategorieRepository.findAll(), true);

    }

    public List<TeamGruppen> getMaedchenMannschaften(String jahr, boolean ganzeliste) {

        if(jahr.length() == 4){
            return getOldJahredump(jahr).getMaedchenMannschaften();
        }

        if (ganzeliste) {
            return convertToGanzeGruppe(mannschaftRepo.findAll(), false);
        }
        return convertToKategorienGruppen(kategorieRepository.findAll(), false);
    }

    private List<TeamGruppen> convertToKategorienGruppen(List<Kategorie> kategorien, boolean knaben) {
        Collections.sort(kategorien, new KategorieKlasseUndGeschlechtComperator());
        List<Kategorie> maedchen = new ArrayList<Kategorie>();
        List<Kategorie> kanben = new ArrayList<Kategorie>();
        List<Kategorie> list = new ArrayList<Kategorie>();

        List<TeamGruppen> result = new ArrayList<TeamGruppen>();

        for (Kategorie k : kategorien) {
            if (k.getMannschaften().get(0).getGeschlecht() == GeschlechtEnum.K) {
                kanben.add(k);
            } else {
                maedchen.add(k);
            }
        }
        if (knaben) {
            list = kanben;
        } else {
            list = maedchen;
        }

        for (Kategorie m : list) {

            TeamGruppen gr = new TeamGruppen();
            gr.setName("Gruppe: " + m.getName());
            this.mannschaftenKonvertierenUndEinfuellen(m.getMannschaften(), gr);
            result.add(gr);
        }

        if(knaben){
            jetzt.setKnabenMannschaften(result);
        } else{
            jetzt.setMaedchenMannschaften(result);
        }

        return result;
    }

    private List<TeamGruppen> convertToGanzeGruppe(List<Mannschaft> mannschaften, boolean knaben) {

        List<Mannschaft> maedchen = new ArrayList<Mannschaft>();
        List<Mannschaft> kanben = new ArrayList<Mannschaft>();

        for (Mannschaft m : mannschaften) {
            if (m.getGeschlecht() == GeschlechtEnum.K) {
                kanben.add(m);
            } else {
                maedchen.add(m);
            }
        }

        TeamGruppen result = new TeamGruppen();
        if (!knaben) {
            mannschaftenKonvertierenUndEinfuellen(maedchen, result);
            result.setName("Die Mädchenteams");
        } else {
            mannschaftenKonvertierenUndEinfuellen(kanben, result);
            result.setName("Die Knabenteams");
        }
        List<TeamGruppen> reslist = new ArrayList<TeamGruppen>();
        reslist.add(result);
        return reslist;
    }

    private void mannschaftenKonvertierenUndEinfuellen(List<Mannschaft> mannschaften, TeamGruppen result) {
        for (Mannschaft m : mannschaften) {
            ch.emad.business.schuetu.websiteinfo.model.Mannschaft ma = new ch.emad.business.schuetu.websiteinfo.model.Mannschaft();
            ma.setNummer(m.getName());
            ma.setBegleitperson(m.getBegleitpersonName());
            ma.setCaptain(m.getCaptainName());
            ma.setKlassenname(m.getKlassenBezeichnung());
            ma.setKlasse("" + m.getKlasse());
            ma.setSpieler(m.getAnzahlSpieler());
            ma.setSchulhaus(m.getSchulhaus());

            result.addMannschaft(ma);
            result.setTotal(result.getTotal() + ma.getSpieler());
        }

        // sortieren nach klasse
        Collections.sort(result.getMannschaften(), new MannschaftsComperator());
    }

    public String getJahrFuerDump() {
        return jahrFuerDump;
    }

    public void setJahrFuerDump(String jahrFuerDump) {
        this.jahrFuerDump = jahrFuerDump;
    }
}