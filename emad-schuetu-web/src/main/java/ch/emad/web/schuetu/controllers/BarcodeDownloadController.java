/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu.controllers;

import ch.emad.business.schuetu.picture.BarcodeUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    public void getFile(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            BufferedImage image = BarcodeUtil.encode(id);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

}
