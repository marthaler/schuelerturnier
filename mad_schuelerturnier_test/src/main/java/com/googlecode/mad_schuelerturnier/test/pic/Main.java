package com.googlecode.mad_schuelerturnier.test.pic;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {


        BufferedImage image = ImageIO.read(new File("/res/555.png"));
        image = CornerUtil.setCorners(image);
        ImageIO.write(image, "png", new File("/res/666.png"));

        image = ImageUtils.rotateImage(image, 10);
        image = CornerUtil.setCorners(image);
        ImageIO.write(image, "png", new File("/res/b1.png"));

        image = ImageUtils.rotateImage(image, 10);
        ImageIO.write(image, "png", new File("/res/b2.png"));

        image = ImageUtils.rotateImage(image, 10);
        ImageIO.write(image, "png", new File("/res/b3.png"));

        image = ImageUtils.rotateImage(image, 10);
        ImageIO.write(image, "png", new File("/res/b4.png"));
        //image = BarcodeUtil.encode("4");
        // image = CornerUtil.setCorners(image);

        // image = ImageUtils.rotateImage(image, 210.0);


        // image = ImageUtils.toBandW(image);


        //image = ImageUtils.rotateImage(image, 45);
        String str = BarcodeUtil.decode(image);

        LOG.info("" + "str: " + str);


        //image = BarcodeUtil.encode("ich bin ein test");

        ImageIO.write(image, "png", new File("/res/m2.png"));
    }
}