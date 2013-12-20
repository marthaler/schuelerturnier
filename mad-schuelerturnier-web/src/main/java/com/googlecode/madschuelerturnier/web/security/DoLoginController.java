/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.security;

import com.googlecode.madschuelerturnier.business.security.DBAuthProvider;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private DBAuthUserRepository repo;

    public void login(LoginBean login){
try{

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.getUser(), login.getPassword());

            Authentication authentication = this.provider.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);


        }

    }

}
