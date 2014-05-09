package com.googlecode.madschuelerturnier;

import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper;
import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper2;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumEintragerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(SeleniumEintragerThread.class);

    private SeleniumDriverWrapper2 util = new SeleniumDriverWrapper2();

    int i = 0;

    public synchronized static String getIdExt() {
        return idExt;
    }

    public synchronized static void setIdExt(String idExt) {
        SeleniumEintragerThread.idExt = idExt;
    }

    private static String idExt = "";

    @Override
    public void run() {

        Thread.currentThread().setName("EINTR");

        util.login("","");
        for (int i = 0; i < 20000; i++) {
try{
            if (MatchOnly.restart) {
                break;
            }


            if(idExt == null || idExt.isEmpty()){
                this.util.sleepAMoment(5);
                continue;

            }

LOG.info("EINTRAGEN: " + idExt);
                this.util.sleepAMoment(3);
                this.util.clickById("form1:j_idt25:up");
                this.util.sleepAMoment(3);

                this.util.sendById("form1:j_idt25:j_idt35", idExt);
                this.util.sleepAMoment(3);


                this.util.clickById("form1:j_idt25:suchen");

            this.util.sleepAMoment(3);

            final String str = this.util.getSourceAsString();

            final Pattern pattern = Pattern.compile("([M]|[K])[0-9]{3}");
            final Matcher matcher = pattern.matcher(str);

            final List<String> mannschaften = new ArrayList<String>();


            while (matcher.find()) {
                mannschaften.add(matcher.group());
            }



            final String mansch = mannschaften.get(0);
            final String m = mansch.substring(2, 4);
            this.util.sleepAMoment(2);

            this.util.sendById("form1:j_idt25:ToreA", m);
            this.util.sleepAMoment(2);
            final String mansch2 = mannschaften.get(1);
            final String m2 = mansch2.substring(2, 4);
            this.util.sendById("form1:j_idt25:ToreB", m2);
            this.util.sleepAMoment(2);

            this.util.clickById("form1:j_idt25:save");

            this.util.sleepAMoment(1);
            this.i = 0;
} catch (Exception e){
    LOG.error(e.getMessage(),e);
}

            this.setIdExt("");

        }

        }


    private String findFirstID(String str){

        String ret =  StringUtils.substringBetween(str,"class=\"ui-dialog-title\">Schirizettel: ","</span><a");
        if(ret != null && ret.length() == 2){
            return ret;
        }
        return null;
    }

    public static void main(final String[] args) {
        final SeleniumEintragerThread th = new SeleniumEintragerThread();
        th.run();
        try {
            th.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

}
