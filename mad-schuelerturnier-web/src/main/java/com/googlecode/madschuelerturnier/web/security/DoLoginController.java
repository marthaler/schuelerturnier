/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.security;

import com.googlecode.madschuelerturnier.business.security.DBAuthProvider;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * fuehrt ein login aufgrund eines tokens, das per email an den besitzer gegangen ist durch
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class DoLoginController {

    @Autowired
    private DBAuthProvider provider;

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

        } catch (Exception e) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anmeldefehler, bitte probieren sie es noch einmal", ""));
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
