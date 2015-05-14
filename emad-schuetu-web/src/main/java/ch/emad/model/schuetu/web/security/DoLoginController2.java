/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.web.security;

import ch.emad.business.common.security.DBAuthProvider;
import ch.emad.model.common.model.DBAuthUser;
import ch.emad.persistence.common.DBAuthUserRepository;
import ch.emad.web.common.security.LinkLoginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * fuehrt ein normale login durch
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class DoLoginController2 {

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
        } catch(Exception e){
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
