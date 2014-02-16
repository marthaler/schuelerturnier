/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.pdf;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * MannschaftBegeiterA5CouverPDFCreatorTest
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class MannschaftBegeiterA5CouverPDFCreatorTest {

    @Test
    public void finaleSuchenNormal() {


        List<Mannschaft> mannschaftList = new ArrayList<Mannschaft>();
        for (int i = 0; i < 10; i++) {

            Mannschaft m = new Mannschaft();
            m.setBegleitpersonAnrede("Anrede-" + i);
            m.setBegleitpersonName("Name-" + i);
            m.setBegleitpersonStrasse("Strasse-" + i);
            m.setBegleitpersonPLZOrt("Ort-" + i);

            mannschaftList.add(m);

        }

        MannschaftBegeiterA5CouverPDFCreator cr = new MannschaftBegeiterA5CouverPDFCreator();
        byte[] result = cr.createPdf(mannschaftList);

        try {
            //FileUtils.writeByteArrayToFile(new File("test.pdf"),result);
            Assert.assertTrue(result.length > 30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

