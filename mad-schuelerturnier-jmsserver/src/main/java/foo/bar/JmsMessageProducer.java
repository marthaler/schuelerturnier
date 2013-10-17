package foo.bar;

import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;

@Component
public class JmsMessageProducer {

    private static final Logger LOG = Logger.getLogger(JmsMessageProducer.class);

    protected static final String MESSAGE_COUNT = "messageCount";

    @Autowired
    private JmsTemplate template = null;
    private int messageCount = 9999;
     private static int temp = 0;
    /**
     * Generates JMS messages
     */
    @PostConstruct
    public void generateMessages() throws JMSException {
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            temp = i;
            final String text = "Message number is " + i + ".";
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            template.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {

                    SpielEinstellungen einst = new SpielEinstellungen();
                    einst.setTest("ich bin test: " + temp);

                    ObjectMessage message = session.createObjectMessage();
                    message.setObject(einst);
                    message.setIntProperty(MESSAGE_COUNT, index);
                    System.out.println("Sending message: " + temp);
                    LOG.info("Sending message: " + text);
                    
                    return message;
                }
            });
        }
    }

}