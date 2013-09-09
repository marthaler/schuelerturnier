package com.googlecode.madschuelerturnier.business.scanner;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public final class GryscaleConverter {

    private static final Logger LOG = Logger.getLogger(GryscaleConverter.class);

    private GryscaleConverter() {

    }


    public static void convertToGray(String filesource, String filedest) {
        BufferedImage src = null;
        try {
            src = ImageIO.read(new File(filesource));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp grayScaleConversionOp = new ColorConvertOp(ColorSpace
                .getInstance(ColorSpace.CS_GRAY), null);

        grayScaleConversionOp.filter(src, dest);
        try {
            ImageIO.write(dest, "png", new File(filedest));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}