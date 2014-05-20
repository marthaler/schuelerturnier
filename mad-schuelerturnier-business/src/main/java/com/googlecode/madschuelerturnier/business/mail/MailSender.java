/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.mail;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

/**
 * Verschickt Mails
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Controller
public class MailSender {

    private static final Logger LOG = Logger.getLogger(MailSender.class);

    public void sendMail(String to, String from, String subject, String text) {

        try {
            Email email = new SimpleEmail();

            email.setHostName("asmtp.mail.hostpoint.ch");
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator("robot@schuelerturnier-scworb.ch", "LoginPrepareAction"));
            email.setSSLOnConnect(true);
            email.setFrom(from);
            email.setSubject(subject);
            email.setMsg(text);
            email.setCharset("utf-8");
            email.addTo(to);
            email.send();
        } catch (EmailException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
