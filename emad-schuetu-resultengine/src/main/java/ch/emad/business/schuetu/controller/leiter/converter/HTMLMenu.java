/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.controller.leiter.converter;

import ch.emad.model.schuetu.model.Kategorie;
import ch.emad.persistence.schuetu.repository.KategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class HTMLMenu {

    public static final String TR = "<tr>";

    @Autowired
    private KategorieRepository repo;

    private static final String DOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\">";

    private static final String TABLE_WEBSITE = "<meta http-equiv=\"expires\" content=\"0\"><style type='text/css'>table.normal {border-spacing: 0px; border-padding:0px;width:700px;border:1px solid #000; vertical-align:top; overflow:hidden; font-size:10pt; font-family:Arial,sans-serif }td { border:1px solid #000; vertical-align:top; overflow:hidden; }</style><table class='normal'>";

    private List<String> generateKategorienamenListe() {
        List<Kategorie> kategorie = this.repo.findAll();
        List<String> result = new ArrayList<String>();
        for (Kategorie kategorie1 : kategorie) {
            // todo weg, wenn leere kategorien wieder geloescht werden koennen
            if (!kategorie1.getName().contains("invalide")) {
                result.add(kategorie1.getName());
            }
        }
        return result;
    }

    private String getCombobox(List<String> grl, final String sel) {

        final StringBuilder buffer = new StringBuilder();

        buffer.append("<script type=\"text/javascript\">\n");
        buffer.append("  function JumpToIt(frm) { ");
        buffer.append("    var newPage = frm.url.options[frm.url.selectedIndex].value\n");
        buffer.append("    if (newPage != \"None\") {\n");
        buffer.append("      location.href=newPage\n");
        buffer.append("    }\n");
        buffer.append("  }\n");
        buffer.append("</script>\n");

        Collections.sort(grl);

        buffer.append("<form action='controllers' method='post'>");
        buffer.append("<label valign='top' for='url'>Kategorie waehlen</label>");

        buffer.append("<select valign='top' name='url' onChange='JumpToIt(this.form)'>");
        buffer.append("<option value=''>-</option> ");

        for (final String key : grl) {

            String lab = key.toLowerCase();

            lab = enhance(lab);

            if (lab.contains("/")) {
                String[] sp = lab.split("/");

                if (sp[0].length() < 3) {
                    lab = enhance(sp[0]);
                } else {
                    lab = sp[0];
                }
                lab = lab + " / ";
                if (sp[1].length() < 3) {
                    lab = lab + enhance(sp[1]);
                } else {
                    lab = lab + sp[1];
                }
            }

            if (key.equals(sel)) {
                buffer.append(" <option value='" + lab + ".html' selected=\"selected\" >" + lab + "</option>");
            } else {
                buffer.append(" <option value='" + lab + ".html'>" + lab + "</option>");
            }
        }
        buffer.append("</select>");
        buffer.append("</form>");

        return buffer.toString();
    }

    private String enhance(String labIn) {
        String lab = labIn;
        if (lab.length() == 2) {
            lab = lab.substring(0, 1) + "0" + lab.substring(1, 2);
        }
        return lab;
    }

    // generiert den header mit den kinks und der combobox
    public String generateMenu(String selected) {

        List<String> ll = generateKategorienamenListe();

        final StringBuilder builder = new StringBuilder();
        builder.append(DOCTYPE);
        builder.append(HTMLMenu.TABLE_WEBSITE);
        builder.append(HTMLTags.TR_E);

        builder.append(HTMLTags.TD);

        builder.append(getCombobox(ll, selected));

        builder.append(HTMLTags.TD_E);

        builder.append(HTMLTags.TD);
        builder.append("<a href='rangliste.html'>rangliste</a>");
        builder.append(HTMLTags.TD_E);

        builder.append("<td valign='top'>");
        builder.append("Stand vom: [dateTime]");
        builder.append(HTMLTags.TD_E);
        builder.append(HTMLTags.TR_E);
        builder.append("</table>");

        return builder.toString();
    }
}