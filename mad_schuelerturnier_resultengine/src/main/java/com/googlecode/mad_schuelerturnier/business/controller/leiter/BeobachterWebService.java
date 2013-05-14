/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.controller.leiter;

import com.googlecode.mad_schuelerturnier.business.controller.leiter.converter.HTMLOutConverter;
import com.googlecode.mad_schuelerturnier.model.Gruppe;
import com.googlecode.mad_schuelerturnier.model.compusw.RanglisteneintragHistorie;
import com.googlecode.mad_schuelerturnier.model.compusw.RanglisteneintragZeile;
import com.googlecode.mad_schuelerturnier.model.enums.SpielEnum;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * mad letzte aenderung: $Date: 2012-01-08 15:40:58 +0100 (So, 08 Jan 2012) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 124 $
 * @headurl $HeadURL: https://mad-schuelerturnier.googlecode.com/svn/trunk/mad_schuelereturnier /src/main/java/com/googlecode/mad_schuelerturnier/business/ controller/leiter/BeobachterWebService.java
 * $
 */
public class BeobachterWebService {

    private static final String HTML_MENUJUMP = "src/main/resources/menujump_template.html";

    private static final Logger LOG = Logger.getLogger(BeobachterWebService.class);

    private final String html_start = "<html><head><title>aaa-Webinterface</title></head>" + "<STYLE TYPE='text/css'>" + "table.peakhere {page-peak-before: always}" + "</STYLE><body>";
    static final String HTML_END = "</body></html>";

    private String name = "test";

    private final HTMLOutConverter con = new HTMLOutConverter();


    public void setName(final String name) {
        this.name = name;
    }


    public void go(String currentLine) {


        final String postBoundary = null;
        final String contentength = null;
        final String filename = null;
        final String contentLength = null;
        final PrintWriter fout = null;
        try {


            final StringTokenizer tokenizer = new StringTokenizer(currentLine);
            final String httpMethod = tokenizer.nextToken();
            String httpQueryString = tokenizer.nextToken();

            String responseString = this.html_start.replace("aaa", this.name);

//            responseString = responseString + FileUtil.readFileAsString(BeobachterWebService.HTML_MENUJUMP);


            final String gruppensuche = httpQueryString;

            //           responseString = HTMLMenu.generateMenu(responseString, gruppensuche);

            String unterGr = "";

            if (httpQueryString.contains("A")) {
                unterGr = "A";
                httpQueryString = httpQueryString.replace("A", "");
            }

            if (httpQueryString.contains("B")) {
                unterGr = "B";
                httpQueryString = httpQueryString.replace("B", "");
            }

            if (httpQueryString.contains("gruppe=")) {
                final String[] str = httpQueryString.split("=");
//                    responseString = this.getTable(responseString, SpielAblauf.getInstance().getMan().getGruppen().get(str[1]), unterGr);
                responseString = responseString + "<b>";
                responseString = responseString + BeobachterWebService.HTML_END;

            }

            if (httpQueryString.contains("_r")) {

                // HTMLOutConverter con = new HTMLOutConverter();
                // responseString = con.generateMenu(responseString, gruppensuche, false);
                //                   responseString = responseString + new HTMLConverterRangliste().generateOutWebsiteForKategorie(SpielAblauf.getInstance().getMan().getGruppen().values());

            }


            if (httpQueryString.contains("_m")) {

                String[] s = httpQueryString.split("_m");
                String query = s[1];

                String[] q = query.split(",");

//                    SpielAblauf.getInstance().generalmurks(q);
            }

            if (httpQueryString.contains("_g")) {

                HTMLOutConverter con = new HTMLOutConverter();
                //     responseString = con.generateMenu(responseString, gruppensuche, false);
                //                   responseString = responseString + new HTMLConverterRangliste().printOutGere(SpielAblauf.getInstance().getMan().getGruppen().values());

            }

            if (httpQueryString.contains("display1")) {
                //responseString = this.con.gestSpileMatrix(responseString);
            }


            if (httpQueryString.contains("_s")) {

                final String gruppe = httpQueryString.replace("/_s_", "");

                //               responseString = this.con.generatePageKategorie(responseString, gruppe, false);

                responseString = responseString + "<b>";
                responseString = responseString + BeobachterWebService.HTML_END;

            }

            System.out.println(responseString);

        } catch (final Exception e) {
            BeobachterWebService.LOG.error(e.getMessage(), e);
        }

    }


