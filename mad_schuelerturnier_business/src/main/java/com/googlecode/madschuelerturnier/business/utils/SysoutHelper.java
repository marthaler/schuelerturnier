/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.utils;

import com.googlecode.madschuelerturnier.model.Kategorie;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SysoutHelper {

    public static final String BEGRENZER = "*************************************";
    private static final Logger LOG = Logger.getLogger(SysoutHelper.class);

    public static void printKategorieMap(List<Kategorie> list) {
        LOG.info("" + "");
        LOG.info("" + BEGRENZER);

        List<String> str = new ArrayList<String>();
        for (Kategorie kat : list) {
            str.add(kat.getName());
        }
        Collections.sort(str);

        for (Kategorie kat : list) {


            LOG.info(kat.getName() + " --> " + kat.getGruppeA().getMannschaften());
            if (kat.getGruppeB() != null) {
                LOG.info("" + "              --> " + kat.getGruppeB().getMannschaften());
            } else {
                LOG.info("" + "              --> ");
            }

            LOG.info("" + "              --> spiele: " + kat.getSpiele().size());

            LOG.info("" + "");
        }
        LOG.info("" + BEGRENZER);
        LOG.info("" + "");
    }

    public static void printKategorieList(List<Kategorie> map) {
        LOG.info("" + "");
        LOG.info("" + BEGRENZER);

        for (Kategorie key : map) {
            String name = (key.getName() + "                           ").substring(0, 10);
            if (key.getGruppeA() == null) {
                continue;
            }
            LOG.info("" + " " + name + "   --> " + key.getGruppeA().getMannschaftenSorted());
            if (key.getGruppeB() != null) {
                LOG.info("" + "              --> " + key.getGruppeB().getMannschaftenSorted());
            } else {
                LOG.info("" + "              --> ");
            }

            LOG.info("" + "              -->       spiele: " + key.getSpiele().size());
            LOG.info("" + "              --> mannschaften: " + key.getMannschaften().size());
        }
        LOG.info("" + BEGRENZER);
        LOG.info("");
    }

}
