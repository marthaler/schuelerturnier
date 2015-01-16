/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.backingbeans.schiri;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.dropbox.DropboxStarter;
import com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrung;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.web.security.DoLoginController;
import com.googlecode.madschuelerturnier.web.security.LoginBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Session basiertes Backing Bean für die mobile Schiri-Sicht
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component
@Scope("session")
public class SchiriBackingBean implements Serializable{

    @Autowired
    SpielDurchfuehrung durchfuehrung;

    @Autowired
    private DoLoginController loginC;

    private boolean schirienabled = true;

    private String user;
    private String password;

    LoginBean login = new LoginBean();

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

        public void login(){

            login.setUser(this.user);
            login.setPassword(this.password);
            loginC.login(login);

    }

    public boolean isLoggedIn(){

        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {

        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if(user != null && user.getPrincipal() instanceof DBAuthUser){
            DBAuthUser us = (DBAuthUser) user.getPrincipal();
            us.getAuthoritiesString().contains("USER_");
            return true;
        }
        }
        return false;
    }


    public List<String> getNext(){
        List<String> ret = new ArrayList<String>();
        for(SpielZeile zeile : durchfuehrung.getList3Vorbereitet()){
            ret.add(zeile.getA().toString());
            ret.add(zeile.getB().toString());
            ret.add(zeile.getC().toString());

            //mad
        }
        return ret;
    }

    public boolean isSchirienabled() {
        return schirienabled;
    }

    public void setSchirienabled(boolean schirienabled) {
        this.schirienabled = schirienabled;
    }
}