    private String getTable(final String responseString, final Gruppe gr, final String untergruppe) {

        if (gr == null) {
            return "nicht vorhanden";
        }

        final StringBuffer b = new StringBuffer();
        final LinkedList<Long> list = new LinkedList<Long>();

        Map<Long, RanglisteneintragHistorie> historien = null;
 /*
        if ("A".equals(untergruppe)) {
            historien = gr.getRanglistenverlaufA();
        } else if ("B".equals(untergruppe)) {
            historien = gr.getRanglistenverlaufB();
        } else {
            historien = gr.getRanglistenverlauf();

        }
        */
        final Set<Long> v = historien.keySet();

        for (final Long long1 : v) {
            list.addFirst(long1);
        }

        int i = list.size();

        for (final Long key : list) {

            final RanglisteneintragHistorie str = historien.get(key);

            boolean penalty = false;

            Spiel sp = null;

            if (str.getSpiel() == null) {
                penalty = true;
            } else {
                sp = str.getSpiel();
            }

            b.append("<table class='normal'>");

            b.append("<tr>");
            b.append("<td colspan='8'>");
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            if (!penalty) {

                if (sp.getTyp() == SpielEnum.GFINAL) {
                    b.append("<b>" + gr.getName().replace(".", "") + " - " + sdf.format(str.getSpiel().getStart()) + " - Finale </b>");
                } else if (sp.getTyp() == SpielEnum.KFINAL) {
                    b.append("<b>" + gr.getName().replace(".", "") + " - " + sdf.format(str.getSpiel().getStart()) + " - kleiner Finale</b>");
                } else {
                    //            b.append("<b>" + gr.getName().replace(".", "") + " - " + sdf.format(str.getSpiel().getStart()) + " - Spiele: " + i + "/" + gr.countGruppenspiele() + "</b>");
                }

            } else {
                //        b.append("<b>" + gr.getName().replace(".", "") + " - Penalty: " + (i - gr.countGruppenspiele()) + "</b>");
            }

            b.append("</td>");
            b.append("</tr>");

            b.append("<tr>");
            b.append("<td colspan='8'>");
            if (!penalty) {
//                b.append("Spiel: " + str.getSpiel().getA().getName() + "-" + str.getSpiel().getB().getName() + " " + str.getSpiel().getToreABestaetigt() + ":" + str.getSpiel().getToreBBestaetigt()
//                        + "");
            } else {
                b.append("Spiel: Penalty");
            }
            b.append("</td>");
            b.append("</tr>");

            b.append("<tr>");
            b.append("<td colspan='1'>");
            b.append("<b>Mannschaft</b>");
            b.append("</td>");

            b.append("<td colspan='1'>");
            b.append("<b>Spiele gesp. / anst.</b>");
            b.append("</td>");


            b.append("<td colspan='1'>");
            b.append("<b>Punkte</b>");
            b.append("</td>");

            b.append("<td colspan='1'>");
            b.append("<b>Tordiff. (erz.-erh.)</b>");
            b.append("</td>");

            b.append("<td colspan='1'>");
            b.append("<b>Grund</b>");
            b.append("</td>");
            b.append("</tr>");
            i--;
            final List<RanglisteneintragZeile> zeilen = str.getZeilen();

            for (final RanglisteneintragZeile ranglisteneintragZeile : zeilen) {
                b.append("<tr>");

//                if (ranglisteneintragZeile.getMannschaft().isMemberofGrupA()) {
//                    b.append("<td colspan='1'>");
//                } else {
//                    b.append("<td colspan='1' bgcolor='gray'>");
//                }

                b.append("" + ranglisteneintragZeile.getMannschaft().getName() + "");
                b.append("</td>");

                b.append("<td colspan='1'>");
                b.append("" + ranglisteneintragZeile.getSpieleVorbei() + "/" + ranglisteneintragZeile.getSpieleAnstehend());
                b.append("</td>");


                b.append("<td colspan='1'>");
                b.append("" + ranglisteneintragZeile.getPunkte() + "");
                b.append("</td>");


                b.append("<td colspan='1'>");
                b.append("" + ranglisteneintragZeile.getToreErziehlt() + " - " + ranglisteneintragZeile.getToreKassiert() + " = " + ranglisteneintragZeile.getTordifferenz());
                b.append("</td>");


                final int diferenz = str.compareWithLast(ranglisteneintragZeile);

                if (diferenz < 0) {
                    b.append("<td colspan='1' bgcolor='red'>");
                } else if (diferenz > 0) {
                    b.append("<td colspan='1' bgcolor='green'>");
                } else {
                    b.append("<td colspan='1'>");
                }

                b.append("" + ranglisteneintragZeile.getRangierungsgrund() + "");

                b.append("</td>");

                b.append("</tr>");
            }

            b.append("</table>");

            b.append("<br>");

        }

        return responseString + b.toString();
    }


}