package com.googlecode.mad_schuelerturnier.web;

import org.primefaces.event.TabChangeEvent;
import org.springframework.stereotype.Component;

@Component
public class MyBean {

	public void onTabChange(final TabChangeEvent event) {
		System.out.println(event.getTab().getId());
	}
}