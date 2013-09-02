package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.impl.Business;
import com.googlecode.madschuelerturnier.business.xls.FromXLS;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

@Component
public class FileUploadHandler {

    private static final Logger LOG = Logger.getLogger(FileUploadHandler.class);

    @Autowired
    private FromXLS xls;

    @Autowired
    private MannschaftRepository repo;

    @Autowired
    private SpielRepository srepo;

    @Autowired
    private Business erepo;

    public void handleFileUpload(FileUploadEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("feedback", new FacesMessage("Upload ok!", "->" + event.getFile().getFileName()));

        // Einstellungen sichern
        SpielEinstellungen einstellungen = xls.convertXLSToEinstellung(event.getFile().getContents());
        erepo.saveEinstellungen(einstellungen);
        LOG.info("einstellungen gespeicher: " + einstellungen);

        // Mannschaften laden und updaten
        List<Mannschaft> mannschaftsliste = xls.convertXLSToMannschaften(event.getFile().getContents());
        for (Mannschaft m : mannschaftsliste) {
            repo.save(m);
            LOG.info("mannschaft gespeicher: " + m);
        }

        // Spiele laden und updaten
        List<Spiel> spiele = xls.convertXLSToSpiele(event.getFile().getContents());
        for (Spiel s : spiele) {
            srepo.save(s);
            LOG.info("spiel gespeicher: " + s);
        }
    }
}