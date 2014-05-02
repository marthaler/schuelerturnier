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

    private static final Logger LOG = Logger.getLogger(MannschaftBegleiterA5CouverPDFCreator.class);

    public byte[] createPdfFromDB() {
        List<Mannschaft> mannschaften = repo.findAll();
        Collections.sort(mannschaften,new MannschaftsNamenComperator());
        return createPdf(repo.findAll());
    }

    protected byte[] createPdf(List<Mannschaft> mannschaften) {
        try {
            InputStream inputStream = getTemplate("couvert.jrxml");

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(mannschaften);

            Map parameters = new HashMap();

            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,stream);
            exporter.exportReport();
            return stream.toByteArray();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    private InputStream getTemplate(String template){
        return this.getClass().getClassLoader().getResourceAsStream(template);
    }
}

