/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.impl;

import com.googlecode.mad_schuelerturnier.business.ImportExportBusiness;
import com.googlecode.mad_schuelerturnier.business.dataloader.CVSSpielParser;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class ImportExportBusinessImpl implements ImportExportBusiness {

    private static final Logger LOG = Logger.getLogger(ImportExportBusinessImpl.class);

    @Autowired
    private CVSSpielParser cvsParser;

    @Autowired
    private SpielRepository spielRepo;

    public ImportExportBusinessImpl() {

    }


    public List<Spiel> loadAllSpiele() {
        return spielRepo.findAll();
    }


   
    public void updateSpiele(String spiele, boolean bestaetigt, boolean eingetraegen) {
        List<Spiel> sp = cvsParser.parseFileContent(spiele);

        List<Spiel> spDb = spielRepo.findAll();
        Map<String, Spiel> spieleMap = new HashMap<String, Spiel>();
        for (Spiel spiel : spDb) {
            spieleMap.put(spiel.getIdString(), spiel);
        }

        for (Spiel spiel : sp) {
            Spiel orig = spieleMap.get(spiel.getIdString());
            if (eingetraegen) {
                orig.setToreA(spiel.getToreABestaetigt());
                orig.setToreB(spiel.getToreBBestaetigt());
            }

            if (bestaetigt) {
                orig.setToreABestaetigt(spiel.getToreABestaetigt());
                orig.setToreBBestaetigt(spiel.getToreBBestaetigt());
            }
            spielRepo.save(orig);
        }
    }
}