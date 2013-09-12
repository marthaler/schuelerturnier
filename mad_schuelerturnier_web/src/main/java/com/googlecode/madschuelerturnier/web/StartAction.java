/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.Event;

import java.util.Collection;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class StartAction {

    @Autowired
    private Business business;


    public Event start(UsernamePasswordAuthenticationToken user) {

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (!business.isDBInitialized()) {
            return new Event(this, "initialisieren");
        } else if (contains(authorities, "ROLE_OPERATOR")) {
            return new Event(this, "spiel_start");
        } else if (contains(authorities, "ROLE_SPEAKER")) {
            return new Event(this, "speaker");
        } else if (contains(authorities, "ROLE_KONTROLLIERER")) {
            return new Event(this, "kontrollierer");
        } else if (contains(authorities, "ROLE_EINTRAGER")) {
            return new Event(this, "eintrager");
        } else if (contains(authorities, "ROLE_BEOBACHTER")) {
            return new Event(this, "gt_matrix");
        }


        return new Event(this, "dashboard");

    }


    public boolean contains(Collection<GrantedAuthority> authorities, String au) {
        for (GrantedAuthority auth : authorities) {

            String authS = auth.getAuthority();

            if (authS.equals(au)) {
                return true;
            }


        }
        return false;
    }

}
