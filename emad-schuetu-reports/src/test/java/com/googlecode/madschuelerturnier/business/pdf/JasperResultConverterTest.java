/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.pdf;


import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MannschaftBegeiterA5CouverPDFCreatorTest
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class JasperResultConverterTest {

    //@Test
    public void finaleSuchenNormal() {


        List<Mannschaft2> mannschaftList = new ArrayList<Mannschaft2>();
        for (int i = 0; i < 10; i++) {

            Mannschaft2 m = new Mannschaft2(i);


            mannschaftList.add(m);

        }

        JasperResultConverter cr = new JasperResultConverter();
        byte[] result = cr.createPdf(mannschaftList,"couvert2");

        try {
            // todo in den target verschieben
            FileUtils.writeByteArrayToFile(new File("d:/test2.pdf"), result);
            Assert.assertTrue(result.length > 30);
        } catch (Exception e) {
            e.printStackTrace();
        }


        PDDocument document = null;
        try {
            document = PDDocument.loadNonSeq(new File("d:/Test.pdf"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
        int page = 0;
        for (PDPage pdPage : pdPages)
        {
            ++page;
            BufferedImage bim = null;
            try {
                bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ImageIOUtil.writeImage(bim, "png", "d:/png" + "-" + page, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public File targetDir(){
        String relPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        File targetDir = new File(relPath+"../../target");
        if(!targetDir.exists()) {
            targetDir.mkdir();
        }
        return targetDir;
    }


}

