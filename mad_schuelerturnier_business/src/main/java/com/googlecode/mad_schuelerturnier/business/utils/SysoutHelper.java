/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.utils;

import com.googlecode.mad_schuelerturnier.model.Kategorie;

import java.util.*;


/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SysoutHelper {

    public static void printKategorieMap(Map<String, Kategorie> map) {
        System.out.println("");
        System.out.println("*************************************");
        Set<String> keys = map.keySet();
        List<String> str = new ArrayList<String>();
        for (String string : keys) {
            str.add(string);
        }
        Collections.sort(str);

        for (String key : str) {

            System.out.print(key + " ");
            String name = (map.get(key).getName() + "                           ").substring(0, 10);

            System.out.println(" " + name + "--> " + map.get(key).getGruppeA().getMannschaften());
            if (map.get(key).getGruppeB() != null) {
                System.out.println("              --> " + map.get(key).getGruppeB().getMannschaften());
            } else {
                System.out.println("              --> ");
            }

            System.out.println("              --> spiele: " + map.get(key).getSpiele().size());

            System.out.println("");
        }
        System.out.println("*************************************");
        System.out.println("");
    }

    public static void printKategorieList(List<Kategorie> map) {
        System.out.println("");
        System.out.println("*************************************");


        for (Kategorie key : map) {
            String name = (key.getName() + "                           ").substring(0, 10);
             if(key.getGruppeA() == null){
                                        continue;
             }
            System.out.println(" " + name + "   --> " + key.getGruppeA().getMannschaftenSorted());
            if (key.getGruppeB() != null) {
                System.out.println("              --> " + key.getGruppeB().getMannschaftenSorted());
            } else {
                System.out.println("              --> ");
            }

            System.out.println("              -->       spiele: " + key.getSpiele().size());
            System.out.println("              --> mannschaften: " + key.getMannschaften().size());

            System.out.println("");
        }
        System.out.println("*************************************");
        System.out.println("");
    }

}
