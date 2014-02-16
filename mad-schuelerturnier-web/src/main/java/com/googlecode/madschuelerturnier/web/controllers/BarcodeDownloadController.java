/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.picture.BarcodeUtil;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Controller, welcher id in einen Barcode umwandelt
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class BarcodeDownloadController {

    private static final Logger LOG = Logger.getLogger(BarcodeDownloadController.class);

    @RequestMapping(value = "/barcode/{id}.png", method = RequestMethod.GET)
    public void getFile( @PathVariable("id") String id, HttpServletResponse response) {
        try {
        BufferedImage image = BarcodeUtil.encode(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ImageIO.write( image, "png", response.getOutputStream() );
         response.flushBuffer();
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }

    }

}
