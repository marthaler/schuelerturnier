/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

package com.googlecode.madschuelerturnier.business.dropbox;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String [] args){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-dropbox-context.xml");
    }
}
