/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.print;

import com.googlecode.madschuelerturnier.business.controller.leiter.converter.XHTMLOutputUtil;
import com.googlecode.madschuelerturnier.business.templateengine.Templates;
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

    private List<Spiel> aktuelleSpiele = new ArrayList<Spiel>();

    private Set<Spiel> spielSet = new HashSet<Spiel>();

    private Map<Integer, String> savedPages = new HashMap<Integer, String>();

    // idee war, dass beim admin gui ausgeschlatet werden kann
    private boolean print = true;


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
        int pageN = savedPages.size();
        pageN = pageN + 1;

        String page = Templates.printSpiel(aktuelleSpiele, pageN);
        System.out.println(page);
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
