/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.mail;

import com.googlecode.madschuelerturnier.business.vorbereitung.helper.KorrekturenHelper;
import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.KorrekturPersistence;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

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
        sender.sendMail("marthaler.worb@gmail.com","noreply@schuelerturnier-scworb.ch","Betreff","Text");
    }

}
