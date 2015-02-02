/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.serienbriefe.TemplateBusiness;
import com.googlecode.madschuelerturnier.business.serienbriefe.TemplateBusinessImpl;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Controller zum downloaden der ausgefuellten PDF Files
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class TemplateDownloadController {

    private static final Logger LOG = Logger.getLogger(TemplateDownloadController.class);

    @Autowired
    private TemplateBusiness controller;

    public StreamedContent getFileAsStreamedContent(String name) {
        InputStream is = new ByteArrayInputStream(controller.getTemplate(name));
        return new DefaultStreamedContent(is, "application/pdf", name+".pdf");
    }

    public StreamedContent getFileAsStreamedContent() {
        InputStream is = new ByteArrayInputStream(controller.getRechnungen());
        return new DefaultStreamedContent(is, "application/pdf","rechnungen.pdf");
    }



}
