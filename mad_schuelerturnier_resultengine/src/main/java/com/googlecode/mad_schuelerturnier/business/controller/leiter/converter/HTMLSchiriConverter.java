/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.controller.leiter.converter;

import com.googlecode.mad_schuelerturnier.model.enums.SpielEnum;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * mad letzte aenderung: $Date: 2012-01-09 21:02:56 +0100 (Mo, 09 Jan 2012) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 155 $
 * @headurl $HeadURL:
 * https://mad-schuelerturnier.googlecode.com/svn/trunk/mad_schuelereturnier
 * /src/main/java/com/googlecode/mad_schuelerturnier/business/
 * controller/leiter/HTMLSchiriConverter.java $
 */
@Controller
public class HTMLSchiriConverter {

    private static final Logger LOG = Logger.getLogger(HTMLSchiriConverter.class);

    @Autowired
    XHTMLOutputUtil xhtml;

    /**
     * @param list
     * @return
     */
    public String getTable2(final List<Spiel> list) {
        String responseString = "";
        final List<String> listT = new ArrayList<String>();

        int k = 0;
        for (final Spiel spiel : list) {


            if (spiel.getPlatz() == null) {
                HTMLSchiriConverter.LOG.warn("Spiel gefunden ohne Platz... werde dieses überspringen... " + spiel.toString());
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

            final StringBuilder b = new StringBuilder();


            // zeilenumbruch


            if (k % 10 == 0) {
                b.append("<table border='1' cellspacing='0' cellpadding='3' width='350'>");
            } else {
                b.append("<table border='1' cellspacing='0' cellpadding='3' width='350'>");
            }

            k++;

            b.append("<tr>");
            b.append("<td colspan='12'>");
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            b.append("<b>Platz " + spiel.getPlatz() + " um " + sdf.format(spiel.getStart()) + "&nbsp;" + spiel.getIdString() + " </b>");
            b.append(" <img src='resources/static/barcode/" + spiel.getIdString() + ".png' width='100' height='100'> </b>");
            b.append("</td>");

            b.append("</tr>");

            b.append("<tr>");
            b.append("<td colspan='8'>");
            b.append("<b>" + nameA + "</b>" + "&nbsp; Farbe: ______________");
            b.append("</td>");
            b.append("<td colspan='4'>");
            b.append("Tore:");
            b.append("</td>");
            b.append("</tr>");

            b.append("<tr align='center'>");
            b.append("<td>");
            b.append("1<input type=\"radio\" name=\"1\" value=\"1\">");
            b.append("</td>");
            b.append("<td>");
            b.append("2");
            b.append("</td>");
            b.append("<td>");
            b.append("3");
            b.append("</td>");
            b.append("<td>");
            b.append("4");
            b.append("</td>");
            b.append("<td>");
            b.append("5");
            b.append("</td>");
            b.append("<td>");
            b.append("6");
            b.append("</td>");
            b.append("<td>");
            b.append("7");
            b.append("</td>");
            b.append("<td>");
            b.append("8");
            b.append("</td>");
            b.append("<td>");
            b.append("9");
            b.append("</td>");
            b.append("<td>");
            b.append("10");
            b.append("</td>");
            b.append("<td>");
            b.append("11");
            b.append("</td>");
            b.append("<td>");
            b.append("12");
            b.append("</td>");

            b.append("</tr>");
            b.append("<tr>");
            b.append("</tr>");

            b.append("<tr>");
            b.append("<td colspan='8'>");
            b.append("<b>" + nameB + "</b>" + "&nbsp; Farbe: ______________");
            b.append("</td>");
            b.append("<td colspan='4'>");
            b.append("Tore: ");
            b.append("</td>");
            b.append("</tr>");

            b.append("<tr align='center'>");
            b.append("<td>");
            b.append("1");
            b.append("</td>");
            b.append("<td>");
            b.append("2");
            b.append("</td>");
            b.append("<td>");
            b.append("3");
            b.append("</td>");
            b.append("<td>");
            b.append("4");
            b.append("</td>");
            b.append("<td>");
            b.append("5");
            b.append("</td>");
            b.append("<td>");
            b.append("6");
            b.append("</td>");
            b.append("<td>");
            b.append("7");
            b.append("</td>");
            b.append("<td>");
            b.append("8");
            b.append("</td>");
            b.append("<td>");
            b.append("9");
            b.append("</td>");
            b.append("<td>");
            b.append("10");
            b.append("</td>");
            b.append("<td>");
            b.append("11");
            b.append("</td>");
            b.append("<td>");
            b.append("12");
            b.append("</td>");
            b.append("</table>");

            listT.add(b.toString());
        }

        int i = 0;

        for (final String string : listT) {

            if (i % 2 == 0) {

                if ((i % 12 == 0) && (i > 0)) {
                    responseString = responseString + "<table class='bb' border='0' cellspacing='0' cellpadding='3' width='750' style=\"page-break-after:always;\">";
                } else {
                    responseString = responseString + "<table border='0' cellspacing='0' cellpadding='3' width='750'>";
                }

                responseString = responseString + "<tr>";
            }
            responseString = responseString + "<td> " + string + "</td>";
            if (i % 2 == 2) {
                responseString = responseString + "</tr>";

                responseString = responseString + "</table>";

            }
            i++;
        }

        responseString = responseString + "<br>";


        return xhtml.cleanup(responseString, true);
    }

    public String getTable(final List<Spiel> list) {
        String responseString = "";
        final List<String> listT = new ArrayList<String>();

        int k = 0;
        for (final Spiel spiel : list) {


            if (spiel.getPlatz() == null) {
                HTMLSchiriConverter.LOG.warn("Spiel gefunden ohne Platz... werde dieses überspringen... " + spiel.toString());
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

            final StringBuilder b = new StringBuilder();


            // zeilenumbruch

            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String ret = getTemplate2().replace("[zeit]",sdf.format(spiel.getStart()));
            ret = ret.replace("[idstring]",spiel.getIdString().toUpperCase());
            ret = ret.replace("[platz]",spiel.getPlatz().toString());
            ret = ret.replace("[a]",nameA);
            ret = ret.replace("[b]",nameB);


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

                responseString = responseString + "<tr>";
            }
            responseString = responseString + "<td> " + string + "</td>";
            if (i % 2 == 2) {
                responseString = responseString + "</tr>";

                responseString = responseString + "</table>";

            }
            i++;
        }

        responseString = responseString + "<br>";


        return xhtml.cleanup(responseString, true);
    }




    private String getTemplate2(){

        final StringBuilder b = new StringBuilder();

        b.append("<table style=\"border:2px solid black;\" border='2' cellpadding=\"0\" cellspacing=\"0\" height=\"122\"");
        b.append("width=\"350\">");
        b.append("<tbody>");
        b.append("<tr>");
        b.append("<td colspan=\"2\"><b>&nbsp;Platz [platz] um [zeit]&nbsp;[idstring]&nbsp;</b> </td>");
        b.append("</tr>");
        b.append("<tr>");
        b.append("<td colspan=\"1\" align=\"left\"><b>&nbsp;[a]</b>&nbsp; Farbe:");
        b.append("______________&nbsp;&nbsp; Tore:</td>");
        b.append("<td rowspan=\"4\" colspan=\"1\"><img src='resources/static/barcode/[idstring].png'");
        b.append("height=\"85\" width=\"85\"></td>");
        b.append("</tr>");
        b.append("<tr align=\"center\">");
        b.append("<td colspan=\"1\" rowspan=\"1\">" +

                "&nbsp;1 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "2 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "3 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "4 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "5 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "6 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "7 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "8 <input name=\"1\" value=\"1\" type=\"radio\">" +

                "</td>");
        b.append("</tr>");
        b.append("<tr>");
        b.append("<td colspan=\"1\" align=\"left\"><b>&nbsp;[b]</b>&nbsp; Farbe:");
        b.append("______________&nbsp;&nbsp; Tore:</td>");
        b.append("</tr>");
        b.append("<tr align=\"center\">");
        b.append("<td colspan=\"1\" rowspan=\"1\">" +

                "&nbsp;1 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "2 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "3 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "4 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "5 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "6 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "7 <input name=\"1\" value=\"1\" type=\"radio\">" +
                "8 <input name=\"1\" value=\"1\" type=\"radio\">" +

                "</td>");
        b.append("</tr>");
        b.append("</tbody>");
        b.append("</table>");
        return b.toString() ;
    }


}