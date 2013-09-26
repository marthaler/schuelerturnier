/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.templateengine;

import com.googlecode.madschuelerturnier.model.Spiel;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;
//import org.apache.velocity.tools.generic.DateTool;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * Hilfsklasse zum aufrufen der verschiedenen Templates
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.7
 */
public class Templates {

    private Templates() {
    }

    public static String printSpiel(List<Spiel> spiele, Integer seite) {
        VelocityContext context = new VelocityContext();
        context.put("page", seite);
        context.put("spiele", spiele);
        //context.put("datetool", new DateTool());
        return TemplateEngine.convert("print-spiele-vtemplate", context);
    }
}
