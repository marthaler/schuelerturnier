/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class TestModelFactory {

    private ModelFactory fact = new ModelFactory();

    @Test
    public void getInstanceTest() throws Exception {

        Object obj = fact.getInstance("com.googlecode.madschuelerturnier.model.Mannschaft");
        Assert.assertNotNull(obj);

    }
}
