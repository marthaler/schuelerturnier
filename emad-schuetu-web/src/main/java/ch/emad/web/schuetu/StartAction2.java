/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu;

import ch.emad.business.schuetu.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
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
public class StartAction2 {

    @Autowired
    private Business business;

    public Event start(RememberMeAuthenticationToken rememberMeAuthenticationToken) {
        return getEvent(rememberMeAuthenticationToken);
    }

    public Event start(UsernamePasswordAuthenticationToken user) {   // NOSONAR
        return getEvent(user);
    }

    private Event getEvent(AbstractAuthenticationToken user) {
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
        } else if (contains(authorities, "ROLE_ROOT")) {
            return new Event(this, "spielsteuerung");
        }

        return new Event(this, "dashboard");
    }


    private boolean contains(Collection<GrantedAuthority> authorities, String au) {
        for (GrantedAuthority auth : authorities) {

            String authS = auth.getAuthority();

            if (authS.equals(au)) {
                return true;
            }
        }
        return false;
    }
}
