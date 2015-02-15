/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.webstamp;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import ch.emad.schuetu.reports.pdf.PDFUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class WebstampService {

    private static final Logger LOG = Logger.getLogger(WebstampService.class);

    @Autowired
    @Qualifier("dropboxConnector")
    private DropboxConnector connector;

    private List<String> pngs = new ArrayList<String>();

    private SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    @PostConstruct
    private void init() {
        prepareStamps();
    }

    public int countAvailableStamps() {
        return pngs.size();
    }

    public byte[] getNextStamp() {
        if(countAvailableStamps()<1){
           return null;
        }
        String file = pngs.remove(0);
        byte[] in= connector.loadFile(file);
        byte[] ret = rotatePic(in,90);

        connector.saveFile(file.replace("neu","alt"),in);
        connector.deleteFile(file);

        return ret;
    }

    @Async
    public void prepareStamps() {
        int i = 0;
        String name = fm.format(new Date());
        List<byte[]> out = new ArrayList<byte[]>();
        // pdfs holen
        List<String> files = connector.getFilesInFolder("stamps/neu");
        for (String file : files) {
            //umwandeln in einzelne png und in liste speichern
            if (file.contains(".pdf")) {
                byte[] pdf = connector.loadFile("stamps/neu/" + file);
                out.addAll(PDFUtil.splitPDFToPNG(pdf));
                // pdf in alt ordner verschieben
                connector.saveFile("stamps/alt/" + name + ".pdf", pdf);
                connector.deleteFile("stamps/neu/"+file);
            }
        }
        // fertige png's im neu ordner speichern
        for (byte[] file : out) {
            i++;
            connector.saveFile("stamps/neu/" + name + "-" + String.format("%03d", i) + ".png", file);
        }

        // zaehler neu stellen
        pngs.clear();
        List<String> files2 = connector.getFilesInFolder("stamps/neu");
        for (String file : files2) {
            if (file.contains(".png")) {
                pngs.add(file);
            }
        }
        Collections.sort(pngs);
    }

    private byte[] rotatePic(byte[] img, double angle) {
        Image image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(img));
        } catch (IOException e) {
            e.printStackTrace();
        }

        double sin = Math.abs(Math.sin(Math.toRadians(angle))),
                cos = Math.abs(Math.cos(Math.toRadians(angle)));

        int w = image.getWidth(null), h = image.getHeight(null);

        int neww = (int) Math.floor(w * cos + h * sin),
                newh = (int) Math.floor(h * cos + w * sin);

        BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimg.createGraphics();

        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);

        BufferedImage im;

        if (image instanceof BufferedImage) {
            im = (BufferedImage) image;
        }else {
            // Create a buffered image with transparency
            BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            // Draw the image on to the buffered image
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();
            // Return the buffered image
            im = bimage;
        }

        g.drawRenderedImage(im, null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bimg, "png", baos);
            baos.flush();
            baos.close();
        } catch (IOException e) {
            LOG.error(e);
        }
        return baos.toByteArray();

    }

}
