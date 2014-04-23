/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.dropbox.service;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.dropbox.DropboxFileAsyncBean;
import com.googlecode.madschuelerturnier.model.support.File;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;

/**
 * Aspekt zum sichern der File Attachements in der Dropbox
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component("dropboxConnector")
public class DropboxWeiche implements DropboxConnector{

    private static final Logger LOG = Logger.getLogger(DropboxConnector.class);

    @Autowired
    @Qualifier("localDropboxConnector")
    private DropboxConnector connector;

    private DropboxConnector connectorRemote;

    @PostConstruct
    public void init(){
        LOG.info("DropboxWeiche versuche verbindung mit rmi://127.0.0.1:1199/DropboxConnectorRemote");
        try{
        RmiProxyFactoryBean connectorRemoteProxy = new RmiProxyFactoryBean();
        connectorRemoteProxy.setServiceUrl("rmi://127.0.0.1:1199/DropboxConnectorRemote");
        connectorRemoteProxy.setServiceInterface(com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector.class);
        connectorRemoteProxy.afterPropertiesSet();

        connectorRemote = (DropboxConnector) connectorRemoteProxy.getObject();
        connectorRemote.isConnected();
        LOG.info("DropboxWeiche verbindung ok");
        }
        catch (Exception e){
            LOG.error(e.getMessage(),e);
            connectorRemote = null;
        }
    }

    @Override
    public Boolean isConnected() {
       if(isLocal()){
          return this.connector.isConnected();
       }else {
           return this.connectorRemote.isConnected();
       }
    }

    @Override
    public String getLoginURL() {
        if(isLocal()){
            return this.connector.getLoginURL();
        }else {
            return this.connectorRemote.getLoginURL();
        }
    }

    @Override
    public void insertToken(String token) {
        if(isLocal()){
            this.connector.insertToken(token);
        }else {
            this.connectorRemote.insertToken(token);
        }
    }

    @Override
    public List<String> getFilesInFolder() {
        if(isLocal()){
            return this.connector.getFilesInFolder();
        }else {
            return this.connectorRemote.getFilesInFolder();
        }
    }

    @Override
    public List<String> getFilesInAltFolder() {
        if(isLocal()){
            return this.connector.getFilesInAltFolder();
        }else {
            return this.connectorRemote.getFilesInAltFolder();
        }
    }

    @Override
    public byte[] loadFile(String file) {
        if(isLocal()){
            return this.connector.loadFile(file);
        }else {
            return this.connectorRemote.loadFile(file);
        }
    }

    @Override
    public void saveFile(String file, byte[] content) {
        if(isLocal()){
            this.connector.saveFile(file, content);
        }else {
            this.connectorRemote.saveFile(file,content);
        }
    }

    @Override
    public byte[] selectGame(String folder) {
        if(isLocal()){
            return this.connector.selectGame(folder);
        }else {
            return this.connectorRemote.selectGame( folder);
        }
    }

    @Override
    public List<String> getAllGames() {
        if(isLocal()){
            return this.connector.getAllGames();
        }else {
            return this.connectorRemote.getAllGames();
        }
    }

    @Override
    public String getSelectedGame() {
        if(isLocal()){
            return this.connector.getSelectedGame();
        }else {
            return this.connectorRemote.getSelectedGame();
        }
    }

    @Override
    public byte[] loadGameAttachemt(String file) {
        if(isLocal()){
            return this.connector.loadGameAttachemt(file);
        }else {
            return this.connectorRemote.loadGameAttachemt( file);
        }
    }

    @Override
    public void saveGameAttachemt(String file, String suffix, byte[] content) {
        if(isLocal()){
            this.connector.saveGame(content);
        }else {
            this.connectorRemote.saveGame(content);
        }
    }

    @Override
    public void deleteGameAttachemt(String file) {
        if(isLocal()){
            this.connector.deleteGameAttachemt(file);
        }else {
            this.connectorRemote.deleteGameAttachemt(file);
        }
    }

    @Override
    public void saveGame(byte[] content) {
        if(isLocal()){
            this.connector.saveGame(content);
        }else {
            this.connectorRemote.saveGame(content);
        }
    }

    @Override
    public void saveOldGame(String jahr, String content) {
        if(isLocal()){
            this.connector.saveOldGame(jahr,content);
        }else {
            this.connectorRemote.saveOldGame(jahr,content);
        }
    }

    @Override
    public Map<String, String> loadOldGames() {
        if(isLocal()){
            return this.connector.loadOldGames();
        }else {
            return this.connectorRemote.loadOldGames();
        }
    }


    private boolean isLocal(){
        if(this.connectorRemote != null){
            return false;
        }
        return true;
    }
}