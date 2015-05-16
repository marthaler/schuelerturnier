/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu.controllers;

import ch.emad.business.schuetu.serienbriefe.TemplateBusiness;
import ch.emad.web.schuetu.backingbeans.KontakteBackingBean;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
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
