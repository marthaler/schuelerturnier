/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.utils;

import ch.emad.model.schuetu.model.Mannschaft;
import org.junit.Assert;
import org.junit.Test;

public class TestModelFactory {

    private ModelFactory2 fact = new ModelFactory2();

    @Test
    public void getInstanceTest() throws Exception {

        Object obj = fact.getInstance("ch.emad.model.schuetu.model.Mannschaft");
        Assert.assertNotNull(obj);

    }
}
