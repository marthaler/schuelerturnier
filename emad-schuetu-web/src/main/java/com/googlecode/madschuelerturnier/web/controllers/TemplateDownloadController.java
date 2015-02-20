/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.serienbriefe.TemplateBusiness;
import com.googlecode.madschuelerturnier.business.serienbriefe.TemplateBusinessImpl;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import com.googlecode.madschuelerturnier.web.backingbeans.KontakteBackingBean;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
@Scope("session")
public class TemplateDownloadController {

    private static final Logger LOG = Logger.getLogger(TemplateDownloadController.class);

    @Autowired
    private TemplateBusiness controller;

    @Autowired
    private KontakteBackingBean kontakte;

    public StreamedContent getFileAsStreamedContent(String name) {
        InputStream is = new ByteArrayInputStream(controller.getTemplate(name));
        return new DefaultStreamedContent(is, "application/pdf", name+".pdf");
    }

    public StreamedContent getFileAsStreamedContentReal(String name) {

        InputStream is = new ByteArrayInputStream(controller.getBriefe(kontakte.getAll(),name));
        return new DefaultStreamedContent(is, "application/pdf",name+".pdf");
    }

    public StreamedContent downloadCouverts() {
        InputStream is = new ByteArrayInputStream(kontakte.downloadCouverts());
        return new DefaultStreamedContent(is, "application/pdf","couverts.pdf");
    }


}
