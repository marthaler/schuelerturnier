package com.googlecode.mad_schuelerturnier.web;

import com.googlecode.mad_schuelerturnier.business.xls.FromXLS;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielEinstellungenRepository;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
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
    FromXLS xls;

    @Autowired
    MannschaftRepository repo;

    @Autowired
    SpielRepository srepo;

    @Autowired
    SpielEinstellungenRepository erepo;

    public void handleFileUpload(FileUploadEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("feedback", new FacesMessage("Upload ok!", "->" + event.getFile().getFileName()));

        // try {

        List<Mannschaft> mannschaftsliste = xls.convertXLSToMannschaften(event.getFile().getContents());

        for (Mannschaft m : mannschaftsliste) {
            repo.save(m);
            LOG.info("mannschaft gespeicher: " + m);
        }

        List<Spiel> spiele = xls.convertXLSToSpiele(event.getFile().getContents());

        for (Spiel s : spiele) {
            srepo.save(s);
            LOG.info("spiel gespeicher: " + s);
        }

        SpielEinstellungen e = xls.convertXLSToEinstellung(event.getFile().getContents());

        erepo.save(e);
        LOG.info("einstellungen gespeicher: " + e);

        //FileUtils.writeByteArrayToFile(new File("/" + event.getFile().getFileName()), event.getFile().getContents());

    }
}