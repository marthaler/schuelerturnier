/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.util;

import ch.emad.model.schuetu.util.DateUtil;
import org.junit.*;
import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;

/**
 * Zum Testen des Date Utils
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public class DateUtilTest {


    @Test
    public void testDateUtilFormat() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));
        Date d = new Date(66644333);
        String str = DateUtil.getShortTimeDayString(d);
        Assert.assertEquals("Datum falsch formatiert","Do 19:30",str);
    }


}