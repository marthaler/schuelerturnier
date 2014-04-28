/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.websiteinfo.WebsiteInfoService;
import com.googlecode.madschuelerturnier.business.websiteinfo.model.Mannschaft;
import com.googlecode.madschuelerturnier.business.websiteinfo.model.MannschaftsComperator;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.web.SpielstatusWebBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller fuer die Webseiteninformationen
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component
public class MobileController {

    private static final Logger LOG = Logger.getLogger(MobileController.class);

    private String mannschaft;

@Autowired
private MannschaftRepository repo;


    public List<String> getMannschaften(){

        List<com.googlecode.madschuelerturnier.model.Mannschaft> mannschaften = repo.findAll();
        List<String> list = new ArrayList<String>();
for(com.googlecode.madschuelerturnier.model.Mannschaft m : mannschaften){
list.add("Für Mannschaft: " + m.getName());
    LOG.info("getmannschaft: " + mannschaft);

}
        return list;
    }

    public void setMannschaft(String mannschaft){
        this.mannschaft = mannschaft.replace("Für Mannschaft: ","");
    }

    public String getMannschaft(){
        LOG.info("getmannschaft: " + mannschaft);
        return mannschaft;
    }

    public List<Spiel> getSpiele( ){
        if(findMannschaft(this.mannschaft) == null){
            return new ArrayList<Spiel>();
        }
        return findMannschaft(this.mannschaft).getGruppe().getSpiele();
    }

    private com.googlecode.madschuelerturnier.model.Mannschaft findMannschaft(String id){

        if(id == null){
            return null;
        }

        List<com.googlecode.madschuelerturnier.model.Mannschaft> mannschaften = repo.findAll();
        List<String> list = new ArrayList<String>();
        for(com.googlecode.madschuelerturnier.model.Mannschaft m : mannschaften){
            if(id.equals(m.getName())){
                return m;
            }

        }
        return null;
    }

}
