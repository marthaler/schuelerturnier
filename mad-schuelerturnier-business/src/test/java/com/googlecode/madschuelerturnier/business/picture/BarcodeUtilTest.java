/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.picture;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Testet das Barcode Util
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public class BarcodeUtilTest {

    private static final Logger LOG = Logger.getLogger(BarcodeUtilTest.class);

    @Test
    public void testReadBarcode() {

        BufferedImage img =null;

        URL defaultImage = this.getClass().getResource("/pictures/test.png");
        try {
             img = ImageIO.read(new File(defaultImage.getPath().toString()));
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
        Assert.assertEquals("BZ", BarcodeUtil.decode(img));

        // voller test mit Erstellen des Codes
        Assert.assertEquals("ich bin ein test", BarcodeUtil.decode(BarcodeUtil.encode("ich bin ein test")));

    }
}
