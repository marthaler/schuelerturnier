package com.googlecode.mad_schuelerturnier.web;

import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.model.Kategorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Component
public class StartAction  {

    public Event start(UsernamePasswordAuthenticationToken user) {

        Collection<GrantedAuthority> authorities =  user.getAuthorities();



            if(contains(authorities, "ROLE_OPERATOR")){
                return new Event(this, "spiel_start");
            }

            if(contains(authorities, "ROLE_SPEAKER")){
              return new Event(this, "speaker");
            }

            if(contains(authorities, "ROLE_KONTROLLIERER")){
                return new Event(this, "kontrollierer");
            }

            if(contains(authorities, "ROLE_EINTRAGER")){
                return new Event(this, "eintrager");
            }

            if(contains(authorities, "ROLE_BEOBACHTER")){
                return new Event(this, "gt_matrix");
            }



        return new Event(this, "dashboard");

    }


    public boolean contains(Collection<GrantedAuthority> authorities, String au){
        for (GrantedAuthority auth : authorities){

            String authS = auth.getAuthority();

            if(authS.equals(au)){
                return true;
            }



        }
        return false;
    }

}
