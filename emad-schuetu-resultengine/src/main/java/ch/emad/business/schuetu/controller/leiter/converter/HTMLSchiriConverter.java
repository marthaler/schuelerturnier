/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.controller.leiter.converter;

import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.enums.SpielEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Controller
public class HTMLSchiriConverter {

    private static final Logger LOG = Logger.getLogger(HTMLSchiriConverter.class);

    private static final String TR = "<tr>";
    private static final String TD = "<td> ";
    private static final String TD_E = "</td>";
    private static final String TR_E = "</tr>";
    private static final String TABLE_E = "</table>";
    private static final String BR = "<br>";
    private static final String TBODY_E = "</tbody>";

    @Autowired
    private XHTMLOutputUtil xhtml;

    public String getTable(final List<Spiel> list) {
        String responseString = "";
        final List<String> listT = new ArrayList<String>();

        for (final Spiel spiel : list) {


            if (spiel.getPlatz() == null) {
                HTMLSchiriConverter.LOG.warn("spielkorrektur gefunden ohne Platz... werde dieses überspringen... " + spiel.toString());
                continue;
            }

            String nameA = "";
            if (spiel.getMannschaftA() == null) {

                if (spiel.getTyp() == SpielEnum.GFINAL) {
                    nameA = "GrFin-" + spiel.getKategorieName();
                }

                if (spiel.getTyp() == SpielEnum.KFINAL) {
                    nameA = "KlFin-" + spiel.getKategorieName();
                }


            } else {
                nameA = spiel.getMannschaftA().getName();
            }
            String nameB = "";
            if (spiel.getMannschaftB() != null) {
                nameB = spiel.getMannschaftB().getName();
            }

            // zeilenumbruch
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String ret = getTemplate2().replace("[zeit]", sdf.format(spiel.getStart()));
            ret = ret.replace("[idstring]", spiel.getIdString().toUpperCase());
            ret = ret.replace("[platz]", spiel.getPlatz().toString());
            ret = ret.replace("[a]", nameA);
            ret = ret.replace("[b]", nameB);

            listT.add(ret);
        }

        int i = 0;

        for (final String string : listT) {

            if (i % 2 == 0) {

                if ((i % 12 == 0) && (i > 0)) {
                    responseString = responseString + "<table class='bb' border='0' cellspacing='0' cellpadding='3' width='750' style=\"page-break-after:always;\">";
                } else {
                    responseString = responseString + "<table border='0' cellspacing='0' cellpadding='3' width='750'>";
                }
                responseString = responseString + TR;
            }
            responseString = responseString + TD + string + TD_E;
            if (i % 2 == 2) {
                responseString = responseString + TR_E;
                responseString = responseString + TABLE_E;

            }
            i++;
        }

        responseString = responseString + BR;
        return xhtml.cleanup(responseString, true);
    }

    private String getTemplate2() {

        final StringBuilder b = new StringBuilder();

        b.append("<table style=\"border:2px solid black;\" border='2' cellpadding=\"0\" cellspacing=\"0\" height=\"122\"");
        b.append("width=\"350\">");
        b.append("<tbody>");
        b.append(TR);
        b.append("<td colspan=\"2\"><b>&nbsp;Platz [platz] um [zeit]&nbsp;[idstring]&nbsp;</b> </td>");
        b.append(TR_E);
        b.append(TR);
        b.append("<td colspan=\"1\" align=\"left\"><b>&nbsp;[a]</b>&nbsp; Farbe:");
        b.append("______________&nbsp;&nbsp; Tore:</td>");
        b.append("<td rowspan=\"4\" colspan=\"1\"><img src='barcode/[idstring].png'");
        b.append("height=\"85\" width=\"85\"></td>");
        b.append(TR_E);
        b.append("<tr align=\"center\">");
        b.append("<td colspan=\"1\" rowspan=\"1\">" +

                "1 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "2 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "3 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "4 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "5 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "6 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "7 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "8 <input name=\"1\" value=\"1\" type=\"checkbox\">" + TD_E);
        b.append(TR_E);
        b.append(TR);
        b.append("<td colspan=\"1\" align=\"left\"><b>&nbsp;[b]</b>&nbsp; Farbe:");
        b.append("______________&nbsp;&nbsp; Tore:</td>");
        b.append(TR_E);
        b.append("<tr align=\"center\">");
        b.append("<td colspan=\"1\" rowspan=\"1\">" +
                "1 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "2 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "3 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "4 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "5 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "6 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "7 <input name=\"1\" value=\"1\" type=\"checkbox\">" +
                "8 <input name=\"1\" value=\"1\" type=\"checkbox\">" + TD_E);
        b.append(TR_E);
        b.append(TBODY_E);
        b.append(TABLE_E);
        return b.toString();
    }

}