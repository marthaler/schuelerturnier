/**
 * Copyright (C) eMad, 2014.
 */
package com.googlecode.madschuelerturnier.business.dropbox;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.stages.Stage;
import com.googlecode.madschuelerturnier.stages.StageEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Stellt die Verbindung zum gesharten Dropbox Ordner her
 * liest und Speichert Files aus diesem
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class DropboxStarter {

    private static final Logger LOG = Logger.getLogger(DropboxStarter.class);

    @Autowired
    Business business;

    @Autowired(required = false)
    Stage stage;

    private boolean noNeedToConnect = false;

    @Async
    public void doTheStuff(DropboxConnectorImpl driver){

        byte[] arr = driver.loadFile("startup.properties");
        Properties res;

        if(arr != null && arr.length > 0){
            res = new Properties();
            try {
                res.load(new StringReader(new String(arr)));
            } catch (IOException e) {
                LOG.error(e.getMessage(),e);
            }
            if(res.getProperty("file") == null || res.getProperty("file").isEmpty()){
                return;
            }
            noNeedToConnect = true;
            business.generateSpielFromXLS(driver.loadFile(res.getProperty("file")+"/"+res.getProperty("file")+".xls"));
            driver.setSelectedGame(res.getProperty("file"));

            if(stage != null && ! stage.equals(StageEnum.PRODUCTION) && !res.getProperty("testmode").isEmpty()){
                // todo
                String testdate = res.getProperty("testmode");



            }
        }
    }

    public boolean noNeedToConnect(){
        return noNeedToConnect;
    }

}
