
/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

package ch.emad.business;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class SpringContextBusinessPicimport {

    private static final Logger LOG = Logger.getLogger(SpringContextBusinessPicimport.class);

    @Bean()
    public RmiProxyFactoryBean initBean(){
        org.springframework.remoting.rmi.RmiProxyFactoryBean exp = new org.springframework.remoting.rmi.RmiProxyFactoryBean();
        exp.setServiceUrl("rmi://87.230.15.247:3352/WebcamBusiness");
        //exp.setServiceUrl("rmi://localhost:3350/WebcamBusiness");
        exp.setServiceInterface(WebcamBusiness.class);
        return exp;
    }
}