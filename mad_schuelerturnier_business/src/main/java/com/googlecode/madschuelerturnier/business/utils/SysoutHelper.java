/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.utils;

import com.googlecode.madschuelerturnier.model.Kategorie;
import org.apache.log4j.Logger;

import java.util.*;


/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SysoutHelper {

    public static final String BEGRENZER = "*************************************";
    private static final Logger LOG = Logger.getLogger(SysoutHelper.class);

    public static void printKategorieMap(Map<String, Kategorie> map) {
        LOG.info("" + "");
        LOG.info("" + BEGRENZER);
        Set<String> keys = map.keySet();
        List<String> str = new ArrayList<String>();
        for (String string : keys) {
            str.add(string);
        }
        Collections.sort(str);

        for (String key : str) {

            LOG.info("" + key + " ");
            String name = (map.get(key).getName() + "                           ").substring(0, 10);

            LOG.info("" + " " + name + "--> " + map.get(key).getGruppeA().getMannschaften());
            if (map.get(key).getGruppeB() != null) {
                LOG.info("" + "              --> " + map.get(key).getGruppeB().getMannschaften());
            } else {
                LOG.info("" + "              --> ");
            }

            LOG.info("" + "              --> spiele: " + map.get(key).getSpiele().size());

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

            LOG.info("" + "");
        }
        LOG.info("" + BEGRENZER);
        LOG.info("");
    }

}
