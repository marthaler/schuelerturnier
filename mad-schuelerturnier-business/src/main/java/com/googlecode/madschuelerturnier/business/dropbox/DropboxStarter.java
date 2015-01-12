/**
 * Copyright (C) eMad, 2014.
 */
package com.googlecode.madschuelerturnier.business.dropbox;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.stages.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private boolean noNeedToConnect = false;

    @Async
    public void doTheStuff(DropboxConnectorImpl driver){

        byte[] arr = driver.loadFile("fixiertesjahr.txt");
        String res;

        if(arr != null && arr.length > 0){
            res = new String(arr);
            noNeedToConnect = true;
            business.generateSpielFromXLS(driver.loadFile(res+"/"+res+".xls"));
            driver.setSelectedGame(res);
        }
    }

    public boolean noNeedToConnect(){
        return noNeedToConnect;
    }

}
