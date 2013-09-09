package com.googlecode.madschuelerturnier.business.scanner;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Cropper {

    private Cropper() {

    }

    private static final Logger LOG = Logger.getLogger(Cropper.class);

    public static void crop(String filesource, String filedest, int a, int b, int c, int d) {
        try {
            BufferedImage img = ImageIO.read(new File(filesource));

            BufferedImage partImg = img.getSubimage(a, b, c, d);

            ImageIO.write(partImg, "png", new File(filedest));

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        Cropper.crop("/Users/dama/Documents/schuelerturnier/mad_schuelerturnier_web/target/mad_schuelerturnier_web-1.1.1-SNAPSHOT/static/scannbilder/1369768081375.png", "/temp/pmg2.png", 0, 90, 590, 260);
    }
}