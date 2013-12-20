/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.business.mail.MailSender;
import com.googlecode.madschuelerturnier.business.security.DBAuthProvider;
import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import com.googlecode.madschuelerturnier.web.security.LoginBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * fuehrt ein login aufgrund eines tokens, das per email an den besitzer gegangen ist durch
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class LinkLoginSendlinkController {

    private static final Logger LOG = Logger.getLogger(DBFileUploadController.class);

    @Autowired
    private MailSender sender;

    @Autowired
    private DBAuthUserRepository repo;

    public void loginPrepare(LoginBean bean) {


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder

                .getRequestAttributes()).getRequest();

        String adresse = "";
        InetAddress ipA = null;
        try {
            ipA = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage(), e);
        }

        if(request.getServerPort() == 443){
        adresse = "https://" + ipA.getHostAddress();
        }else{
            adresse ="http://" + ipA.getHostAddress() + ":" + request.getServerPort();
        }

        DBAuthUser user = repo.findByMail(bean.getMail());

        if(user == null){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eingegebene Emailadresse wurde nicht gefunden",""));
            return;
        }

        String text = "Sie haben einen Anmeldelink für die Schülerturnier Applikation bestellt: \n"+
        adresse + "/app/login/link"+user.getLinktoken() + "\n\n"+

        "Falls sie ihr Passwort ändern möchten können sie das nach dem anklicken \n des Links tun indem sie "+
                "ihren Benutzernamen (oben rechts) anklicken";

        sender.sendMail(user.getMail(),"informatik@schuelerturnier-scworb.ch","Anmeldung per Link" , text);

    }

}
