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
import java.util.List;

/**
 * Aspekt zum sichern der File Attachements in der Dropbox
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component("dropboxConnector")
public class DropboxWeiche implements DropboxConnector{

    @Autowired
    @Qualifier("localDropboxConnector")
    private DropboxConnector connector;

    private DropboxConnector connectorRemote;

    @PostConstruct
    public void init(){
        try{
        RmiProxyFactoryBean connectorRemoteProxy = new RmiProxyFactoryBean();
        connectorRemoteProxy.setServiceUrl("rmi://127.0.0.1:1199/DropboxConnectorRemote");
        connectorRemoteProxy.setServiceInterface(com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector.class);
        connectorRemoteProxy.afterPropertiesSet();

        connectorRemote = (DropboxConnector) connectorRemoteProxy.getObject();
        connectorRemote.isConnected();

        }
        catch (Exception e){
            connectorRemote = null;
        }
    }

    @Override
    public Boolean isConnected() {
       if(isLocal()){
          return this.isConnected();
       }else {
           return this.connectorRemote.isConnected();
       }
    }

    @Override
    public String getLoginURL() {
        if(isLocal()){
            return this.getLoginURL();
        }else {
            return this.connectorRemote.getLoginURL();
        }
    }

    @Override
    public void insertToken(String token) {
        if(isLocal()){
            this.insertToken(token);
        }else {
            this.connectorRemote.insertToken(token);
        }
    }

    @Override
    public List<String> getFilesInFolder() {
        if(isLocal()){
            return this.getFilesInFolder();
        }else {
            return this.connectorRemote.getFilesInFolder();
        }
    }

    @Override
    public List<String> getFilesInAltFolder() {
        if(isLocal()){
            return this.getFilesInAltFolder();
        }else {
            return this.connectorRemote.getFilesInAltFolder();
        }
    }

    @Override
    public byte[] loadFile(String file) {
        if(isLocal()){
            return this.loadFile(file);
        }else {
            return this.connectorRemote.loadFile(file);
        }
    }

    @Override
    public void saveFile(String file, byte[] content) {
        if(isLocal()){
            this.saveFile(file, content);
        }else {
            this.saveFile(file,content);
        }
    }

    @Override
    public byte[] selectGame(String folder) {
        if(isLocal()){
            return this.selectGame(folder);
        }else {
            return this.selectGame( folder);
        }
    }

    @Override
    public List<String> getAllGames() {
        if(isLocal()){
            return this.getFilesInAltFolder();
        }else {
            return this.connectorRemote.getFilesInAltFolder();
        }
    }

    @Override
    public String getSelectedGame() {
        if(isLocal()){
            return this.getSelectedGame();
        }else {
            return this.getSelectedGame();
        }
    }

    @Override
    public byte[] loadGameAttachemt(String file) {
        if(isLocal()){
            return this.loadGameAttachemt(file);
        }else {
            return this.connectorRemote.loadGameAttachemt( file);
        }
    }

    @Override
    public void saveGameAttachemt(String file, String suffix, byte[] content) {
        if(isLocal()){
            this.saveGame(content);
        }else {
            this.connectorRemote.saveGame(content);
        }
    }

    @Override
    public void deleteGameAttachemt(String file) {
        if(isLocal()){
            this.deleteGameAttachemt(file);
        }else {
            this.connectorRemote.deleteGameAttachemt(file);
        }
    }

    @Override
    public void saveGame(byte[] content) {
        if(isLocal()){
            this.saveGame(content);
        }else {
            this.connectorRemote.saveGame(content);
        }
    }


    private boolean isLocal(){
        if(this.connectorRemote != null){
            return false;
        }
        return true;
    }
}