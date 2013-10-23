package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class JmsMessageListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(JmsMessageListener.class);

    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
        try {   
            //int messageCount = message.getIntProperty(JmsMessageListener.MESSAGE_COUNT);
            
            if (message instanceof ObjectMessage) {
                ObjectMessage tm = (ObjectMessage)message;
                 LOG.info("message count: " + tm.getIntProperty("count")+ " " + tm.getStringProperty("typ")+ " " + tm.getObject());
            }
        } catch (JMSException e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
}
      