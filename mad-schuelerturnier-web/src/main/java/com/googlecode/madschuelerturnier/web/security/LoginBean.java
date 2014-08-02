/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.security;

import java.io.Serializable;

/**
 * Dient als Behaelter fuer die login Informationen von der Loginmaske
 *
 * @author marthaler.worb@gmail.com
 * @since 1.3.1
 */
public class LoginBean implements Serializable {

    private String user;
    private String password;
    private String mail;
    private boolean rememberme;

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

    public boolean isRememberme() {
        return rememberme;
    }

    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
    }
}
