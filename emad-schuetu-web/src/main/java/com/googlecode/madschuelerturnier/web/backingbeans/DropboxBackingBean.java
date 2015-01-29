/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.backingbeans;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.dropbox.DropboxStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Backing Bean fuer den Dropbox Verbindungsaufbau
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component
@Scope("session")
public class DropboxBackingBean implements Serializable{

    private String token;

    // bei der erstellung eines neuen unterordners fuer die dropboxablage
    private String newfolder = new Date().toString();

    private boolean willNotConnect = false;

    @Autowired
    DropboxStarter starter;

    @Autowired
    @Qualifier("dropboxConnector")
    private DropboxConnector connector;

    public void save() {
        connector.insertToken(token);
        token = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getURL() {
        return this.connector.getLoginURL();
    }

    public boolean isConnected() {
        try {
            return connector.isConnected();
        } catch (Exception e){
            return false;
        }
    }

    public List<String> getFileList() {
        if (!isConnected()) {
            return null;
        }
        return connector.getAllGames();
    }

    public void setWillNotconnect(boolean willNotconnect) {
        this.willNotConnect = willNotconnect;
    }

    public boolean isWillNotconnect() {

        if(starter.noNeedToConnect()){
            return true;
        }

        return willNotConnect;
    }

    public String getNewfolder() {
        return newfolder;
    }

    public void setNewfolder(String newfolder) {
        this.newfolder = newfolder;
    }
}