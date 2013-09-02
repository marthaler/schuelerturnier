package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.business.impl.Business;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TestSpielKontrollerTest
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class TestBusiness {

    @Autowired
    private Business business;


    @Test
    public void TestSpielKontroller() {
        Assert.assertNull(business.getSpielEinstellungen());
        Assert.assertFalse(business.isDBInitialized());
        business.initializeDB();
        Assert.assertNotNull(business.getSpielEinstellungen());
    }
}
