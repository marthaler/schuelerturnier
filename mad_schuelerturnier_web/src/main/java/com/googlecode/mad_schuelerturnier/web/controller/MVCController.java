package com.googlecode.mad_schuelerturnier.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MVCController {

	private static final Logger LOG = Logger.getLogger(MVCController.class);

	@RequestMapping("demomvc")
	public ModelAndView helloWorld() {
		MVCController.LOG.info("--> Request");
		final ModelAndView mav = new ModelAndView();
		mav.setViewName("demomvc");
		mav.getModel().put("message", "Hallo demomvc");
		MVCController.LOG.info("<-- Response: " + mav.getModel());
		return mav;
	}
}