package com.googlecode.madschuelerturnier.web.integration;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class WebapplicationSeleniumTest {

    private static final Logger LOG = Logger.getLogger(WebapplicationSeleniumTest.class);
    private WebClient client;

    @Before
    public void init() {

        client = new WebClient();

    }

    @Test
    @Ignore
    public void simpleCheck() throws IOException {



        HtmlPage page = client.getPage("http://localhost:8080");
        Assert.assertTrue(page.getBody().asText().contains("Passwort:"));
    }

}