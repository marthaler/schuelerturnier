/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.security;

import com.googlecode.madschuelerturnier.model.AuditLog;
import com.googlecode.madschuelerturnier.persistence.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DBAuthProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuditLogRepository repo;

    private Md5PasswordEncoder md5Encoder  = new Md5PasswordEncoder();

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        UserDetails loadedUser = this.userDetailsService.loadUserByUsername(username);

        try{
        if ( md5Encoder.isPasswordValid(loadedUser.getPassword(), authentication.getCredentials().toString(), null)) {

            AuditLog l = new AuditLog();
            l.setText(username +" hat sich angemeldet");
            repo.save(l);

            return loadedUser;
        }
        } catch(UsernameNotFoundException e){
            AuditLog l = new AuditLog();
                    l.setText("fehlgeschlagener anmeldungsversuch von: " + username + " -> " +e.getMessage());
            repo.save(l);
            throw e;
        }

        AuditLog l = new AuditLog();
        l.setText("fehlgeschlagener anmeldungsversuch von: " + username + " -> passwort ungueltig");
        repo.save(l);

        throw new BadCredentialsException("passwort ungueltig");
    }



    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }
}