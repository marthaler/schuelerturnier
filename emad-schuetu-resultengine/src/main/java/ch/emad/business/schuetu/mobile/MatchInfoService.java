package ch.emad.business.schuetu.mobile;

import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.comperators.MannschaftsNamenComperator;
import ch.emad.persistence.schuetu.repository.KategorieRepository;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;
import ch.emad.model.schuetu.util.DateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Service fuer die Mobile Sites
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.2
 */
@Component
public class MatchInfoService {

    private List<String> mannschaftsNamen;

    private Map<String,String> finalSpielPaarungBekannt = new HashMap<String, String>();

    private Map<String,Long> mannschaftIdMap = new HashMap<String, Long>();

    @Autowired
    private MannschaftRepository mrepo;

    @Autowired
    private KategorieRepository krepo;

    public List<String> getMannschaftsNamen() {
        if (mannschaftsNamen == null) {
            updateMannschaftsNamen();
        }
        return mannschaftsNamen;
    }

    public String evaluateFinalSpielPaarungBekannt(String katName){
        if(finalSpielPaarungBekannt.size() < 1){
            updateFinalSpielPaarungBekannt();
        }
        return finalSpielPaarungBekannt.get(katName);
    }

    public String evaluateFinalSpielPaarungBekannt(Mannschaft mann){
        return evaluateFinalSpielPaarungBekannt(mann.getKategorie().getName());
    }

    public Long evaluateMannschaftId(String mann){
       if(mannschaftIdMap.isEmpty()){
           updateMannschaftIdMap();
       }
        return mannschaftIdMap.get(mann);
    }

    private void updateFinalSpielPaarungBekannt(){
        for(Kategorie kategorie : krepo.findAll()){
            Date start = kategorie.getLatestSpiel().getStart();
            DateTime startJ = new DateTime(start);
            startJ = startJ.plusMinutes(15);
            finalSpielPaarungBekannt.put(kategorie.getName(),DateUtil.getShortTimeDayString(startJ.toDate()));
        }
    }

    private void updateMannschaftIdMap(){
        for(Mannschaft mannschaft : mrepo.findAll()){
            mannschaftIdMap.put(mannschaft.getName(),mannschaft.getId());
        }
    }

    private void updateMannschaftsNamen() {
        List<String> res = new ArrayList<String>();
        res.add("Mannschaft wählen");
        List<Mannschaft> all = mrepo.findAll();
        Collections.sort(all, new MannschaftsNamenComperator());
        for (Mannschaft m : all) {
            res.add(m.getName().toUpperCase());
        }
        mannschaftsNamen = res;
    }


}
