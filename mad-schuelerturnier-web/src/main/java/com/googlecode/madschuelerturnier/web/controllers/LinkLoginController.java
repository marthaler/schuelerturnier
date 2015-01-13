/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

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
public class LinkLoginController {

    @Autowired
    private DBAuthProvider provider;

    @Autowired
    private DBAuthUserRepository repo;

    @RequestMapping("/login/link/{token}")
    public String loginPrepare(@PathVariable("token") String token, HttpServletRequest request) {

        DBAuthUser user = repo.findByLinktoken(token);
        if (user == null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            try {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                authToken.setDetails(new WebAuthenticationDetails(request));
                Authentication authentication = this.provider.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        }

        return "redirect:/app/flow";
    }

}
