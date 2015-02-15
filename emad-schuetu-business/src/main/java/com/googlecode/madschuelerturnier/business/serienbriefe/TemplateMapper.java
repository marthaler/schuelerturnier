/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.serienbriefe;

import com.googlecode.madschuelerturnier.interfaces.RechnungReportable;
import com.googlecode.madschuelerturnier.model.Mannschaft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */
public class TemplateMapper {

    public static Collection<Map<String,String>> convertRechnungen(List<Mannschaft> reportable){

        Collection<Map<String,String>> result = new ArrayList<Map<String,String>>();

        for(RechnungReportable rep : reportable){

            Map<String,String> map = new HashMap<String,String>();

            map.put("V01",rep.getAnrede());
            map.put("V02",rep.getNameVorname());
            map.put("V03",rep.getStrasse());
            map.put("V04",rep.getPLZOrt());
            map.put("V05","" +rep.getAnzahl());
            map.put("V06",""+rep.getPreis());
            map.put("V07",""+rep.getBetrag());
            map.put("V08","" + rep.getESR());
            map.put("V09","V09");
            map.put("V10","V10");
            map.put("V11","V11");

            result.add(map);
        }
return result;
    }

}