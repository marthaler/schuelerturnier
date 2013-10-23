package com.googlecode.madschuelerturnier.business.integration.jms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JmsClient {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-jmsclient.context.xml");

        System.out.println("...");

        try {
            Thread.sleep(55555);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
