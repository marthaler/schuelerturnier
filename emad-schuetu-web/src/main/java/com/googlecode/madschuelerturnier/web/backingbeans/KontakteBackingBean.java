/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.backingbeans;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.dropbox.DropboxStarter;
import com.googlecode.madschuelerturnier.model.Kontakt;
import com.googlecode.madschuelerturnier.persistence.repository.KontaktRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Backing Bean fuer den Dropbox Verbindungsaufbau
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component(value = "kontakteBB")
@Scope("session")
public class KontakteBackingBean implements Serializable{

    public String selectedList ="%%%";

    public String selectedRessor ="%%%";


    private Kontakt selected;

    @Autowired
    KontaktRepository repo;

    public List<String> getAllLists(){
        return repo.slectAllLists();
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

    public List<Kontakt> getAll(){

        String li =  this.selectedList.substring(1,this.selectedList.length()-1) ;
        String re = this.selectedRessor.substring(1,this.selectedRessor.length()-1) ;

        return repo.findFiltredKontakteRessor("%"+re+"%","%"+li+"%");
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