/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.picture;

import com.googlecode.madschuelerturnier.business.mail.MailSender;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * MailSender Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public class BarcodeUtilTest {


    @Test
    public void testReadBarcode() {

        BufferedImage img =null;

        URL defaultImage = BarcodeUtilTest.class.getResource("/pictures/test.png");
        try {
             img = ImageIO.read(new File(defaultImage.getPath().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("BZ", BarcodeUtil.decode(img));

    }

}
