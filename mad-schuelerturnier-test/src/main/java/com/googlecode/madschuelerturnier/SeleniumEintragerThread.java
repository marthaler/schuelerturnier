package com.googlecode.madschuelerturnier;

import com.googlecode.madschuelerturnier.business.xls.FromXLSLoader;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.util.SeleniumDriverWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumEintragerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(SeleniumEintragerThread.class);

    private SeleniumDriverWrapper util;

    private String url;
    private String password;
    private String user;
    private boolean running = true;
    private String methode;
    private byte[] arr;

    private List<Spiel> spiele = null;

    public SeleniumEintragerThread(String user, String password, String url, String methode, byte[] arr) {
        this.url = url;
        this.password = password;
        this.user = user;
        util = new SeleniumDriverWrapper(url);
        this.methode = methode;
        this.arr = arr;
        FromXLSLoader loader = new FromXLSLoader();
        spiele = loader.convertXLSToSpiele(arr);
    }

    public void shutDown() {
        Thread.currentThread().interrupt();
        running = false;
        util.destroy();
    }

    Set<String> idSpeicher = new HashSet<String>();

    int i = 0;

    int errorcount = 0;

    @Override
    public void run() {

        Thread.currentThread().setName("O_EINTR");

        while (running) {
            try{

                this.util.sleepAMoment();
                this.util.sleepAMoment();
                this.util.getBaseURL();
                this.util.sleepAMoment();
                this.util.sleepAMoment();

            i ++;
            this.util.clickByLink("Abmelden");
            this.util.sleepAMoment();

            this.util.login("tester1915eintrager", "1234");
            this.util.sleepAMoment();

            this.util.clickById("form:ac:weiter");
            this.util.sleepAMoment();
            // penalty eintragen
            if (this.util.getSourceAsString().contains("form1:dataTablePen:0:j_idt32")) {
                this.util.sendByName("form1:dataTablePen:0:j_idt28", "irgendwas");
                this.util.clickById("form1:dataTablePen:0:j_idt32");
                this.util.sleepAMoment();
            }

            String id = findFirstID(this.util.getSourceAsString());


            if (idSpeicher.contains(id)) {
                if(i <5){
                    continue;
                }

            }

            if (SeleniumWebcamThread.getIdExt() == null || SeleniumWebcamThread.getIdExt().isEmpty()) {
                SeleniumWebcamThread.setIdExt(id);
                idSpeicher.add(id);
                i=0;

                String a = "99";
                String b = "99";

                if (methode.equals("echt")) {

                    for (Spiel sp : spiele) {
                        if (sp.getIdString().equals(id)) {
                            a = "" + sp.getToreABestaetigt();
                            b = "" + sp.getToreBBestaetigt();
                        }
                    }

                } else if (methode.equals("random3")) {

                    java.util.Random random = new java.util.Random();
                    a = "" + random.nextInt(3);
                    b = "" + random.nextInt(3);


                } else if (methode.equals("random9")) {

                    java.util.Random random = new java.util.Random();
                    a = "" + random.nextInt(9);
                    b = "" + random.nextInt(9);

                } else if (methode.equals("aufsteigend")) {

                    List<Integer> res = findGoals(this.util.getSourceAsString());

                    a = "" + (10 - res.get(0));
                    b = "" + (10 - res.get(1));


                } else if (methode.equals("absteigend")) {

                    List<Integer> res = findGoals(this.util.getSourceAsString());
                    a = "" + res.get(0).toString();
                    b = "" + res.get(1).toString();
                }

                SeleniumWebcamThread.setaExt(a);
                SeleniumWebcamThread.setbExt(b);
                this.util.sleepAMoment(10);

            }

            this.util.sleepAMoment(3);
            } catch(Exception e){
                LOG.error("!!! " + e.getMessage());
                errorcount ++;

                if(errorcount > 5){
                    this.running = false;
                    SeleniumEintragerThread th = new SeleniumEintragerThread( user,  password,  url,  methode,  arr);
                    th.run();
                }
            }
        }
    }


    private String findFirstID(String str) {
        String ret = StringUtils.substringBetween(str, "Schirizettel für: ", "</span>");
        if (ret != null && ret.length() == 2) {
            return ret;
        }
        return null;
    }

    private List<Integer> findGoals(String str) {
        List<Integer> ret = new ArrayList<Integer>();
        final Pattern pattern = Pattern.compile("([M]|[K])[0-9]{3}");
        final Matcher matcher = pattern.matcher(str);
        final List<String> mannschaften = new ArrayList<String>();
        while (matcher.find()) {
            mannschaften.add(matcher.group());
        }
        final String mansch = mannschaften.get(0);
        final String m = mansch.substring(2, 4);

        final String mansch2 = mannschaften.get(1);
        final String m2 = mansch2.substring(2, 4);

        try {
            ret.add(Integer.parseInt(m));
            ret.add(Integer.parseInt(m2));
        } catch (Exception e) {

        }

        return ret;
    }

}
