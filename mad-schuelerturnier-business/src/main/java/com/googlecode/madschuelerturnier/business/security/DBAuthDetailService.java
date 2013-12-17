/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.security;

import com.googlecode.madschuelerturnier.model.AuditLog;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.persistence.repository.AuditLogRepository;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Findet die User aufgrund der Userid
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class DBAuthDetailService implements UserDetailsService {

    private static final Logger LOG = Logger.getLogger(DBAuthDetailService.class);

    private Md5PasswordEncoder md5Encoder = new Md5PasswordEncoder();

    Set<String> alleRollen = new HashSet<String>();

    @Autowired
    private DBAuthUserRepository repo;

    @Autowired
    AuditLogRepository repoAudit;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.equals("root") && repo.count() < 1) {
             return createRoot();
        }

        UserDetails user = repo.findByUsername(username);

        if (user == null) {
            user = repo.findByMail(username);
        }

        if (user == null) {
            LOG.info("user: " + username + " + not found");
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    private DBAuthUser createRoot() {

        DBAuthUser user = new DBAuthUser();
        user.setMail("initial@schuelerturnier.ch");
        user.setAuthoritiesString("ROLE_ROOT,ROLE_ADMIN,ROLE_OPERATOR,ROLE_BEOBACHTER,ROLE_EINTRAGER,ROLE_KONTROLLIERER,ROLE_SPEAKER,ROLE_USER");
        user.setUsername("root");
        // root
        user.setPassword("63a9f0ea7bb98050796b649e85481845");
        this.saveUser(user);
        return user;

    }

    public void changeUsersPassword(DBAuthUser user) {

        if (user.getPw() == null || user.getPw().length() < 1) {
            return;
        }
        user.setPassword(md5Encoder.encodePassword(user.getPw(), null));

        AuditLog l = new AuditLog();
        l.setText(user.getUsername() + " hat passwort gewechselt");
        repoAudit.save(l);

        repo.save(user);
    }

    public void saveUser(DBAuthUser user) {

        AuditLog l = new AuditLog();
        if (user.getId() == null) {
            l.setText("neuen user angelegt: " + user.getUsername());
        } else {
            l.setText(user.getUsername() + " gesichert");
        }

        repoAudit.save(l);
        repo.save(user);
    }


    public void saveUser(List<DBAuthUser> user, DBAuthUser nu, String id) {

        if (id == null || id.equals("null")) {
            saveUser(nu);
            return;
        }

        for (DBAuthUser dbAuthUser : user) {

            if (dbAuthUser.getId() == Integer.parseInt(id)) {
                saveUser(dbAuthUser);
            }
        }
    }


    public Collection<String> alleRollen() {
        if (alleRollen.size() == 0) {
            List<DBAuthUser> user = repo.findAll();
            for (DBAuthUser dbAuthUser : user) {
                for (GrantedAuthority auth : dbAuthUser.getAuthorities()) {
                    alleRollen.add(auth.toString().toLowerCase().replace("role_", ""));
                }

            }

            alleRollen.add("root");
            alleRollen.add("admin");
            alleRollen.add("operator");
            alleRollen.add("beobachter");
            alleRollen.add("eintrager");
            alleRollen.add("kontrollierer");
            alleRollen.add("speaker");

        }
        return new ArrayList<String>(alleRollen);
    }

}