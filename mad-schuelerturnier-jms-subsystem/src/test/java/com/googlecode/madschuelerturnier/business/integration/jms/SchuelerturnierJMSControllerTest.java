/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * Spielt 2 JMS Controller nach und schickt je eine Nachricht. Es wird geprueft ob jeder Controller
 * seine eigenen Nachrichten auch wirklich nicht bekommen hat.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-jmsserver.test-context.xml"})
public class SchuelerturnierJMSControllerTest {

    private SchuelerturnierJMSController controller1;

    private SchuelerturnierJMSController controller2;

    @Test
    public void testGetSchulhausListe() {

        // "echten" controller hochfahren
        controller1 = new SchuelerturnierJMSController("tcp://localhost:1111", "");

        // "remote" controller hochfahren und mit dem "echten" verbinden
        controller2 = new SchuelerturnierJMSController("tcp://localhost:2222", "tcp://localhost:1111");

        // ueber controller1 wird erste nachricht geschickt
        controller1.sendMessage("Message 1");

        // warten, damit controller 2 die nachricht effektiv empfangen kann
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // letzte nachricht vom controller2 muss nun die vorhin gesendete nachricht sein
        ObjectMessage message = controller2.getLatest();

        try {
            Assert.assertEquals((String) message.getObject(), "Message 1");
        } catch (JMSException e) {
            e.printStackTrace();
        }

        // pruefen ob controller1 seine eigene nachricht nicht erhalten hat
        Assert.assertNull(controller1.getLatest());

        // controller2 sendet nachricht zurueck
        controller2.sendMessage("Message 2");

        // warten, damit controller1 die Nachricht erhalten hat
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // pruefen ob controllerq die nachricht wirklich erhalten hat
        ObjectMessage message2 = controller1.getLatest();

        try {
            Assert.assertEquals((String) message2.getObject(), "Message 2");
        } catch (JMSException e) {
            e.printStackTrace();
        }

        // pruefen ob controller2 seine eigene Nachricht nicht bekommen hat
        ObjectMessage message3 = controller2.getLatest();

        try {
            Assert.assertNotEquals((String) message3.getObject(), "Message 2");
        } catch (JMSException e) {
            e.printStackTrace();
        }
        this.controller1.shutdown();
        this.controller2.shutdown();
    }

}
