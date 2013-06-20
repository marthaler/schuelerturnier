/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.controller.leiter.converter;

import com.googlecode.mad_schuelerturnier.model.Kategorie;
import com.googlecode.mad_schuelerturnier.model.compusw.RanglisteneintragHistorie;
import com.googlecode.mad_schuelerturnier.model.compusw.RanglisteneintragZeile;
import com.googlecode.mad_schuelerturnier.model.enums.SpielEnum;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.KategorieRepository;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * mad letzte aenderung: $Date: 2012-01-08 15:40:58 +0100 (So, 08 Jan 2012) $
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @version $Revision: 124 $
 * @headurl $HeadURL: https://mad-schuelerturnier.googlecode.com/svn/trunk/mad_schuelereturnier /src/main/java/com/googlecode/mad_schuelerturnier/business/
 * controller/leiter/converter/HTMLOutConverter.java $
 */
@Component
public class HTMLOutConverter {

    private static final Logger LOG = Logger.getLogger(HTMLOutConverter.class);

    private static String TABLE = "<style type='text/css'>table.normal {border-spacing: 0px; border-padding:0px;width:600px;border:1px solid #000; vertical-align:top; overflow:hidden; font-size:10pt; font-family:Arial,sans-serif }td { border:1px solid #000; vertical-align:top; overflow:hidden; }</style><table class='normal'>";


    @Autowired
    private XHTMLOutputUtil util;

    @Autowired
    private HTMLMenu menu;

    @Autowired
    private KategorieRepository katRepo;

    private String path = "";

    // hystorie fuer die webapp (mit den historien)
    public String getRangliste(RanglisteneintragHistorie historie) {

        if (historie == null) {
            return "";
        }

        final StringBuffer buffer = new StringBuffer();

        boolean penalty = false;

        Kategorie kategorie = historie.getKategorie();

        boolean first = true;
        do {
            if (!first) {
                historie = historie.getVorherigerEintrag();
            }

            Spiel spiel = historie.getSpiel();

            first = false;

            buffer.append("<br/>");
            buffer.append("<table border='1' cellspacing='0' cellpadding='3' width='700'>");

            buffer.append("<tr>");
            buffer.append("<td colspan='8'>");
            final SimpleDateFormat sdf = new SimpleDateFormat("EEE HH:mm:ss");

            if (spiel != null) {
                buffer.append("<p>");
                if (spiel.getTyp() == SpielEnum.GFINAL) {
                    buffer.append(kategorie.getName() + " - " + sdf.format(historie.getSpiel().getStart()) + " - Finale </p>");
                } else if (spiel.getTyp() == SpielEnum.KFINAL) {
                    buffer.append(kategorie.getName() + " - " + sdf.format(historie.getSpiel().getStart()) + " - kleiner Finale</p>");
                } else {

                    // TODO weg
                    if (kategorie.getGruppeB() != null) {
                        buffer.append(kategorie.getGruppeA().getName() + /*kategorie.getGruppeB()*/  " - " + sdf.format(historie.getSpiel().getStart()) + ""  /*historie.countGruppenspieleGespielt() + "/" + historie.countGruppenspiele() + "</p>   *** eine Teilgruppe ist grau hinterlegt, die andere weiss"*/);
                    } else {
                        buffer.append(kategorie.getName() + " - " + sdf.format(historie.getSpiel().getStart()) + " - Spiele: " + /*historie.countGruppenspieleGespielt() + "/" + historie.countGruppenspiele() +*/ "</p>");
                    }
                }
            } else {
                buffer.append("<p>" + kategorie.getName().replace(".", "") + " - Penalty: " + "</p>");
            }

            buffer.append("</td>");
            buffer.append("</tr>");

            buffer.append("<tr>");
            buffer.append("<td colspan='8'>");
            if (spiel != null) {
                buffer.append("spielkorrektur: " + historie.getSpiel().getMannschaftA().getName() + "-" + historie.getSpiel().getMannschaftB().getName() + " " + historie.getSpiel().getToreABestaetigt() + ":" + historie.getSpiel().getToreBBestaetigt() + "");
            } else {
                buffer.append("spielkorrektur: Penalty");
            }
            buffer.append("</td>");
            buffer.append("</tr>");

            buffer.append("<tr>");
            buffer.append("<td colspan='1'>");
            buffer.append("<p>Mannschaft</p>");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("<p>Sp. gespielt</p>");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("<p>Sp. anstehend</p>");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("<p>Punkte</p>");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("<p>Tordifferenz (Tore erz. - erh.)</p>");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("<p>Rangierungsgrund</p>");
            buffer.append("</td>");

            // zeilen pro hystorieneintrag
            generateZeilen(historie, buffer);

        } while (historie.getVorherigerEintrag() != null);

        return buffer.toString();
    }

