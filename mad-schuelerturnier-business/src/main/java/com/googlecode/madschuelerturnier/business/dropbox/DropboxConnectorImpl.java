/**
 * Copyright (C) eMad, 2014.
 */
package com.googlecode.madschuelerturnier.business.dropbox;

import com.googlecode.madschuelerturnier.stages.Stage;
import com.googlecode.madschuelerturnier.stages.StageEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
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
@Component("dropboxConnector")
public class DropboxConnectorImpl implements DropboxConnector {

    private static final Logger LOG = Logger.getLogger(DropboxConnectorImpl.class);

    @Autowired
    ch.emad.dropbox.DropboxConnector driver;

    @Autowired
    DropboxStarter starter;

    @Autowired(required = false)
    Stage stage;

    @PostConstruct
    public void init() {

        if(stage == null){
            stage = new Stage();
        }

        rootFolder = rootFolder + "/" + stage.getStage().getText();

        // versuche den remote dienst zu holen und ersetze den lokal zugewiesenen falls vorhanden
        LOG.info("DropboxConnectorImpl: versuche verbindung mit rmi://87.230.15.247:4199/DropboxConnectorRemote");
        try {
            RmiProxyFactoryBean connectorRemoteProxy = new RmiProxyFactoryBean();
            connectorRemoteProxy.setServiceUrl("rmi://87.230.15.247:4199/DropboxConnectorRemote");
            connectorRemoteProxy.setServiceInterface(ch.emad.dropbox.DropboxConnector.class);
            connectorRemoteProxy.afterPropertiesSet();

            ch.emad.dropbox.DropboxConnector connectorRemote = (ch.emad.dropbox.DropboxConnector) connectorRemoteProxy.getObject();
            connectorRemote.isConnected();
            LOG.info("DropboxWeiche: verbindung ok");
            driver = connectorRemote;
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        this.starter.doTheStuff(this);
    }

    public void setSelectedGame(String selectedGame) {
        this.selectedGame = selectedGame;
    }

    private String selectedGame = "";

    private String rootFolder = "/shared/z_schuelerturnier/informatik/applikationsdaten";


    @Override
    public Boolean isConnected() {
        return driver.isConnected();
    }

    @Override
    public String getLoginURL() {
        return driver.getLoginURL();
    }

    @Override
    public void insertToken(String token) {
        driver.insertToken(token);
    }

    @Override
    public List<String> getFilesInFolder() {
        return driver.getFilesInFolder(this.rootFolder);
    }

    public List<String> getFilesInAltFolder() {
        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return null;
        }
        List<String> result = new ArrayList<String>();
        for (String str : driver.getFilesInFolder(this.rootFolder + "/alt")) {
            if (str.contains("xls")) {
                result.add(str);
            }
        }
        return result;
    }

    public List<String> getFoldersInRootFolder() {
        return driver.getFilesInFolder(this.rootFolder);
    }

    @Override
    public byte[] loadFile(String file) {
        if(!file.contains(this.rootFolder)){
            file = rootFolder + "/" +file;
        }
        return driver.loadFile(file);
    }

    @Override
    public void saveFile(String file, byte[] content) {
        if(!file.contains(this.rootFolder)){
            file = rootFolder + "/" +file;
        }
        driver.saveFile(file, content);
    }

    @Override
    public byte[] selectGame(String folder) {
        this.selectedGame = folder;
        return this.loadFile(this.rootFolder + "/" + this.selectedGame + "/" + this.selectedGame + ".xls");
    }

    @Override
    public List<String> getAllGames() {
        List<String> ret = new ArrayList<>();
        for (String str : getFoldersInRootFolder()) {
            if (str.contains("schuetu")) {
                ret.add(str);
            }
        }
        return ret;
    }

    public String getSelectedGame() {
        return selectedGame;
    }

    @Override
    public byte[] loadGameAttachemt(String file,String suffix) {
        return this.loadFile(this.rootFolder + "/" +this.selectedGame + "/attachements/" + file +"." + suffix);
    }

    private String loadGameAttachemtMD5(String file,String suffix) {
        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return null;
        }
        byte[] f = this.loadFile(this.rootFolder + "/" +this.selectedGame + "/attachements/md5/" + file + ".md5.txt");
        if (f != null) {
            return new String(f);
        }
        return null;
    }

    @Override
    public void saveGameAttachemt(String file, String suffix, byte[] content) {
        if (!this.isConnected()) {
            LOG.info("dropbox nicht verbunden.");
            return;
        }
        if (this.generateMD5(content).equals(loadGameAttachemtMD5(file,suffix))) {
            LOG.debug("brauche attachement nicht zu sichern inhalt ist gleich");
        } else {
            deleteGameAttachemt(file, suffix);
            this.saveFile(this.rootFolder + "/" +this.selectedGame + "/attachements/" + file, content);
            this.saveFile(this.rootFolder + "/" +this.selectedGame + "/attachements/md5/" + file + ".md5.txt", generateMD5(content).getBytes());
        }
    }

    @Override
    public void deleteGameAttachemt(String file,String suffix) {

        if(!file.contains(this.rootFolder)){
            file = rootFolder + "/" +file;
        }
        file = file +"."+suffix;
        this.driver.deleteFile(file);
    }

    @Override
    public void saveGame(byte[] content) {
        this.saveFile(this.selectedGame + "/" + this.selectedGame + ".xls", content);
        this.saveFile("alt/" + this.selectedGame + ".xls", content);
    }

    @Override
    public void saveOldGame(String jahr, String content) {
        try {
            this.saveFile(this.rootFolder + "/" +"alt/" + jahr + "-websitedump.xml", content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, String> loadOldGames() {
        LOG.fatal("IMPLEMENT !!!");
        return null;
    }

    private String generateMD5(byte[] args) {
        return DigestUtils.md5DigestAsHex(args);
    }

}
