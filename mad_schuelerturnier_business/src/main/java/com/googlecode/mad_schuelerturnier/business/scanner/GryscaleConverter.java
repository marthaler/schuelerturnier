package com.googlecode.mad_schuelerturnier.business.scanner;
 
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
 
public class GryscaleConverter {

    public static void convertToGray(String filesource, String filedest){
        BufferedImage src = null;
        try {
            src = ImageIO.read(new File(filesource));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp grayScaleConversionOp = new ColorConvertOp(ColorSpace
                .getInstance(ColorSpace.CS_GRAY), null);

        grayScaleConversionOp.filter(src, dest);
        try {
            ImageIO.write(dest, "png", new File(filedest));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
 
}