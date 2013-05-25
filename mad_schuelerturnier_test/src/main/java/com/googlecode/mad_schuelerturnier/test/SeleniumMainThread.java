/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.test;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class SeleniumMainThread extends Thread implements Shutdownable {

    public static final int TIME_OUT_IN_SECONDS = 5;
    private static final Logger LOG = Logger.getLogger(SeleniumMainThread.class);

    private static boolean mailSendt = false;
    private static boolean m4only = false;
    private static String zeitverschnellerung = "1";
    private int failurecount = 0;
    private Long start = System.currentTimeMillis();
    private Typen screenshotExtension = Typen.MAIN;

    private WebDriver driver = null;

    private SeleniumDriverWrapper util = null;

    private boolean upAndRunning = true;

    public SeleniumMainThread() {
        this.util = new SeleniumDriverWrapper();
    }

    public void go() {
        this.driver = this.util.getDriver();
        this.util.login("t", "t");

        if (SeleniumMainThread.m4only) {
            eintragen("K4");
        } else {
            eintragen("");
        }

        anmeldephase();

        kategoriezuordnung();

        spieltagezuordnung();

        spielfreigabe();

        spielStarten();

        ShutdownableRegistry.getInstance().getShutdownableByKey(Typen.EINTRAGER).start();
        ShutdownableRegistry.getInstance().getShutdownableByKey(Typen.KONTROLLIERER).start();

        this.util.sleepAMoment(20);

        this.start();

    }

    public void eintragen(final String filter) {
        // final DataGenerator gen = new DataGenerator(this, filter);
    }

    public void insertMannschaft(final String captain, final String begleiter, final String klassenbezeichner, final String schulhaus, final int klasse, final String geschlecht, int spieler) {
        this.util.sleepAMoment(2);
        moveToMenu("span.ui-icon.ui-icon-triangle-1-s");

        this.clickByXpath("//a[@id='form_m:mm_hinzu']/span[2]");

        this.sendById("form1:captain_input", captain);

        this.sendById("form1:begleitperson_input", begleiter);

        this.sendById("form1:klasseb", klassenbezeichner);

        this.sendById("form1:schulhaus_input", schulhaus);

        this.clickByXpath("//div[@id='form1:spieler']/div[3]/span");
        this.clickByXpath("//div[@id='form1:spieler_panel']/div/ul/li[" + spieler + "]");

        this.clickByXpath("//div[@id='form1:klasse']/div[3]/span");
        this.clickByXpath("//div[@id='form1:klasse_panel']/div/ul/li[" + klasse + "]");

        if (geschlecht.contains("K")) {

            this.clickByXpath("//div[@id='form1:geschlecht']/div[3]/span");
            this.clickByXpath("//div[@id='form1:geschlecht_panel']/div/ul/li");

        }
        this.clickById("form1:submit");
        checkTimeLimit();
    }

    public void anmeldephase() {
        this.util.sleepAMoment(3);
        moveToMenu("span.ui-icon.ui-icon-triangle-1-s");

        this.clickByXpath("//a[@id='form_m:mm_phasen1']/span[2]");
        this.clickById("form1:anmeldung");
        checkTimeLimit();
    }

    public void kategoriezuordnung() {

        this.util.sleepAMoment(3);
        moveToMenu("span.ui-icon.ui-icon-triangle-1-s");
        this.clickByXpath("//a[@id='form_m:mm_phasen2']/span[2]");
        this.util.sleepAMoment(3);
        this.clickById("form1:katzuordnung");
        this.util.sleepAMoment(5);
        checkTimeLimit();
    }

    public void spieltagezuordnung() {
        this.util.sleepAMoment(3);
        moveToMenu("span.ui-icon.ui-icon-triangle-1-s");
        this.clickByXpath("//a[@id='form_m:mm_einstelung1']/span[2]");
        this.clickById("form1:starttag_input");
        this.sendById("form1:starttag_input", "04.06.11");
        this.clickById("form1:submit");
        this.util.sleepAMoment(5);

        moveToMenu("span.ui-icon.ui-icon-triangle-1-s");
        this.util.sleepAMoment();
        this.clickByXpath("//a[@id='form_m:mm_phasen3']/span[2]");
        this.clickById("form1:spieltagezuordnung");
        this.util.sleepAMoment(20);
        checkTimeLimit();
    }

    public void spielfreigabe() {
        this.util.sleepAMoment(3);
        moveToMenu("span.ui-icon.ui-icon-triangle-1-s");
        this.clickByXpath("//a[@id='form_m:mm_phasen4']/span[2]");
        this.util.sleepAMoment();
        this.clickById("form1:spielzuordnung");
        checkTimeLimit();
    }

    public void spielStarten() {

        this.clickByXpath("//a[@id='form_m:m_spielen']/span");
        this.clickByXpath("//div[@id='form1:aktuellezeit']/div[2]/span");

        this.util.sleepAMoment();
        resetZeit();
        this.util.sleepAMoment();
        resetZeit();
        this.util.sleepAMoment();

        this.util.clickByXpathAndForget("//button[@type='button'][2]");
        checkTimeLimit();

        // zeitverschnellerung

        this.clickByXpath("//div[@id='form1:output']/div[2]/span");
        this.clickByXpath("//div[@id='form1:output_panel']/div/ul/li[" + SeleniumMainThread.zeitverschnellerung + "]");

        this.clickById("form1:start_zeit");

    }

    private void resetZeit() {
        checkTimeLimit();
        try {

            this.clickById("form1:start_input");
            this.clickById("form1:start_input");
            this.sendById("form1:start_input", "08.06.2013 8:30:00");

        } catch (final Exception e) {
            SeleniumMainThread.LOG.error("not found: form1:start_input");
        }
    }

    @Override
    public void run() {

        while (this.upAndRunning) {

            checkTimeLimit();

            try {

                this.driver.findElement(By.xpath("//a[@id='form_m:m_speaker']/span[2]")).click();
                this.util.sleepAMoment();
                // printOutSource();
                this.util.sleepAMoment();
                final WebElement el = this.driver.findElement(By.id("form1:j_idt36:dataTable2:0:j_idt64"));

                if (el != null) {
                    SeleniumMainThread.LOG.info("found");
                    el.click();
                }
            } catch (final Exception e) {
                SeleniumMainThread.LOG.error("not found");
            }

            this.util.sleepAMoment();
            try {
                this.driver.findElement(By.xpath("//a[@id='form_m:m_speaker']/span[2]")).click();
                this.util.sleepAMoment();

                this.util.sleepAMoment();
                final WebElement el2 = this.driver.findElement(By.id("form1:j_idt36:dataTable3:0:j_idt80"));
                if (el2 != null) {
                    SeleniumMainThread.LOG.info("found2");
                    el2.click();
                }
            } catch (final Exception e) {
                SeleniumMainThread.LOG.error("not found 2");
            }

            this.util.sleepAMoment(2);

        }
    }

    protected void clickById(final String selector) {

        this.util.clickById(selector);
    }

    protected void clickByXpath(final String selector) {

        this.util.clickByXpath(selector, false);

    }

    protected void sendById(final String selector, final String keys) {

        this.util.sendById(selector, keys);
    }

    protected void moveToMenu(final String selector) {

        boolean ok = false;

        while (!ok) {
            checkTimeLimit();
            try {

                final WebElement el = this.driver.findElement(By.cssSelector(selector));

                final Actions builder = new Actions(this.driver);

                builder.moveToElement(el).perform();

                this.util.sleepAMoment();

                ok = true;
            } catch (final Exception e) {
                SeleniumMainThread.LOG.info("moveToMenu nok: " + e.getMessage());
                checkFailure();
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e1) {
                    SeleniumMainThread.LOG.error(e1);
                }
            }
        }
        SeleniumMainThread.LOG.info("moveToMenu ok: " + selector);
    }

    protected void checkFailure() {
        this.failurecount++;
        if (this.failurecount > 4) {
            ShutdownableRegistry.getInstance().shutdown(true, "schuetu integrationstest fehlschlag: nach 5 fehlern");
        }
    }

    protected void checkTimeLimit() {
        if ((System.currentTimeMillis() - (this.start + (1000 * 60 * 60 * 4))) > 0) {
            ShutdownableRegistry.getInstance().shutdown(true, "schuetu integrationstest fehlschlag: timeout nach 4 stunden");
        }
    }

    public void setZeitverschnellerung(final String zeitverschnellerung) {
        SeleniumMainThread.zeitverschnellerung = zeitverschnellerung;
    }

    public void setM4only(final boolean m4only) {
        SeleniumMainThread.m4only = m4only;
    }

    public void shutdown() {
        SeleniumMainThread.LOG.info("killing: " + this.getFileExtension());
        this.util.distroy();
        this.upAndRunning = false;
        this.util.takeScreesnshot(this.screenshotExtension);
        this.util.sleepAMoment(3);

    }

    public Typen getFileExtension() {
        return this.screenshotExtension;
    }
}