/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.generator;

import com.googlecode.mad_schuelerturnier.business.picture.BarcodeUtil;
import com.googlecode.mad_schuelerturnier.model.helper.IDGeneratorContainer;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * mad letzte aenderung: $Date: 2012-01-09 21:02:56 +0100 (Mo, 09 Jan 2012) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 155 $
 * @headurl $HeadURL:
 * https://mad-schuelerturnier.googlecode.com/svn/trunk/mad_schuelereturnier
 * /src/main/java/com/googlecode/mad_schuelerturnier/business/
 * controller/leiter/HTMLSchiriConverter.java $
 */
@Controller
public class BarcodeGenerator {

    private static final Logger LOG = Logger.getLogger(BarcodeGenerator.class);

    private static String folder;

    @Async
    public void init(String path) {
        folder = path + "barcode" + System.getProperty("file.separator");
        new File(folder).mkdirs();
        for (int i = 0; i < 676; i++) {
            barcode(IDGeneratorContainer.getNext());
        }

    }

    private void barcode(String text) {

        if (text == null || "".equals(text)) {
            text = "00";
        }

        try {


            BufferedImage image = BarcodeUtil.encode(text);
            File f = new File(folder + text + ".png");

            ImageIO.write(image, "png", f);


        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}