package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.turnierimport.ImportHandler;
import com.googlecode.madschuelerturnier.business.xls.FromXLSLoader;
import com.googlecode.madschuelerturnier.model.Korrektur;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.helper.SpielEinstellungen;
import com.googlecode.madschuelerturnier.persistence.repository.KorrekturRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class FileUploadController {

    private static final Logger LOG = Logger.getLogger(FileUploadController.class);

    @Autowired
    private FromXLSLoader xls;

    @Autowired
    private MannschaftRepository mannschaftsRepo;

    @Autowired
    private KorrekturRepository korrekturRepository;

    @Autowired
    private Business busniess;

    @Autowired
    private ImportHandler importHandler;

    public void handleFileUpload(FileUploadEvent event) {

        // Einstellungen sichern
        SpielEinstellungen einstellungen = xls.convertXLSToEinstellung(event.getFile().getContents());
        busniess.saveEinstellungen(einstellungen);
        LOG.info("einstellungen gespeicher: " + einstellungen);

        // Mannschaften laden und updaten
        List<Mannschaft> mannschaftsliste = xls.convertXLSToMannschaften(event.getFile().getContents());
        for (Mannschaft m : mannschaftsliste) {
            mannschaftsRepo.save(m);
            LOG.info("mannschaft gespeicher: " + m);
        }

        // Korrekturen laden und updaten
        List<Korrektur> korrekturen = xls.convertXLSToKorrektur(event.getFile().getContents());
        for (Korrektur k : korrekturen) {
            korrekturRepository.save(k);
            LOG.info("korrektur gespeicher: " + k);
        }

        // Spiele laden und updaten
        List<Spiel> spiele = xls.convertXLSToSpiele(event.getFile().getContents());
        LOG.info("spiele geladen: " + spiele.size());

        importHandler.turnierHerstellen(spiele);

    }

}