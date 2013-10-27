/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.io.Serializable;

/**
 * Ist eine ausgehende JMS Verbindung
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class SchuelerturnierJMSSender {

    private static final Logger LOG = Logger.getLogger(SchuelerturnierJMSSender.class);

    private String connString = "";
    private String ownConnection = "";

    private Session session;

    private Connection connection;

    private MessageProducer producer;

    private boolean online = false;

    private int messageCount = 0;

    public SchuelerturnierJMSSender(String connection,String ownConnection) {
        this.ownConnection = ownConnection;
        connString = connection;
        createConnection();
    }

    public void sendMessage(String id, Serializable object) {
        sendMessage(id, object, object.getClass().toString());
    }

    private void sendMessage(String id, Serializable object, String typ) {
        try {
            ObjectMessage message = session.createObjectMessage();
            message.setObject(object);
            message.setStringProperty("id", id);
            message.setStringProperty("typ", typ);
            messageCount = messageCount + 1;
            message.setIntProperty("count", messageCount);
            producer.send(message);
        } catch (JMSException e) {
            LOG.error(e.getMessage(), e);
            online = false;
        }
    }

    private void createConnection() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(connString);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();
            this.connection = connection;

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            this.session = session;

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic("schuelerturnier");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            this.producer = producer;
            online = true;

            this.sendMessage("00", ownConnection, "schuelerturnier-anmeldung");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            online = false;
        }
    }

    public void teardown() {
        try {
            session.close();
            connection.close();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}