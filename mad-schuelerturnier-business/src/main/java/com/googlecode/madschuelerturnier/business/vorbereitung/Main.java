package com.googlecode.madschuelerturnier.business.vorbereitung;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dama on 14.04.14.
 */
public class Main {

    public static void main (String [] arr){
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

        Date date2 = new Date();
        System.out.println(date2);

        System.out.println(TimeZone.getDefault());

      for(String id :TimeZone.getAvailableIDs()){
          System.out.println(id);
      }
    }
}
