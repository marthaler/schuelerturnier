package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.Business;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.webflow.execution.Event;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * StartAction Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class StartActionTest {

    @Mock
    private Business business;

    @Mock
    private UsernamePasswordAuthenticationToken user;

    @InjectMocks
    private StartAction obj = new StartAction();


    @Test
    public void testStart() {

        Event ret = obj.start(user);
        assertEquals("initialisieren", ret.getId());

        when(this.business.isDBInitialized()).thenReturn(true);

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        // dasboard
        ret = obj.start(user);
        assertEquals("flow", ret.getId());


        // beobachter
        GrantedAuthority auth = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_BEOBACHTER";
            }
        };
        authorities.add(auth);
        when(this.user.getAuthorities()).thenReturn(authorities);

        ret = obj.start(user);

        assertEquals("gt_matrix", ret.getId());


        // eintrager
        GrantedAuthority auth2 = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_EINTRAGER";
            }
        };
        authorities.add(auth2);

        ret = obj.start(user);

        assertEquals("eintrager", ret.getId());


        // kontrollierer
        GrantedAuthority auth3 = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_KONTROLLIERER";
            }
        };
        authorities.add(auth3);

        ret = obj.start(user);

        assertEquals("kontrollierer", ret.getId());


        // speaker
        GrantedAuthority auth4 = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_SPEAKER";
            }
        };
        authorities.add(auth4);

        ret = obj.start(user);

        assertEquals("speaker", ret.getId());


        // kontrollierer
        GrantedAuthority auth5 = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_OPERATOR";
            }
        };
        authorities.add(auth5);

        ret = obj.start(user);

        assertEquals("spiel_start", ret.getId());

    }

}



