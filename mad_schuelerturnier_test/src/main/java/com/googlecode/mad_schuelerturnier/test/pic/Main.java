package com.googlecode.mad_schuelerturnier.test.pic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {


        BufferedImage image = ImageIO.read(new File("/res/a.png"));
        image = CornerUtil.setCorners(image);
        ImageIO.write(image, "png", new File("/res/b.png"));

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

        System.out.println("str: " + str);


        //image = BarcodeUtil.encode("ich bin ein test");

        ImageIO.write(image, "png", new File("/res/m2.png"));
    }
}