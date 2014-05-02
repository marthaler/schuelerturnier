package com.googlecode.madschuelerturnier.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 04.01.13
 * Time: 08:30
 * To change this template use File | Settings | File Templates.
 */
public class SeleniumDriverWrapper {

    private static final Logger LOG = Logger.getLogger(SeleniumDriverWrapper.class);

    private static int TIME_OUT_IN_SECONDS = 5;

    private String baseURL = "http://localhost:8081";

    public WebDriver getDriver() {
        return driver;
    }

    private SafariDriver driver = new SafariDriver();

    private boolean upAndRunning = true;

    public SeleniumDriverWrapper() {

        //driver.setJavascriptEnabled(true);
        driver.manage().timeouts().implicitlyWait(TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);
    }

    public void login(String user, String password) {
        getBaseURL();
        this.sendById("account_form:user", user);
        driver.findElement(By.id("account_form:password")).clear();
        driver.findElement(By.id("account_form:password")).sendKeys(password);
        this.sendById("account_form:password", password);
        this.clickById("account_form:ok");

    }

    public void getBaseURL() {
        driver.get(baseURL + "/app/flow");
    }

    public void sendById(String selector, String keys) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT_IN_SECONDS);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(selector)));
            driver.findElement(By.id(selector)).click();
            driver.findElement(By.id(selector)).clear();
            driver.findElement(By.id(selector)).sendKeys(keys);

        } catch (Exception e) {
            LOG.info("sendById NOK: " + e.getMessage());
            sleepAMoment();
            return;

        }
        LOG.info("sendById ok: " + selector);
    }

    public void sendByName(String selector, String keys) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT_IN_SECONDS);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(selector)));
            driver.findElement(By.name(selector)).click();
            driver.findElement(By.name(selector)).clear();
            driver.findElement(By.name(selector)).sendKeys(keys);

        } catch (Exception e) {
            LOG.info("sendByName NOK: " + e.getMessage());
            sleepAMoment();
            return;

        }
        LOG.info("sendById ok: " + selector);
    }


    public void clickById(String selector) {

        if (selector.startsWith("id=")) {
            selector = selector.replace("id=", "");
        }

        try {

            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT_IN_SECONDS);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(selector)));
            driver.findElement(By.id(selector)).click();


        } catch (Exception e) {
            LOG.info("clickById NOK: " + e.getMessage());
            sleepAMoment();
            return;
        }
        LOG.info("clickById ok: " + selector);
    }


    public void clickByLink(String linkText) {
        LOG.info("click link now: " + linkText);
        if (linkText.startsWith("link=")) {
            linkText = linkText.replace("link=", "");
        }

        try {
            driver.findElement(By.linkText(linkText)).click();
        } catch (Exception e) {
            LOG.info("clickByLink NOK: " + e.getMessage());
            sleepAMoment();
            return;
        }
        LOG.info("clickByLink ok: " + linkText);
    }


    public void clickByXpath(String selector, boolean retry) {

        try {

            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT_IN_SECONDS);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(selector)));

            driver.findElement(By.xpath(selector)).click();

        } catch (Exception e) {
            LOG.info("clickByXpath NOK: " + e.getMessage().replace("\n", " "));
            if (!retry) {
                return;
            }
            sleepAMoment();
        }


        LOG.info("clickByXpath ok: " + selector);
    }


    public void clickByXpathAndForget(String selector) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT_IN_SECONDS);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(selector)));
            driver.findElement(By.xpath(selector)).click();
        } catch (Exception e) {
            LOG.info("clickByXpathAndForget NOK: " + e.getMessage());
        }
        LOG.info("clickByXpath OK: " + selector);
    }


    public String getSourceAsString() {
        return driver.getPageSource();
    }

    public HtmlPage getSourceAsHtmlPage() {

        HtmlPage page = null;
        try {

            URL url = new URL("http://www.aa.bb");

            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
            webClient.getOptions().setJavaScriptEnabled(false);


            StringWebResponse response = new StringWebResponse(driver.getPageSource(), url);
            page = HTMLParser.parseHtml(response, webClient.getCurrentWindow());


        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return page;
    }

    public void takeScreesnshot() {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("/temp/screenshot/" + new Date() + ".png"));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void sleepAMoment(int... timeinseconds) {
        try {
            if (timeinseconds.length == 0) {
                Thread.sleep(1000);
            } else {
                Thread.sleep(timeinseconds[0] * 1000);
            }
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void distroy() {
        this.driver.close();
    }

}
