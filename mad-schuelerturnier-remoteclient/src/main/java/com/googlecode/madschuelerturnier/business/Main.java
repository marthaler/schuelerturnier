package com.googlecode.madschuelerturnier.business;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        Business client = context.getBean(Business.class);

        System.out.println(client.isDBInitialized());

        client.initializeDB();

        client.startClock();

        System.out.println(client.isDBInitialized());

        System.out.println(client.getSpielEinstellungen().getCreationdate());

        System.out.println(client.spielzeitVerspaetung());

    }

}
