package foo.bar;

import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class JmsMessageListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(JmsMessageListener.class);

    @Autowired
    private AtomicInteger counter = null;

    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
        try {   
            //int messageCount = message.getIntProperty(JmsMessageListener.MESSAGE_COUNT);
            
            if (message instanceof ObjectMessage) {
                ObjectMessage tm = (ObjectMessage)message;
                SpielEinstellungen msg = (SpielEinstellungen) tm.getObject();

                  System.out.println("bla...: " + msg.getTest());
               // LOG.info("Processed message '{}'.  value={}", msg, messageCount);
                
                counter.incrementAndGet();
            }
        } catch (JMSException e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
}
      