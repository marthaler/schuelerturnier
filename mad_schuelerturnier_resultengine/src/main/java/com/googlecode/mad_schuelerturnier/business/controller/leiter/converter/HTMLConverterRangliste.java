/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.controller.leiter.converter;

import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.compusw.RanglisteneintragHistorie;
import com.googlecode.mad_schuelerturnier.model.compusw.RanglisteneintragZeile;
import com.googlecode.mad_schuelerturnier.model.enums.GeschlechtEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * mad letzte aenderung: $Date: 2011-12-23 00:38:28 +0100 (Fr, 23 Dez 2011) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 66 $
 * @headurl $HeadURL: $
 */
@Component
public class HTMLConverterRangliste {

    @Autowired
    HTMLMenu menu;

    @Autowired
    XHTMLOutputUtil util;

    // todo fix !!!

    final SimpleDateFormat sdf = new SimpleDateFormat("E/HH:mm");
    final String[] kategorien = {"M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "K1", "K2", "K3", "K4", "K5", "K6", "K7", "K8", "K9"};


    public String printOutGere(final Collection<RanglisteneintragHistorie> kat, boolean menuon) {
        final StringBuffer buffer = new StringBuffer();

        if(menuon) {
          new HTMLMenu();
          buffer.append(menu.generateMenu("_"));
        }

        buffer.append("<br/>");

        buffer.append("<style type='text/css'>table {   border-spacing: 0px; border-padding:0px;width:700px;border:1px solid #000; vertical-align:top; overflow:hidden; font-size:10pt; font-family:Arial,sans-serif }td { border:1px solid #000; vertical-align:top; overflow:hidden; }</style><table>");
        buffer.append("<table style=\"font-size: 9px\" border=\"1\">");
        buffer.append("<tr>");
        buffer.append("<td>Kategorie</td><td>1. Rang</td><td>2. Rang</td><td>3. Rang</td><td>4. Rang</td>");
        buffer.append("</tr>");

        for (final String k : this.kategorien) {
            buffer.append("<tr>");
            String klassenLable = "";
            GeschlechtEnum geschlecht = null;
            if (k.contains("K")) {
                klassenLable = "Knaben " + k.replace("K", "") + ". Klasse";
                geschlecht = GeschlechtEnum.K;
            } else {
                klassenLable = "Mädchen " + k.replace("M", "") + ". Klasse";
                geschlecht = GeschlechtEnum.M;
            }

            buffer.append("<td><b>" + klassenLable + "</b></td>");

            final List<Mannschaft> mannschaften = this.getRangliste(kat, Integer.parseInt(k.substring(1, 2)), geschlecht);

            for (int i = 0; i < 4; i++) {
                Mannschaft ma = null;
                if ((mannschaften.size() > 0) && (mannschaften.size() > i)) {
                    ma = mannschaften.get(i);
                }

                if ((ma != null && ma.getGruppe().getKategorie().isFertigGespielt())) {

                    buffer.append("<td>" + ma.getName() + "</td>");
                } else {
                    buffer.append("  <td>  </td>");
                }
            }


            buffer.append("</tr>");

            buffer.append("<tr>");
            buffer.append("<td>Schulhaus</td>");
            for (int i = 0; i < 4; i++) {
                Mannschaft ma = null;

                if ((mannschaften.size() > 0) && (mannschaften.size() > i)) {
                    ma = mannschaften.get(i);
                }

                if ((ma != null && ma.getGruppe().getKategorie().isFertigGespielt())) {

                    buffer.append("<td>" + ma.getSchulhaus() + "</td>");
                } else {
                    buffer.append("  <td>  </td>");
                }
            }

            buffer.append("</tr>");

            buffer.append("<tr>");
            buffer.append("<td>Capitain</td>");

            for (int i = 0; i < 4; i++) {

                Mannschaft ma = null;
                if ((mannschaften.size() > 0) && (mannschaften.size() > i)) {
                    ma = mannschaften.get(i);
                }

                if ((ma != null && ma.getGruppe().getKategorie().isFertigGespielt())) {

                    buffer.append("<td>" + ma.getCaptainName() + "</td>");
                } else {
                    buffer.append("  <td>  </td>");
                }

            }
            buffer.append("</tr>");

            buffer.append("<tr>");
            buffer.append("<td>Begleitperson</td>");
            for (int i = 0; i < 4; i++) {

                Mannschaft ma = null;
                if ((mannschaften.size() > 0) && (mannschaften.size() > i)) {
                    ma = mannschaften.get(i);
                }
                if ((ma != null && ma.getGruppe().getKategorie().isFertigGespielt())) {

                    buffer.append("<td>" + ma.getBegleitpersonName() + "</td>");
                } else {
                    buffer.append("  <td>  </td>");
                }
            }

            buffer.append("</tr>");

            buffer.append("<tr>");
            buffer.append("  <td>  </td>");
            for (int g = 0; g < 4; g++) {
                buffer.append("  <td>  </td>");
            }

            buffer.append("</tr>");

        }
        buffer.append("</table>");
        return util.cleanup(buffer.toString(), false);
    }


    private List<Mannschaft> getRangliste(final Collection<RanglisteneintragHistorie> kategorien, int klasse, GeschlechtEnum geschlecht) {

        ArrayList<Mannschaft> res = new ArrayList<Mannschaft>();

        for (RanglisteneintragHistorie ranglisteneintragHistorie : kategorien) {
            List<RanglisteneintragZeile> m = ranglisteneintragHistorie.getZeilen();

            for (RanglisteneintragZeile temp : m) {

                if (temp.getMannschaft().getGeschlecht() == geschlecht) {
                    if (temp.getMannschaft().getKlasse() == klasse) {
                        res.add(temp.getMannschaft());
                    }
                }
            }
        }
        return res;
    }
}
