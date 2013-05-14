package com.googlecode.mad_schuelerturnier.web;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ReloadBackingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int i = 0;
	private boolean state = false;
	
	
	public boolean getState(){
		i++;
		 state = false;
		if(i%3 == 0){

			return false;
		}
		return true;
	}
}
