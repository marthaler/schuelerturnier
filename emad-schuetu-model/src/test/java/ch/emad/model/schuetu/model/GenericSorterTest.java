/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model;

import ch.emad.model.schuetu.model.comperators.GenericSorter;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.NoNestedClassRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.rule.impl.NoStaticExceptFinalRule;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Zum automatischen Testen der Pojos
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public class GenericSorterTest {

    @Test
    public void ensureExpectedPojoCount() {
        List<Rechnung> r = new ArrayList<Rechnung>();

        Rechnung r1 = new Rechnung();
        r1.setDebitor("a");
        r1.setRechnungsnummer(3);
        r.add(r1);

        Rechnung r2 = new Rechnung();
        r2.setDebitor("b");
        r2.setRechnungsnummer(2);
        r.add(r2);

        Rechnung r3 = new Rechnung();
        r3.setDebitor("c");
        r3.setRechnungsnummer(1);
        r.add(r3);

        GenericSorter.sortAsc(r, "getDebitor");
        Assert.assertTrue(r.get(0).getDebitor().equals("a"));
        Assert.assertTrue(r.get(2).getDebitor().equals("c"));

        GenericSorter.sortDesc(r, "getDebitor");
        Assert.assertTrue(r.get(0).getDebitor().equals("c"));
        Assert.assertTrue(r.get(2).getDebitor().equals("a"));

        /**
        GenericSorter.sortAsc(r, "getRechnungsnummer");
        Assert.assertTrue(r.get(0).getRechnungsnummer() == 3);
        Assert.assertTrue(r.get(2).getRechnungsnummer() == 1);

        GenericSorter.sortDesc(r, "getRechnungsnummer");
        Assert.assertTrue(r.get(0).getRechnungsnummer() == 1);
        Assert.assertTrue(r.get(2).getRechnungsnummer() == 3);
         **/
    }


}