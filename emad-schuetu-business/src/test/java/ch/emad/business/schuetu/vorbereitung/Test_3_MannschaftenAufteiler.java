/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.vorbereitung;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


public class Test_3_MannschaftenAufteiler {


    @After
    public void cleanup() {

    }

    @Test
    public void durchrechnenTest() {

        Float f = (5f / 2);

        Assert.assertEquals(Float.valueOf((float) 2.5), f);

        int i = Math.round(f);
        Assert.assertEquals(3, i);

    }

    @Test
    public void durchrechnenTest2() {

        int i = 5 / 2;
        Assert.assertEquals(2, i);

    }

}