    private void generateZeilen(RanglisteneintragHistorie gr, StringBuffer buffer) {

        final List<RanglisteneintragZeile> zeilen = gr.getZeilen();

        for (final RanglisteneintragZeile ranglisteneintragZeile : zeilen) {
            buffer.append("<tr>");

            if (ranglisteneintragZeile.getMannschaft().isMemberofGroupA()) {
                buffer.append("<td colspan='1'>");
            } else {
                buffer.append("<td colspan='1' bgcolor='gray'>");
            }

            buffer.append("" + ranglisteneintragZeile.getMannschaft().getName() + "");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("" + ranglisteneintragZeile.getSpieleVorbei() + "");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("" + ranglisteneintragZeile.getSpieleAnstehend() + "");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("" + ranglisteneintragZeile.getPunkte() + "");
            buffer.append("</td>");

            buffer.append("<td colspan='1'>");
            buffer.append("" + ranglisteneintragZeile.getToreErziehlt() + "-" + ranglisteneintragZeile.getToreKassiert() + "=" + ranglisteneintragZeile.getTordifferenz());
            buffer.append("</td>");

            final int diferenz = gr.compareWithLast(ranglisteneintragZeile);

            if (diferenz < 0) {
                buffer.append("<td colspan='1' bgcolor='red'>");
            } else if (diferenz > 0) {
                buffer.append("<td colspan='1' bgcolor='green'>");
            } else {
                buffer.append("<td colspan='1'>");
            }

            buffer.append("" + ranglisteneintragZeile.getRangierungsgrund().toString() /*.substring(0, 2) + ""*/);

            buffer.append("</td>");

            buffer.append("</tr>");

        }
        buffer.append("</table>");
    }


    public String convertSpiele(final List<Spiel> gruppen, final List<Spiel> finale, final String gruppe) {

        final StringBuilder builder = new StringBuilder();

        builder.append("<br/>");

        builder.append(HTMLOutConverter.TABLE);

        HTMLOutConverter.getTitelzeile(builder, "Gruppenspiele");

        builder.append(HTMLOutConverter.getSpielRow(gruppen, gruppe, true));
        HTMLOutConverter.getTitelzeile(builder, "Finalspiele");
        builder.append(HTMLOutConverter.getSpielRow(finale, gruppe, false));

        builder.append("</table>");
        return builder.toString();

    }

    private static void getTitelzeile(final StringBuilder b, final String name) {
        b.append("<tr>");

        b.append("<tr>");
        b.append("<td colspan='7'>");
        b.append("<b>" + name + "</b>");
        b.append("</td>");
        b.append("</tr>");

        b.append("<td>");
        b.append("<b>Nr.</b>");
        b.append("</td>");

        b.append("<td>");
        b.append("<b>Startzeit</b>");
        b.append("</td>");

        b.append("<td>");
        b.append("<b>Platz</b>");
        b.append("</td>");

        b.append("<td>");
        b.append("<b>Mannschaft A</b>");
        b.append("</td>");

        b.append("<td>");
        b.append("");
        b.append("</td>");

        b.append("<td>");
        b.append("<b>Mannschaft B</b>");
        b.append("</td>");

        b.append("<td>");
        b.append("");
        b.append("</td>");

        b.append("</tr>");

    }

    private static String getSpielRow(final List<Spiel> gruppen, final String gruppe, final boolean gruppenspiele) {
        final StringBuilder builder = new StringBuilder();
        int i = 1;
        for (final Spiel spiel : gruppen) {

            if (spiel == null) {
                continue;
            }

            builder.append("<tr>");

            builder.append("<td>");
            builder.append(i);
            i++;
            builder.append("</td>");

            builder.append("<td>");

            final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");

            builder.append(sdf.format(spiel.getStart()));
            builder.append("</td>");

            builder.append("<td>");
            builder.append(spiel.getPlatz());
            builder.append("</td>");

            builder.append("<td>");
            if (spiel.getMannschaftA() != null) {
                builder.append(spiel.getMannschaftA().getName());
            } else {

                if (spiel.getGruppe() != null) {
                    builder.append(spiel.getTyp() + " " + spiel.getGruppe().getName());
                } else {
                    builder.append(spiel.getTyp() + " -");
                }

            }
            builder.append("</td>");

            if (spiel.getToreABestaetigt() < 0) {
                builder.append("<td bgcolor='red'>");
            } else {
                builder.append("<td>");
                builder.append(spiel.getToreABestaetigt());
            }

            builder.append("</td>");

            builder.append("<td>");
            if (spiel.getMannschaftB() != null) {
                builder.append(spiel.getMannschaftB().getName());
            } else {
                if (spiel.getGruppe() != null) {
                    builder.append(spiel.getTyp() + " " + spiel.getGruppe().getName());
                } else {
                    builder.append(spiel.getTyp() + " -");
                }
            }

            builder.append("</td>");

            if (spiel.getToreBBestaetigt() < 0) {
                builder.append("<td bgcolor='red'>");
            } else {
                builder.append("<td>");
                builder.append(spiel.getToreBBestaetigt());
            }

            builder.append("</tr>");

        }
        return builder.toString();
    }

    public void dumpoutPages() {

        List<Kategorie> liste = katRepo.findAll();

        for (Kategorie kategorie : liste) {
            String page = generatePageKategorie(kategorie);

            try {
                FileUtils.writeStringToFile(new File(this.path + "/website/" + kategorie.getName() + ".html"), page);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public String generatePageIndex() {
        return util.cleanup(menu.generateMenu(""), false);
    }

    public String generatePageKategorie(Kategorie kategorie) {

        final StringBuilder builder = new StringBuilder();

        builder.append(menu.generateMenu(kategorie.getName()));

        List<Spiel> finale = new ArrayList<Spiel>();
        List<Spiel> gruppen = kategorie.getSpieleSorted();


        finale.add(kategorie.getGrosserFinal());
        finale.add(kategorie.getKleineFinal());

        builder.append(this.convertSpiele(gruppen, finale, kategorie.getName()));

        return this.util.cleanup(builder.toString(), false);

    }

    public void setPath(String path) {
        this.path = path + System.getProperty("file.separator") + "sound";
    }

}
