/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.serienbriefe;

import ch.emad.model.schuetu.model.Mannschaft;

import java.util.*;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */
public class TemplateMapper {

    public static Collection<Map<String, String>> convertRechnungen(List<Mannschaft> reportable) {

        Collection<Map<String, String>> result = new ArrayList<Map<String, String>>();

        for (Mannschaft rechnung : reportable) {


            Map<String, String> temp = new HashMap<String, String>();

            temp.put("V01-W", "2015");
            temp.put("V02-W", rechnung.getAnrede());
            temp.put("V03-W", rechnung.getNameVorname());
            temp.put("V04-W", rechnung.getStrasse());
            temp.put("V05-W", rechnung.getPLZOrt());
            temp.put("V06-W", "2015");
            temp.put("V07-W", "88");
            temp.put("V08-W", rechnung.getESR());
            temp.put("V09-W", rechnung.getName());
            temp.put("V10-W", "" + rechnung.getAnzahl());
            temp.put("V11-W", rechnung.getBetrag());


        }

        return result;
    }

}