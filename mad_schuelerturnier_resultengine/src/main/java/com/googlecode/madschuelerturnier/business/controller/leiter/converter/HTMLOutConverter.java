/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.controller.leiter.converter;

import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.compusw.RanglisteneintragHistorie;
import com.googlecode.madschuelerturnier.model.compusw.RanglisteneintragZeile;
import com.googlecode.madschuelerturnier.model.enums.SpielEnum;
import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import com.googlecode.madschuelerturnier.persistence.repository.KategorieRepository;
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
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class HTMLOutConverter {

    private static final Logger LOG = Logger.getLogger(HTMLOutConverter.class);

    public static final String TABLE = "<style type='text/css'>table.normal {border-spacing: 0px; border-padding:0px;width:600px;border:1px solid #000; vertical-align:top; overflow:hidden; font-size:10pt; font-family:Arial,sans-serif }td { border:1px solid #000; vertical-align:top; overflow:hidden; }</style><table class='normal'>";


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

        final StringBuilder stringBuilder = new StringBuilder();

        Kategorie kategorie = historie.getKategorie();

        boolean first = true;
        do {
            if (!first) {
                historie = historie.getVorherigerEintrag();
            }

            Spiel spiel = historie.getSpiel();

            first = false;

            stringBuilder.append(HTMLTags.BR);
            stringBuilder.append("<table border='1' cellspacing='0' cellpadding='3' width='700'>");

            stringBuilder.append(HTMLTags.TR);
            stringBuilder.append("<td colspan='8'>");
            final SimpleDateFormat sdf = new SimpleDateFormat("EEE HH:mm:ss");

            if (spiel != null) {
                stringBuilder.append(HTMLTags.P);
                if (spiel.getTyp() == SpielEnum.GFINAL) {
                    stringBuilder.append(kategorie.getName() + " - " + sdf.format(historie.getSpiel().getStart()) + " - Finale </p>");
                } else if (spiel.getTyp() == SpielEnum.KFINAL) {
                    stringBuilder.append(kategorie.getName() + " - " + sdf.format(historie.getSpiel().getStart()) + " - kleiner Finale</p>");
                } else {
                    // TODO weg
                    if (kategorie.getGruppeB() != null) {
                        stringBuilder.append(kategorie.getGruppeA().getName() + /*kategorie.getGruppeB()*/  " - " + sdf.format(historie.getSpiel().getStart()) + ""  /*historie.countGruppenspieleGespielt() + "/" + historie.countGruppenspiele() + "</p>   *** eine Teilgruppe ist grau hinterlegt, die andere weiss"*/);
                    } else {
                        stringBuilder.append(kategorie.getName() + " - " + sdf.format(historie.getSpiel().getStart()) + " - Spiele: " + /*historie.countGruppenspieleGespielt() + "/" + historie.countGruppenspiele() +*/ "</p>");
                    }
                }
            } else {
                stringBuilder.append("<p>" + kategorie.getName().replace(".", "") + " - Penalty: " + HTMLTags.P_E);
            }

            stringBuilder.append(HTMLTags.TD_E);
            stringBuilder.append(HTMLTags.TR_E);

            stringBuilder.append(HTMLTags.TR);
            stringBuilder.append("<td colspan='8'>");
            if (spiel != null) {
                stringBuilder.append("spielkorrektur: " + historie.getSpiel().getMannschaftA().getName() + "-" + historie.getSpiel().getMannschaftB().getName() + " " + historie.getSpiel().getToreABestaetigt() + ":" + historie.getSpiel().getToreBBestaetigt() + "");
            } else {
                stringBuilder.append("spielkorrektur: Penalty");
            }
            stringBuilder.append(HTMLTags.TD_E);
            stringBuilder.append(HTMLTags.TR_E);

            stringBuilder.append(HTMLTags.TR);
            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("<p>Mannschaft</p>");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("<p>Sp. gespielt</p>");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("<p>Sp. anstehend</p>");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("<p>Punkte</p>");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("<p>Tordifferenz (Tore erz. - erh.)</p>");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("<p>Rangierungsgrund</p>");
            stringBuilder.append(HTMLTags.TD_E);

            // zeilen pro hystorieneintrag
            generateZeilen(historie, stringBuilder);

        } while (historie.getVorherigerEintrag() != null);

        return stringBuilder.toString();
    }

    private void generateZeilen(RanglisteneintragHistorie gr, StringBuilder stringBuilder) {

        final List<RanglisteneintragZeile> zeilen = gr.getZeilen();

        for (final RanglisteneintragZeile ranglisteneintragZeile : zeilen) {
            stringBuilder.append(HTMLTags.TR);

            if (ranglisteneintragZeile.getMannschaft().isMemberofGroupA()) {
                stringBuilder.append("<td colspan='1'>");
            } else {
                stringBuilder.append("<td colspan='1' bgcolor='gray'>");
            }

            stringBuilder.append("" + ranglisteneintragZeile.getMannschaft().getName() + "");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("" + ranglisteneintragZeile.getSpieleVorbei() + "");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("" + ranglisteneintragZeile.getSpieleAnstehend() + "");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("" + ranglisteneintragZeile.getPunkte() + "");
            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append("<td colspan='1'>");
            stringBuilder.append("" + ranglisteneintragZeile.getToreErziehlt() + "-" + ranglisteneintragZeile.getToreKassiert() + "=" + ranglisteneintragZeile.getTordifferenz());
            stringBuilder.append(HTMLTags.TD_E);

            final int diferenz = gr.compareWithLast(ranglisteneintragZeile);

            if (diferenz < 0) {
                stringBuilder.append("<td colspan='1' bgcolor='red'>");
            } else if (diferenz > 0) {
                stringBuilder.append("<td colspan='1' bgcolor='green'>");
            } else {
                stringBuilder.append("<td colspan='1'>");
            }

            stringBuilder.append(ranglisteneintragZeile.getRangierungsgrund().toString());

            stringBuilder.append(HTMLTags.TD_E);

            stringBuilder.append(HTMLTags.TR_E);

        }
        stringBuilder.append("</table>");
    }


    public String convertSpiele(final List<Spiel> gruppen, final List<Spiel> finale) {

        final StringBuilder builder = new StringBuilder();

        builder.append(HTMLTags.BR);

        builder.append(TABLE);

        HTMLOutConverter.getTitelzeile(builder, "Gruppenspiele");

        builder.append(HTMLOutConverter.getSpielRow(gruppen));
        HTMLOutConverter.getTitelzeile(builder, "Finalspiele");
        builder.append(HTMLOutConverter.getSpielRow(finale));

        builder.append("</table>");
        return builder.toString();

    }

    private static void getTitelzeile(final StringBuilder b, final String name) {
        b.append(HTMLTags.TR);

        b.append(HTMLTags.TR);
        b.append("<td colspan='7'>");
        b.append("<b>" + name + "</b>");
        b.append(HTMLTags.TD_E);
        b.append(HTMLTags.TR_E);

        b.append(HTMLTags.TD);
        b.append("<b>Nr.</b>");
        b.append(HTMLTags.TD_E);

        b.append(HTMLTags.TD);
        b.append("<b>Startzeit</b>");
        b.append(HTMLTags.TD_E);

        b.append(HTMLTags.TD);
        b.append("<b>Platz</b>");
        b.append(HTMLTags.TD_E);

        b.append(HTMLTags.TD);
        b.append("<b>Mannschaft A</b>");
        b.append(HTMLTags.TD_E);

        b.append(HTMLTags.TD);
        b.append(HTMLTags.TD_E);

        b.append(HTMLTags.TD);
        b.append("<b>Mannschaft B</b>");
        b.append(HTMLTags.TD_E);

        b.append(HTMLTags.TD);
        b.append(HTMLTags.TD_E);

        b.append(HTMLTags.TR_E);

    }

    private static String getSpielRow(final List<Spiel> gruppen) {
        final StringBuilder builder = new StringBuilder();
        int i = 1;
        for (final Spiel spiel : gruppen) {

            if (spiel == null) {
                continue;
            }

            builder.append(HTMLTags.TR);

            builder.append("<td>");
            builder.append(i);
            i++;
            builder.append(HTMLTags.TD_E);

            builder.append("<td>");

            final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");

            builder.append(sdf.format(spiel.getStart()));
            builder.append(HTMLTags.TD_E);

            builder.append("<td>");
            builder.append(spiel.getPlatz());
            builder.append(HTMLTags.TD_E);

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
            builder.append(HTMLTags.TD_E);

            if (spiel.getToreABestaetigt() < 0) {
                builder.append("<td bgcolor='red'>");
            } else {
                builder.append("<td>");
                builder.append(spiel.getToreABestaetigt());
            }

            builder.append(HTMLTags.TD_E);

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

            builder.append(HTMLTags.TD_E);

            if (spiel.getToreBBestaetigt() < 0) {
                builder.append("<td bgcolor='red'>");
            } else {
                builder.append("<td>");
                builder.append(spiel.getToreBBestaetigt());
            }

            builder.append(HTMLTags.TR_E);

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

        builder.append(this.convertSpiele(gruppen, finale));

        return this.util.cleanup(builder.toString(), false);

    }

    public void setPath(String path) {
        this.path = path + System.getProperty("file.separator") + "sound";
    }

}
