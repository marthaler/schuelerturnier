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

/**
 * Fuehrt die Konvertierung ins Template aus
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.7
 */
public class TemplateEngine {

    private static final Logger LOG = Logger.getLogger(TemplateEngine.class);

    private TemplateEngine() {
    }

    public static String convert(String templateFile, VelocityContext context) {

        VelocityEngine ve = new VelocityEngine();

        ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
        ve.setProperty("runtime.log.logsystem.log4j.category", TemplateEngine.class.getName());
        ve.init();

        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        InputStream templateIn = new BufferedInputStream(TemplateEngine.class.getResourceAsStream("/" + templateFile + ".html"));
        StringWriter writer = new StringWriter();

        SimpleNode node = null;

        try {
            IOUtils.copy(templateIn, writer, "UTF-8");
            String templateAsString = writer.toString();
            node = runtimeServices.parse(templateAsString, templateFile);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        Template template = new Template();
        template.setRuntimeServices(runtimeServices);
        template.setData(node);
        template.initDocument();

        StringWriter sw = new StringWriter();

        template.merge(context, sw);
        return sw.toString();

    }
}
