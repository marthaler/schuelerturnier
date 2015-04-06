/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.pdf;

import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.comperators.MannschaftsNamenComperator;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Generiert Jasper Report PDF
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@Component
public class MannschaftBegleiterA5CouverPDFCreator {
    @Autowired
    private MannschaftRepository repo;

    @Autowired
    private JasperResultConverter converter;

    private static final Logger LOG = Logger.getLogger(MannschaftBegleiterA5CouverPDFCreator.class);

    public byte[] createPdfFromDB() {
        List<Mannschaft> mannschaften = repo.findAll();
        Collections.sort(mannschaften,new MannschaftsNamenComperator());
        return createPdf(mannschaften);
    }

    protected byte[] createPdf(List<Mannschaft> mannschaften) {
        return converter.createPdf(mannschaften,"couvert");
    }
 
}

