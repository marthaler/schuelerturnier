package ch.emad.schuetu.business;

import ch.emad.schuetu.business.util.SeleniumDriverWrapper;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//

/**
 * Created with IntelliJ IDEA. User: dama Date: 02.01.13 Time: 19:30 To change this template use File | Settings | File Templates.
 */
public class SeleniumKontrolliererThread extends Thread {

    private static final Logger LOG = Logger.getLogger(SeleniumEintragerThread.class);

    private SeleniumDriverWrapper util;

    private String url;
    private String password;
    private String user;
    private boolean running = true;

    private int errorcount = 0;

    public SeleniumKontrolliererThread(String user, String password, String url) {
        this.url = url;
        this.password = password;
        this.user = user;
        util = new SeleniumDriverWrapper(url);
    }


    public void shutDown() {
        Thread.currentThread().interrupt();
        running = false;
        util.destroy();
    }

    @Override
    public void run() {

        Thread.currentThread().setName("KONTROLLE");

        this.util.login("tester1915kontrollierer", "1234");
        this.util.sleepAMoment();

        this.util.clickById("form:ac:weiter");

        while (running) {
            try {
                this.util.clickByLink("Kontrollierer");

                this.util.sleepAMoment(2);

                final String str = this.util.getSourceAsString();

                final Pattern pattern = Pattern.compile("([M]|[K])[0-9]{3}");

                final Matcher matcher = pattern.matcher(str);

                final List<String> mannschaften = new ArrayList<String>();

                while (matcher.find()) {
                    mannschaften.add(matcher.group());
                }

                if (mannschaften.size() < 1) {
                    this.util.sleepAMoment(2);
                } else {
                    if ((mannschaften.size() % 4) == 0) {
                        this.util.clickById("form1:dataTable1:0:save");
                    } else {
                        this.util.clickById("form1:dataTable1:0:save");
                    }
                }

            } catch (Exception e) {
                LOG.error("!!! " + e.getMessage());
                errorcount++;

                if (errorcount > 5) {
                    this.running = false;
                    SeleniumKontrolliererThread th = new SeleniumKontrolliererThread(user, password, url);
                    th.run();
                }
            }
        }
    }
}
