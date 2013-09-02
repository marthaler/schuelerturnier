package com.googlecode.madschuelerturnier.business.vorbereitung;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class Test_3_MannschaftAufteiler {

    @Autowired
    _3_MannschaftenAufteiler aufteiler;

    @After
    public void cleanup() {

    }

    @SuppressWarnings("unchecked")
    @Test
    public void durchrechnenTest() {

        Float f = (float) (5f / 2);

        Assert.assertEquals(Float.valueOf((float) 2.5), f);

        int i = Math.round(f);
        Assert.assertEquals(3, i);

    }


    @SuppressWarnings("unchecked")
    @Test
    public void durchrechnenTest2() {

        int i = 5 / 2;
        Assert.assertEquals(2, i);

    }


}
