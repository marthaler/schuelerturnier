/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model;

import com.googlecode.mad_schuelerturnier.persistence.repository.TextRepository;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test zum pruefen ob die CRUD funktionen fuer die DO's funktionieren, mit eager loading
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-persistence-context.xml")
public class TextRepositoryIntegrationTest {

    private static final Logger LOG = Logger.getLogger(TextRepositoryIntegrationTest.class);

    @Autowired
    private TextRepository repo;

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void textTest() {

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            b.append("A");
        }

        Text t = new Text();
        t.setValue(b.toString());
        t.setKey("key");
        repo.save(t);

        Text te = repo.findTextByKey("key");
        Assert.assertTrue(te.getValue().length() > 4999);

        Text t2 = new Text();
        t2.setKey("key");

        repo.save(t2);

    }
}
