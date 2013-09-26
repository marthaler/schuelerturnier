/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.templateengine;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;
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
