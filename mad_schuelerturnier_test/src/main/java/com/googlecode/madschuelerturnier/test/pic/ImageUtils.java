package com.googlecode.madschuelerturnier.test.pic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;

public class ImageUtils {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageIO.read(new File("/res/m1.png"));
        BufferedImage rotatedImage = rotateImage(image, 210.0);
        ImageIO.write(rotatedImage, "png", new File("/res/m7.png"));
    }

    static BufferedImage rotateImage(BufferedImage src, double degrees) {
        AffineTransform affineTransform = AffineTransform.getRotateInstance(
                Math.toRadians(degrees),
                src.getWidth() / 2,
                src.getHeight() / 2);
        BufferedImage rotatedImage = new BufferedImage(src.getWidth(), src
                .getHeight(), src.getType());
        Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
        g.setTransform(affineTransform);
        g.drawImage(src, 0, 0, null);
        return rotatedImage;
    }


    static BufferedImage toBandW(BufferedImage src) {

        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return op.filter(src, src);
    }


}