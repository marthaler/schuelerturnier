/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.pdf;


import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * MannschaftBegeiterA5CouverPDFCreatorTest
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class JasperResultConverterTest {

    @Test
    public void finaleSuchenNormal() {


        List<Mannschaft2> mannschaftList = new ArrayList<Mannschaft2>();
        for (int i = 0; i < 10; i++) {

            Mannschaft2 m = new Mannschaft2(i);


            mannschaftList.add(m);

        }

        JasperResultConverter cr = new JasperResultConverter();
        byte[] result = cr.createPdf(mannschaftList,"couvert");

        try {
            // todo in den target verschieben
            FileUtils.writeByteArrayToFile(new File("d:/pdf/test.pdf"), result);
            Assert.assertTrue(result.length > 30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

