/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.controller.leiter.converter;

import com.googlecode.madschuelerturnier.model.*;
import com.googlecode.madschuelerturnier.model.comperators.SpielMannschaftsnamenComperator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class HTMLSpielMatrixConverter {

    @Autowired
    private XHTMLOutputUtil xhtml;

    private final SimpleDateFormat sdf = new SimpleDateFormat("E HH:mm");

    private static final Logger LOG = Logger.getLogger(HTMLSpielMatrixConverter.class);

    public String generateSpieleTable(final List<Kategorie> list) {   // NOSONAR

        final StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append("<table border='1' cellspacing='0' cellpadding='3' width='700'>");

        for (final Kategorie kat : list) {

            if (kat.getGruppeA() == null) {
                LOG.info("kategorie ohne gruppe a: " + kat.getName());
                continue;
            }

            String col = "";

            if (kat.hasVorUndRueckrunde()) {
                col = "bgcolor='green'";

            } else if (!kat.hasVorUndRueckrunde() && (kat.getGruppeB() != null) && !kat.getGruppeB().getMannschaften().isEmpty()) {
                col = "bgcolor='blue'";
            } else {
                col = "bgcolor='cyan'";
            }

            stringBuffer.append("<tr><td width='10' " + col + " rowspan='3'>" + kat.getName().replace(".", "") + "</td>");

            stringBuffer.append("<td>A: " + kat.getGruppeA().getName());

            List<Mannschaft> str = kat.getGruppeA().getMannschaften();
            Boolean rr = null;
            if (kat.hasVorUndRueckrunde()) {
                rr = Boolean.TRUE;
            }

            this.printMannschaften2(stringBuffer, str, rr);
            stringBuffer.append("</td></tr><tr><td>");
            if (kat.getGruppeB() != null) {

                stringBuffer.append("  B: " + kat.getGruppeB().getName());
                str = kat.getGruppeB().getMannschaften();
                if (kat.hasVorUndRueckrunde()) {
                    rr = Boolean.FALSE;
                }
                this.printMannschaften2(stringBuffer, str, rr);
            } else {
                stringBuffer.append("B: -</td><td>&nbsp;</td>");
            }

            stringBuffer.append("</td></tr>");

            stringBuffer.append("</td></tr>");

            final Gruppe a = kat.getGruppeA();
            final Gruppe ub = kat.getGruppeB();

            final List<Spiel> spiele = new ArrayList<Spiel>();

            for (final Mannschaft mannschaft : a.getMannschaften()) {

                spiele.addAll(mannschaft.getSpiele());

            }

            if (ub != null) {

                for (final Mannschaft mannschaft : ub.getMannschaften()) {

                    spiele.addAll(mannschaft.getSpiele());
                }
            }

            try {
                stringBuffer.append("<tr><td>&nbsp;</td><td><p>Letztes Gruppenspiel: " + this.sdf.format(kat.getLatestSpiel().getStart()) + "</p></td></tr>");
            } catch (final Exception e) {
                stringBuffer.append("<tr><td>&nbsp;</td><td><p>Letztes Gruppenspiel: " + "!!!!!!" + "</p></td></tr>");
            }
        }

        stringBuffer.append("</table>");

        return xhtml.cleanup(stringBuffer.toString(), false);
    }


    /**
     * @param stringBuilder
     * @param str
     */
    private void printMannschaften2(final StringBuilder stringBuilder, final List<Mannschaft> str, final Boolean vorrunde) { // NOSONAR

        stringBuilder.append("<td>");
        stringBuilder.append("<style type='text/css'>table.inner {border-spacing: 0px; border-padding:0px;width:100%;border:0px; vertical-align:top; overflow:hidden; font-size:10pt; font-family:Arial,sans-serif }td { border:1px solid #000; vertical-align:top; overflow:hidden; }</style>");
        stringBuilder.append("<table class = 'inner'>");
        stringBuilder.append("<tr><td>&nbsp;</td>");

        for (final Mannschaft m1 : str) {
            stringBuilder.append("<td><b>" + m1.getName() + "</b></td>");
        }
        int iZeile = 0;
        for (final Mannschaft mannschaft : str) {

            stringBuilder.append("<tr><td>");
            stringBuilder.append("<b>" + mannschaft.getName() + "</b>");

            final List<Spiel> tempSpiele = new ArrayList<Spiel>();

            // todo schauen warum paarungen 0
            if ((vorrunde != null && vorrunde) && mannschaft.getSpiele().size() > 0) {
                tempSpiele.add(mannschaft.getSpiele().get(0));
                tempSpiele.add(mannschaft.getSpiele().get(1));
                // todo schauen warum paarungen nur 2
            } else if ((vorrunde != null && !vorrunde) && mannschaft.getSpiele().size() > 2) {
                tempSpiele.add(mannschaft.getSpiele().get(2));
                tempSpiele.add(mannschaft.getSpiele().get(3));
            } else {
                tempSpiele.addAll(mannschaft.getSpiele());
            }


            int iSpalte = 0;
            boolean linefin = false;

            Collections.sort(tempSpiele, new SpielMannschaftsnamenComperator());

            for (final Spiel spiel : tempSpiele) {

                if ((iZeile == iSpalte) || linefin) {
                    stringBuilder.append("<td bgcolor='white'>&nbsp;</td>");
                    linefin = true;
                    continue;
                }

                if (spiel == null) {
                    stringBuilder.append("<td  bgcolor='blue'>");
                } else if (spiel.getToreABestaetigt() > -1) {
                    stringBuilder.append("<td  bgcolor='green'>");
                } else if (spiel.isAmSpielen()) {
                    stringBuilder.append("<td  bgcolor='yellow'>");
                } else {
                    stringBuilder.append("<td>");
                }

                if (spiel == null) {
                    stringBuilder.append("!!!");
                } else {
                    printSpiel(stringBuilder, spiel);
                }

                stringBuilder.append("</td>");

                iSpalte++;
            }

            stringBuilder.append("<td bgcolor='white'>&nbsp;</td>");

            stringBuilder.append("</tr>");
            iZeile++;
        }

        stringBuilder.append("</table>");
        stringBuilder.append("</td>");
    }

    private void printSpiel(StringBuilder stringBuilder, Spiel spiel) {
        stringBuilder.append(spiel.getPlatz() + ";" + spiel.getIdString() + ";");
        if (spiel.getStart() != null) {
            stringBuilder.append(this.sdf.format(spiel.getStart()));
        }
    }
}
