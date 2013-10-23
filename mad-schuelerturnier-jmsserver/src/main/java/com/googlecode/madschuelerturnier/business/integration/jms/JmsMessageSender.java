package com.googlecode.madschuelerturnier.business.integration.jms;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

@Component
public class JmsMessageSender {

    private static final Logger LOG = Logger.getLogger(JmsMessageSender.class);

    private int messageCount = 0;

    @Autowired
    private JmsTemplate template = null;

    public void sendMessage(final String id, final Serializable obj) {

            template.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    messageCount = messageCount +1;
                    ObjectMessage message = session.createObjectMessage();
                    message.setStringProperty("typ", obj.getClass().toString());
                    message.setStringProperty("id", id);
                    message.setObject(obj);
                    message.setIntProperty("count",messageCount);
                    LOG.debug("Sending message: " + obj.getClass().toString() + " " + obj);
                    return message;
                }
            });
        }
}