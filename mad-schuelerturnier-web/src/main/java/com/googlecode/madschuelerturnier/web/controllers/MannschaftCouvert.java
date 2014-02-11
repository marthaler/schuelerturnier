/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.pdf.MannschaftBegeiterA5CouverPDFCreator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Controller zum Downloaden der Mannschaftscouverts
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class MannschaftCouvert {

    @Autowired
    private MannschaftBegeiterA5CouverPDFCreator cr;

    @RequestMapping(value = "/couvertdownload", method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition","inline; filename=couverts.pdf");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Pragma", "No-cache");

            // get your file as InputStream
            InputStream is = new ByteArrayInputStream(cr.createPdfFromDB());
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }

    }


}
