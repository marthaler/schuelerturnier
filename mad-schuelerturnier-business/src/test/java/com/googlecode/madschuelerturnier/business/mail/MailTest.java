/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.mail;

import org.junit.Ignore;
import org.junit.Test;

/**
 * MailSender Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public class MailTest {


    @Test
    @Ignore
    public void testSpielZeileKorrigieren() {

        MailSender sender = new MailSender();
        sender.sendMail("marthaler.worb@gmail.com", "noreply@schuelerturnier-scworb.ch", "Betreff: äöü", "Text: äöü");
    }

}
