/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.templateengine;

import ch.emad.model.schuetu.model.Spiel;
import org.apache.velocity.VelocityContext;

import java.util.List;

/**
 * Hilfsklasse zum aufrufen der verschiedenen Templates
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.7
 */
public final class Templates {

    private Templates() {
    }

    public static String printSpiel(List<Spiel> spiele, Integer seite) {
        VelocityContext context = new VelocityContext();
        context.put("page", seite);
        context.put("spiele", spiele);
        context.put("datetool", new DateTool());
        return TemplateEngine.convert("print-spiele-vtemplate", context);
    }

    public static String getTable(final List<Spiel> spiele) {
        VelocityContext context = new VelocityContext();
        context.put("spiele", spiele);
        context.put("datetool", new DateTool());
        return TemplateEngine.convert("schirizettel-vtemplate", context);
    }


}
