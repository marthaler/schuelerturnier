package com.googlecode.madschuelerturnier;

import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumEintragerThreadOld extends Thread {

    private static final Logger LOG = Logger.getLogger(SeleniumEintragerThreadOld.class);

    private SeleniumDriverWrapper util = new SeleniumDriverWrapper();

    int i = 0;

    @Override
    public void run() {

        Thread.currentThread().setName("O_EINTR");

        for (int i = 0; i < 20000; i++) {
            this.util.clickByLink("Abmelden");
            this.util.sleepAMoment(1);
            this.util.login("tester1915eintrager", "1234");
            this.util.sleepAMoment(1);
            this.util.clickById("form:ac:weiter");


            if (MatchOnly.restart) {
                break;
            }

            this.util.clickByLink("Eintrager");
            this.util.clickByLink("Schirizettelimport");


            // penalty eintragen

            if (this.util.getSourceAsString().contains("form1:dataTablePen:0:j_idt52")) {
                this.util.sendByName("form1:dataTablePen:0:j_idt52", "irgendwas");
                this.util.clickById("form1:dataTablePen:0:j_idt56");
            }

            String id = findFirstID(this.util.getSourceAsString());


            if(SeleniumEintragerThread.getIdExt() == null || SeleniumEintragerThread.getIdExt().isEmpty()){
                SeleniumEintragerThread.setIdExt(id);
            }

            this.util.sleepAMoment(30);


        }

        this.util.distroy();

    }

    public static void main(final String[] args) {
        final SeleniumEintragerThreadOld th = new SeleniumEintragerThreadOld();
        th.run();
        try {
            th.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }



    private String findFirstID(String str){

        String ret =  StringUtils.substringBetween(str,"class=\"ui-dialog-title\">Schirizettel: ","</span><a");
        if(ret != null && ret.length() == 2){
            return ret;
        }
        return null;
    }

}
