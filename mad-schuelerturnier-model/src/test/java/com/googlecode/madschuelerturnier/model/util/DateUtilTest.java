/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model.util;

import com.googlecode.madschuelerturnier.util.DateUtil;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.NoNestedClassRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.rule.impl.NoStaticExceptFinalRule;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Zum Testen des Date Utils
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public class DateUtilTest {


    @Test
    public void ensureExpectedPojoCount() {
        Date d = new Date(66644333);
        String str = DateUtil.getShortTimeDayString(d);
        Assert.assertEquals("Datum falsch formatiert","Do 19:30",str);
    }


}