/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.security;

import com.googlecode.madschuelerturnier.business.security.DBAuthProvider;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import com.googlecode.madschuelerturnier.web.controllers.LinkLoginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * fuehrt ein normale login durch
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class DoLoginController {

    @Autowired
    private DBAuthProvider provider;

    @Autowired
    private DBAuthUserRepository repo;

    @Autowired
    LinkLoginController linklogin;

    public void login(LoginBean login) {
        try {

            if (login.getUser() == null || login.getUser().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Benutzername oder Email leer", ""));
            }

            if (login.getPassword() == null || login.getPassword().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwort leer", ""));
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.getUser(), login.getPassword());
            Authentication authentication = this.provider.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // setzen und speichern des des autologon flags wenn gewuenscht
            if(login.isRememberme()){
                DBAuthUser user = repo.findByUsername(login.getUser());
                user.setAutologin(true);
                repo.save(user);
                setTokenCookie(user.getLinktoken());
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anmeldefehler, bitte probieren sie es noch einmal", ""));
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    public void checkAutoLogon(){
        Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
        javax.servlet.http.Cookie msch = (Cookie) requestCookieMap.get("RememberMeToken");
        String rememberMeToken="";
        if (msch != null && msch.getValue() != null) {
            rememberMeToken = msch.getValue();
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        DBAuthUser user;
        try {
            user = repo.findByLinktoken(rememberMeToken);
        } catch(javax.persistence.NonUniqueResultException e){
            return;
        }

        if(user != null && user.isAutologin()){
            linklogin.loginPrepare(rememberMeToken,request);
        }
    }

    private void setTokenCookie(String token) {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie cookie = new Cookie("RememberMeToken", token);
        cookie.setMaxAge(60 * 60 * 24 * 30 * 12);
        cookie.setVersion(cookie.getVersion() + 1);
        response.addCookie(cookie);
    }

}
