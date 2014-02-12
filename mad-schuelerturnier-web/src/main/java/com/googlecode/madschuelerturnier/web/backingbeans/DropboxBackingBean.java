package com.googlecode.madschuelerturnier.web.backingbeans;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import java.util.List;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class DropboxBackingBean  {

    private String token;

    private boolean willNotConnect = false;

    @Autowired
    private DropboxConnector connector;

    public void save(){
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

    public boolean isConnected(){
        return connector.isConnected();
    }

    public List<String> getFileList(){
        if(!isConnected()){
           return null;
        }
        return connector.getAllGames();
    }

    public void setWillNotconnect(boolean willNotconnect) {
        this.willNotConnect = willNotconnect;
    }

    public boolean isWillNotconnect() {
        return willNotConnect;
    }
}