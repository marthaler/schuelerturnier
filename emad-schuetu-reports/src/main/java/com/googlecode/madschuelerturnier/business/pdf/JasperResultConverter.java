/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.pdf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
@Controller
public class JasperResultConverter {

    private static final Logger LOG = Logger.getLogger(JasperResultConverter.class);

    protected byte[] createPdf(List data,String jrxml) {
        try {
            InputStream inputStream = getTemplate("pdf/" + jrxml +".jrxml");

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(data);

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

