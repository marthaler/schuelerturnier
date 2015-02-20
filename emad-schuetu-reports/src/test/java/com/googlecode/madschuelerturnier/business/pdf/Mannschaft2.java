package com.googlecode.madschuelerturnier.business.pdf;/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */

import com.googlecode.madschuelerturnier.interfaces.CouvertReportable;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class Mannschaft2  implements CouvertReportable {

    private int count;

    public Mannschaft2(int count){
        this.count = count;
    }


    public String getAnrede() {
        return "Anrede " + count;
    }


    public String getNameVorname() {
        return "Name Vorname " + count;
    }


    public String getStrasse() {
        return "Strasse " + count;
    }


    public String getPLZOrt() {
        return "PLZ Ort " + count;
    }

    public ByteArrayInputStream getStamp(){
        try {
            return new ByteArrayInputStream(rotate(FileUtils.readFileToByteArray(new File("d:/png-39.png")), 90));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ByteArrayInputStream getPic(){
        try {
            return new ByteArrayInputStream(rotate(FileUtils.readFileToByteArray(new File("d:/png-39.png")), 90));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] rotate(byte[] img, double angle)
    {Image image = null;

        try {
             image = ImageIO.read(new ByteArrayInputStream(img));
        } catch (IOException e) {
            e.printStackTrace();
        }

        double sin = Math.abs(Math.sin(Math.toRadians(angle))),
                cos = Math.abs(Math.cos(Math.toRadians(angle)));

        int w = image.getWidth(null), h = image.getHeight(null);

        int neww = (int) Math.floor(w*cos + h*sin),
                newh = (int) Math.floor(h*cos + w*sin);

        BufferedImage bimg = toBufferedImage(getEmptyImage(neww, newh));
        Graphics2D g = bimg.createGraphics();

        g.translate((neww-w)/2, (newh-h)/2);
        g.rotate(Math.toRadians(angle), w/2, h/2);
        g.drawRenderedImage(toBufferedImage(image), null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
        ImageIO.write( bimg, "png", baos );
        baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return   baos.toByteArray();

    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Return the buffered image
        return bimage;
    }

    /**
     * Creates an empty image with transparency
     *
     * @param width The width of required image
     * @param height The height of required image
     * @return The created image
     */
    public static BufferedImage getEmptyImage(int width, int height){
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return img;
    }
}
