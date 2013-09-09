package com.googlecode.madschuelerturnier.business.scanner;

import com.googlecode.madschuelerturnier.exceptions.TurnierException;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class TrimWhite {

    private static final Logger LOG = Logger.getLogger(TrimWhite.class);

    private BufferedImage img;

    public static BufferedImage toBinaryImage(final BufferedImage image) {
        final BufferedImage blackAndWhiteImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_BYTE_BINARY);
        final Graphics2D g = (Graphics2D) blackAndWhiteImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return blackAndWhiteImage;
    }

    public TrimWhite(File input) throws TurnierException {
        try {
            img = ImageIO.read(input);

            img = toBinaryImage(img);
        } catch (IOException e) {
            throw new TurnierException("Problem reading image", e);
        }
    }

    public void trim() {
        int width = getTrimmedWidth();
        int height = getTrimmedHeight();

        BufferedImage newImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = newImg.createGraphics();
        g.drawImage(img, 0, 0, null);
        img = newImg;
    }

    public void write(File f) throws TurnierException {
        try {
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            throw new TurnierException("Problem reading image", e);
        }
    }

    private int getTrimmedWidth() {
        int height = this.img.getHeight();
        int width = this.img.getWidth();
        int trimmedWidth = 0;

        for (int i = 0; i < height; i++) {
            for (int j = width - 1; j >= 0; j--) {
                if (img.getRGB(j, i) != Color.WHITE.getRGB() &&
                        j > trimmedWidth) {
                    trimmedWidth = j;
                    break;
                }
            }
        }

        return trimmedWidth;
    }

    private int getTrimmedHeight() {
        int width = this.img.getWidth();
        int height = this.img.getHeight();
        int trimmedHeight = 0;

        for (int i = 0; i < width; i++) {
            for (int j = height - 1; j >= 0; j--) {
                if (img.getRGB(i, j) != Color.WHITE.getRGB() &&
                        j > trimmedHeight) {
                    trimmedHeight = j;
                    break;
                }
            }
        }

        return trimmedHeight;
    }

    public static void main(String[] args) {

        TrimWhite trim = null;
        try {
            trim = new TrimWhite(new File("/a.jpg"));
        } catch (TurnierException e) {
            LOG.error(e.getMessage(), e);
        }
        trim.trim();
        try {
            trim.write(new File("/bmp2.bmp"));
        } catch (TurnierException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}