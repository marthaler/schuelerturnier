package com.googlecode.madschuelerturnier.web.security;

import java.io.Serializable;

/**
 * Created by dama on 20.12.13.
 */
public class LoginBean implements Serializable{

    private String user;
    private String password;
    private String mail;

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
