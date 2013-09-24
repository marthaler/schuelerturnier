/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.print;

import com.googlecode.madschuelerturnier.business.controller.leiter.converter.XHTMLOutputUtil;
import com.googlecode.madschuelerturnier.model.Spiel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class SpielPrintManager {

    private static final Logger LOG = Logger.getLogger(SpielPrintManager.class);

    private static final Integer SPIELE_PER_PAGE = 15;
    public static final String TD_TD = "</td><td>";

    private List<Spiel> aktuelleSpiele = new ArrayList<Spiel>();

    private Set<Spiel> spielSet = new HashSet<Spiel>();

    private Map<Integer, String> savedPages = new HashMap<Integer, String>();

    // idee war, dass beim admin gui ausgeschlatet werden kann
    private boolean print = true;

    @Autowired
    private XHTMLOutputUtil cleaner;

    @Autowired
    private PrintAgent printAgent;

    public SpielPrintManager() {
        LOG.info("instanziert: SpielPrintManager");
    }

    public void saveSpiel(Spiel spiel) {

        if (!spielSet.contains(spiel)) {
            aktuelleSpiele.add(spiel);
        }

        if (aktuelleSpiele.size() >= SPIELE_PER_PAGE && print) {
            printPage();

        }
        spielSet.add(spiel);
    }

    public void printPage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table padding=\"4\" style=\"font-size: 16px\" border=\"1\">");

        stringBuilder.append("<tr>");
        stringBuilder.append("<td>");
        stringBuilder.append("<b>Start</b>");
        stringBuilder.append(TD_TD);
        stringBuilder.append("<b>Platz</b>");
        stringBuilder.append("</td><td colspan=\"2\">");
        stringBuilder.append("<b>Mannschaften</b>");
        stringBuilder.append(TD_TD);
        stringBuilder.append("<b>Tore</b>");
        stringBuilder.append("</td></tr>");

        for (Spiel spiel : aktuelleSpiele) {
            stringBuilder.append("<tr><td>");
            SimpleDateFormat form = new SimpleDateFormat("E HH:mm");
            stringBuilder.append(form.format(spiel.getStart()));
            stringBuilder.append(TD_TD);
            stringBuilder.append(spiel.getPlatz());
            stringBuilder.append(TD_TD);
            stringBuilder.append(spiel.getMannschaftAName());
            stringBuilder.append(TD_TD);
            stringBuilder.append(spiel.getMannschaftBName());
            stringBuilder.append(TD_TD);
            stringBuilder.append(spiel.getToreABestaetigt());
            stringBuilder.append(" : ");
            stringBuilder.append(spiel.getToreBBestaetigt());
            stringBuilder.append("</td></tr>");
        }

        stringBuilder.append("<tr><td align=\"right\" colspan=\"5\">");

        int pageN = savedPages.size();
        pageN = pageN + 1;

        stringBuilder.append("Seite: " + pageN);

        stringBuilder.append("</td></tr>");

        stringBuilder.append("<table>");

        String page = this.cleaner.cleanup(stringBuilder.toString(), false);

        this.savedPages.put(pageN, page);

        this.aktuelleSpiele.clear();

        printAgent.saveFileToPrint("" + pageN, page);

    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }
}
