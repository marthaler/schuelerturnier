package com.googlecode.madschuelerturnier.business.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context.xml"})
public class TestModelFactory {

    @Autowired
    private ModelFactory fact;

    @Test
    public void getInstanceTest() throws Exception {

        Object obj = fact.getInstance("com.googlecode.madschuelerturnier.model.Mannschaft");
        Assert.assertNotNull(obj);

    }
}
