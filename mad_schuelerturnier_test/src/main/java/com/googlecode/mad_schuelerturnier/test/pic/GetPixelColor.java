package com.googlecode.mad_schuelerturnier.test.pic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GetPixelColor {

    //int y, x, tofind, col;

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        try {
            //read image file
            File file1 = new File("/res/m1.png");
            BufferedImage image1 = ImageIO.read(file1);

            //write file
            FileWriter fstream = new FileWriter("E:\\pixellog1.txt");
            BufferedWriter out = new BufferedWriter(fstream);

            //color object
            //Color cyan = new Color(0, 255, 255);

            //find cyan pixels


            findcyanpixelsVonOben(image1);
            findcyanpixelsVonRechts(image1);
            findcyanpixelsVonUnten(image1);
            findcyanpixelsVonLinks(image1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void findcyanpixelsVonOben(BufferedImage image1) {
        for (int y = 0; y < image1.getHeight(); y++) {
            for (int x = 0; x < image1.getWidth(); x++) {

                int c3 = image1.getRGB(x, y);
                Color color = new Color(c3);


                //if (cyan.equals(image1.getRGB(x, y)){

                if (color.getRed() > 200 && color.getGreen() < 20 && color.getBlue() < 20) {
                    System.out.println("von oben red found at=" + x + "," + y);
                    return;

                }
            }
        }
    }

    private static void findcyanpixelsVonRechts(BufferedImage image1) {
        for (int x = image1.getWidth() - 1; x > 0; x--) {
            for (int y = 0; y < image1.getHeight(); y++) {

                int c3 = image1.getRGB(x, y);
                Color color = new Color(c3);


                //if (cyan.equals(image1.getRGB(x, y)){
                //  System.out.println(red + " " +c3);
                if (color.getRed() > 200 && color.getGreen() < 20 && color.getBlue() < 20) {
                    System.out.println("von rechts red found at=" + x + "," + y);
                    return;

                }
            }
        }
    }

    private static void findcyanpixelsVonUnten(BufferedImage image1) {
        for (int y = image1.getHeight() - 1; y > 0; y--) {
            for (int x = 0; x < image1.getWidth() - 1; x++) {
                //System.out.println(x + " " +y);
                int c3 = image1.getRGB(x, y);
                Color color = new Color(c3);


                //if (cyan.equals(image1.getRGB(x, y)){

                if (color.getRed() > 200 && color.getGreen() < 20 && color.getBlue() < 20) {
                    System.out.println("von unten red found at=" + x + "," + y);
                    return;

                }
            }
        }
    }

    private static void findcyanpixelsVonLinks(BufferedImage image1) {
        for (int y = 0; y < image1.getHeight(); y++) {
            for (int x = 0; x < image1.getHeight(); x++) {

                int c3 = image1.getRGB(x, y);
                Color color = new Color(c3);


                //if (cyan.equals(image1.getRGB(x, y)){
                //  System.out.println(red + " " +c3);
                if (color.getRed() > 200 && color.getGreen() < 20 && color.getBlue() < 20) {
                    System.out.println("von rechts red found at=" + x + "," + y);
                    return;

                }
            }
        }
    }
}