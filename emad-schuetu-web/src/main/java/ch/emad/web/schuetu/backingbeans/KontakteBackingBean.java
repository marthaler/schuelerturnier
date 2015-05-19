/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu.backingbeans;

import ch.emad.business.schuetu.pdf.JasperResultConverter;
import ch.emad.business.schuetu.websiteinfo.WebsiteInfoService;
import ch.emad.business.schuetu.websiteinfo.model.WebsiteInfoJahresDump;
import ch.emad.business.schuetu.webstamp.WebstampService;
import ch.emad.model.schuetu.interfaces.RechnungReportable;
import ch.emad.model.schuetu.model.Kontakt;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.persistence.schuetu.repository.KontaktRepository;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Backing Bean fuer den Dropbox Verbindungsaufbau
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component(value = "kontakteBB")
@Scope("session")
public class KontakteBackingBean implements Serializable{

    @Autowired
    private JasperResultConverter converter;

    @Autowired
    WebstampService stampS;

    @Autowired
    private WebsiteInfoService service;

    @Autowired
    KontaktRepository repo;

    @Autowired
    MannschaftRepository repoMannschaft;

    public String selectedList ="%%%";
    public String selectedRessor ="%%%";

    private Kontakt selected;

    public boolean areCouvertOk(){
        if(stampS.countAvailableStamps() < getAll().size()){
            return false;
        }
       return true;
    }

    public String couvertTitel(){
        if(! areCouvertOk()){
            return "Zu wenig Marken vorhanden: " + stampS.countAvailableStamps() +" Stück, benötigt: "+ getAll().size();
        }
        return "Marken drucken, vorhanden: " + stampS.countAvailableStamps()  +" Stück, benötigt: "+ getAll().size();
    }

    public byte[] downloadCouverts(){
        List<RechnungReportable> list = getAll();
        for(RechnungReportable rep : list){
            rep.setStamp(stampS.getNextStamp());
        }
        return converter.createPdf(list,"couvert2");
    }

    public byte[] downloadCouverts(List<RechnungReportable> rechungen){
        for(RechnungReportable rep : rechungen){
            rep.setStamp(stampS.getNextStamp());
        }
        return converter.createPdf(rechungen,"couvert2");
    }

    public byte[] download10Stamps(){
        ArrayList<RechnungReportable> stamps = new ArrayList<>();

        for(int i = 0; i<10;i++){
            stamps.add(new Mannschaft());
        }
        for(RechnungReportable rep : stamps){
            rep.setStamp(stampS.getNextStamp());
        }
        return converter.createPdf(stamps,"marke");
    }

    public List<String> getAllLists(){
        List<String> ret = new ArrayList<>();
        for(String re : service.getOldJahre()){
            ret.add("mannschaften-" + re);
        }
        ret.add("mannschaften-aktuell");
        ret.addAll(repo.slectAllLists());
        return ret ;
    }

    public List<String> getAllRessorts(){
        return repo.slectAllRessorts();
    }

    public void newOrFindOne(String id){
        if(id == null || id.isEmpty()){
            selected = new Kontakt();
            return;
        }
        selected = repo.findOne(Long.parseLong(id));
    }

    public List<RechnungReportable> getAll(){

        // aktuelle mannschaften
        Set<RechnungReportable> result = new HashSet<>();
        if(this.selectedList.contains("mannschaften-aktuell")){
            for(Mannschaft m :repoMannschaft.findAll()){
                result.add(m);
            }
            return new ArrayList<RechnungReportable>(result);
        }
        // mannschaften aus dumps
        if(this.selectedList.contains("annschaften-")){
            String list = this.selectedList;
            WebsiteInfoJahresDump dump =  service.getOldJahredump(list.split("-")[1]);
            List<Spiel> spiele = dump.getGruppenspiele();
            for(Spiel s : spiele){
                result.add(s.getMannschaftA());
                result.add(s.getMannschaftB());
            }
            return new ArrayList<RechnungReportable>(result);
        }

        String li =  this.selectedList.substring(1,this.selectedList.length()-1) ;
          String re = this.selectedRessor.substring(1,this.selectedRessor.length()-1) ;

        for(Kontakt k :repo.findFiltredKontakteRessor("%" + re + "%", "%" + li + "%")){
            result.add(k);
        }

        return new ArrayList<RechnungReportable>(result);
    }

    public Kontakt getSelected() {
        return selected;
    }

    public void setSelected(Kontakt selected) {
        this.selected = selected;
    }

    public String getSelectedRessor() {
        return selectedRessor;
    }

    public void setSelectedRessor(String selectedRessor) {
        this.selectedRessor = selectedRessor;
    }


    public String getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(String selectedList) {
        this.selectedList = selectedList;
    }

    public void save(){
        selected = repo.save(selected);
    }

}