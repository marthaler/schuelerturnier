/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.web.controllers;

import ch.emad.business.schuetu.pdf.MannschaftBegleiterA5CouverPDFCreator;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Controller zum Downloaden der Mannschaftscouverts als C5
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class MannschaftCouvertController {

    private static final Logger LOG = Logger.getLogger(MannschaftCouvertController.class);

    @Autowired
    private MannschaftBegleiterA5CouverPDFCreator cr;

    @RequestMapping(value = "/couvertdownload", method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=couverts.pdf");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Pragma", "No-cache");

            // get your file as InputStream
            InputStream is = new ByteArrayInputStream(cr.createPdfFromDB());
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }

    }


}
