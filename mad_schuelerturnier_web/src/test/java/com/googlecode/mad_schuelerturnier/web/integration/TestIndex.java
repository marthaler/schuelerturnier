package com.googlecode.mad_schuelerturnier.web.integration;
 
import java.io.IOException;

import org.junit.Assert;
 
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
 
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
 
public class TestIndex {
	private WebClient client;
 
	@Before
	public void init() {
		client = new WebClient();
	}
 
	@Test
	public void simpleCheck() throws IOException {

        HtmlPage page = client.getPage("http://localhost:8080");
          System.out.println();
        Assert.assertTrue(page.getBody().asText().contains("Benutzername:"));
	}
 
}