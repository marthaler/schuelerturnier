/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.pdf;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.comperators.MannschaftsNamenComperator;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

