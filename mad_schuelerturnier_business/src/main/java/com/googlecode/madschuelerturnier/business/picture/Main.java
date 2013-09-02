package com.googlecode.madschuelerturnier.business.picture;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
 * JDeskew 1.0
 * 
 * http://www.recognition-software.com/image/deskew/
 * 
 * A Java port of the VB.NET class provided by GMSE Imaging.
 * 
 * http://www.codeproject.com/KB/graphics/Deskew_an_Image.aspx
 *  
 * License Type: The Code Project Open License (CPOL) 1.02
 * 
 * @author Roland Quast
 */

public class Main {

    private static final double MINIMUM_DESKEW_THRESHOLD = 0.6d;

    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            new Main("/res/rrr.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String infilename;

    private BufferedImage outputImage;

    public Main(String inputFilename) throws IOException {
        setCommandLineArgs(inputFilename);
        performDeskew();
        saveOutput();
    }

    public String getCommandLineArgs() {
        return infilename;
    }

    public BufferedImage getOutputImage() {
        return outputImage;
    }

    private BufferedImage getSourceImage() throws IOException {
        return ImageUtil.readImageFile(new File(getCommandLineArgs()));
    }

    private void performDeskew() throws IOException {

        BufferedImage sourceImage = getSourceImage();
        BufferedImage outputImageL = null;

        ImageDeskew deskew = new ImageDeskew(sourceImage);
        double imageSkewAngle = deskew.getSkewAngle();

        if ((imageSkewAngle > Main.MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(Main.MINIMUM_DESKEW_THRESHOLD))) {
            outputImageL = ImageUtil.rotate(sourceImage, -imageSkewAngle,
                    sourceImage.getWidth() / 2, sourceImage.getHeight() / 2);
        } else {
            outputImageL = sourceImage;
        }

        setOutputImage(outputImageL);

    }

    private void saveOutput() throws IOException {

        File outputFileName = new File(getCommandLineArgs());
        String[] nameParts = outputFileName.getName().split("\\.");
        String extension = nameParts[(nameParts.length - 1)];
        String filename = "";

        for (int i = 0; i < nameParts.length - 1; i++) {
            filename = nameParts[i];
            if (i != nameParts.length - 2) {
                filename += ".";
            }
        }
        filename += "deskewed." + extension;

        ImageIO.write(getOutputImage(), extension.toUpperCase().trim(),
                new File("/res/" + filename));


        LOG.info("" + BarcodeUtil.decode(getOutputImage()));

    }

    public void setCommandLineArgs(String inputFilename) {
        this.infilename = inputFilename;
    }

    public void setOutputImage(BufferedImage outputImage) {
        this.outputImage = outputImage;
    }

}