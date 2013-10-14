/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.templateengine;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tool zum konvertieren der Datumsformate in Velocity
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.7
 */
public class DateTool {

    public DateTool() {
    }

    public String format(String pattern, Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);

    }
}
