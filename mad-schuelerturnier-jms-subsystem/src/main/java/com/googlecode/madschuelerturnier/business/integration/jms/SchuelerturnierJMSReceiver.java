/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.integration.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;

/**
 * Ist eine eingehende JMS Verbindung
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class SchuelerturnierJMSReceiver {

    private static final Logger LOG = Logger.getLogger(SchuelerturnierJMSReceiver.class);

    private String connString = "";


    private Session session;

    private Connection connection;

    private MessageConsumer consumer;

    private boolean online = false;

    private int messageCount = 0;


    public SchuelerturnierJMSReceiver(String connection) {
        connString = connection;
        createConnection();
    }


    public ObjectMessage receiveMessage() {
        try {
            Object obj = consumer.receiveNoWait();
            if (obj != null) {
                return (ObjectMessage) obj;
            }
        } catch (JMSException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
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

            // Create a MessageConsumer from the Session to the Topic or Queue
            consumer = session.createConsumer(destination);

            online = true;

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