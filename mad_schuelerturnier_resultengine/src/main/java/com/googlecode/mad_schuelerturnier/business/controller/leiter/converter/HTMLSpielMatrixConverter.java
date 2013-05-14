/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.controller.leiter.converter;

import com.googlecode.mad_schuelerturnier.model.Gruppe;
import com.googlecode.mad_schuelerturnier.model.Kategorie;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.Paarung;
import com.googlecode.mad_schuelerturnier.model.comperators.SpielMannschaftsnamenKompertator;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * mad letzte aenderung: $Date: 2012-01-08 15:40:58 +0100 (So, 08 Jan 2012) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 124 $
 * @headurl $HeadURL:
 * https://mad-schuelerturnier.googlecode.com/svn/trunk/mad_schuelereturnier
 * /src/main/java/com/googlecode/mad_schuelerturnier/business/
 * controller/leiter/converter/HTMLConverter.java $
 */
@Component
public class HTMLSpielMatrixConverter {

    @Autowired
    XHTMLOutputUtil xhtml;

    final SimpleDateFormat sdf = new SimpleDateFormat("E HH:mm");

    private static final Logger LOG = Logger.getLogger(HTMLSpielMatrixConverter.class);

    public String generateSpieleTable(final List<Kategorie> list) {

        final StringBuffer stringBuffer = new StringBuffer();

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

                final List<Paarung> pa = mannschaft.getPaarungen();
                for (final Paarung paarung : pa) {
                    spiele.add(paarung.getSpiel());
                }

            }

            if (ub != null) {

                for (final Mannschaft mannschaft : ub.getMannschaften()) {

                    final List<Paarung> pb = mannschaft.getPaarungen();
                    for (final Paarung paarung : pb) {
                        spiele.add(paarung.getSpiel());
                    }
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
     * @param b
     * @param str
     */
    private void printMannschaften2(final StringBuffer b, final List<Mannschaft> str, final Boolean vorrunde) {

        b.append("<td>");
        b.append("<style type='text/css'>table.inner {border-spacing: 0px; border-padding:0px;width:100%;border:0px; vertical-align:top; overflow:hidden; font-size:10pt; font-family:Arial,sans-serif }td { border:1px solid #000; vertical-align:top; overflow:hidden; }</style>");
        b.append("<table class = 'inner'>");
        b.append("<tr><td>&nbsp;</td>");

        for (final Mannschaft m1 : str) {
            b.append("<td><b>" + m1.getName() + "</b></td>");
        }
        int iZeile = 0;
        for (final Mannschaft mannschaft : str) {

            b.append("<tr><td>");
            b.append("<b>" + mannschaft.getName() + "</b>");

            final List<Spiel> tempSpiele = new ArrayList<Spiel>();

            // todo schauen warum paarungen 0
            if ((vorrunde != null) && (vorrunde == true) && mannschaft.getSpiele().size() > 0) {
                tempSpiele.add(mannschaft.getSpiele().get(0));
                tempSpiele.add(mannschaft.getSpiele().get(1));
                // todo schauen warum paarungen nur 2
            } else if ((vorrunde != null) && (vorrunde == false) && mannschaft.getSpiele().size() > 2) {
                tempSpiele.add(mannschaft.getSpiele().get(2));
                tempSpiele.add(mannschaft.getSpiele().get(3));
            } else {
                tempSpiele.addAll(mannschaft.getSpiele());
            }

            int iSpalte = 0;
            boolean linefin = false;

            Collections.sort(tempSpiele, new SpielMannschaftsnamenKompertator());

            for (final Spiel spiel : tempSpiele) {

                if ((iZeile == iSpalte) || linefin) {
                    b.append("<td bgcolor='white'>&nbsp;</td>");
                    linefin = true;
                    continue;
                }

                if (spiel == null) {
                    b.append("<td  bgcolor='blue'>");
                } else if (spiel.getToreABestaetigt() > -1) {
                    b.append("<td  bgcolor='green'>");
                } else if (spiel.isAmSpielen()) {
                    b.append("<td  bgcolor='yellow'>");
                } else {
                    b.append("<td>");
                }

                if (spiel == null) {
                    b.append("!!!");
                } else {
                    printSpiel(b, spiel);
                }

                b.append("</td>");

                iSpalte++;
            }

            b.append("<td bgcolor='white'>&nbsp;</td>");

            b.append("</tr>");
            iZeile++;
        }

        b.append("</table>");
        b.append("</td>");
    }

    private void printSpiel(StringBuffer b, Spiel spiel) {
        b.append(spiel.getPlatz() + ";" + spiel.getIdString() + ";");
        if (spiel.getStart() != null) {
            b.append(this.sdf.format(spiel.getStart()));
        }
    }
}
