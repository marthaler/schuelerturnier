/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.*;

/**
 * User fuer die Webapplikation
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Entity
@Table(uniqueConstraints=
@UniqueConstraint(columnNames = {"username"}))
public class DBAuthUser extends Persistent implements UserDetails {

    public static String getPictureType() {
        return PICTURE_TYPE;
    }

    private static final String PICTURE_TYPE = "portrait";

    private String username;
    // initial 1234
    private String password = "81dc9bdb52d04dc20036dbd8313ed055";
    private String authoritiesString;
    private String mail;

    private boolean autologin = false;

    private String linktoken = UUID.randomUUID().toString();

    @Transient
    private String pw;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        for (String str : getAuths()) {
            list.add(new SimpleGrantedAuthority("ROLE_" + str.toUpperCase()));
        }
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Deprecated
    public void setPassword(String password) {
        this.password = password;
    }

    public void changeUsersPassword(String password) {

        if (password == null || password.length() < 1) {
            return;
        }
        this.password = new Md5PasswordEncoder().encodePassword(password,null);
    }

    public void setAuthoritiesString(String authorities) {
        this.authoritiesString = authorities;
    }

    public String getAuthoritiesString() {
        return this.authoritiesString;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // getter und setter fuer xls export und import
    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    // die beiden folgenden methoden werden benötigt um im webgui die rollen zuweisen zu koennen
    public List<String> getAuths() {
        Set<String> list = new HashSet<String>();
        if (authoritiesString != null && authoritiesString.length() > 0) {
            for (String auth : authoritiesString.split(",")) {
                list.add(auth.toLowerCase().replace("role_", ""));
            }
        }
        list.add("user");
        return new ArrayList<String>(list);
    }

    public void setAuths(List<String> coll) {
        this.authoritiesString = "";
        for (String auth : coll) {
            authoritiesString = authoritiesString + "ROLE_" + auth.toUpperCase() + ",";
        }
        authoritiesString = authoritiesString.substring(0, authoritiesString.length() - 1);
    }

    public String getLinktoken() {
        return linktoken;
    }

    public boolean isAutologin() {
        return autologin;
    }

    public void setAutologin(boolean autologin) {
        this.autologin = autologin;
    }
}
