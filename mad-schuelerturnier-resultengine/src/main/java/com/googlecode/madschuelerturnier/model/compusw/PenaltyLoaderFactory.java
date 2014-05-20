/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.compusw;

import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.model.enums.RangierungsgrundEnum;
import com.googlecode.madschuelerturnier.model.enums.SpielEnum;
import com.googlecode.madschuelerturnier.model.helper.IDGeneratorContainer;
import com.googlecode.madschuelerturnier.persistence.repository.PenaltyRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dient dazu eine Penalty Instanz zu erzeugen oder aus der db zu laden, falls diese bereits einmal
 * abgespeichert wurde.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.0
 */
@Component
public class PenaltyLoaderFactory {

    private static final Logger LOG = Logger.getLogger(PenaltyLoaderFactory.class);

    private static PenaltyLoaderFactory INSTANCE;

    public PenaltyLoaderFactory(){
        INSTANCE = this;
    }

    public static PenaltyLoaderFactory getInstance(){
        return INSTANCE;
    }

    @Autowired
    private PenaltyRepository repo;

    public Penalty getPenalty(List<Mannschaft> mannschaften){

        // zum aufbereiten des String
        Penalty dump = new Penalty();

        for(Mannschaft m : mannschaften){
            dump.addMannschaftInitial(m);
        }

        Penalty result = repo.findPenaltyByOriginalreihenfolge(dump.toMannschaftsString());

        if(result != null){
            return result;
        }

        // dump uebernehmen und speichern und zurueckgeben
        dump.setIdString(IDGeneratorContainer.getNext());
        LOG.info("neuer penalty: " + dump);
        return repo.save(dump);

    }

}