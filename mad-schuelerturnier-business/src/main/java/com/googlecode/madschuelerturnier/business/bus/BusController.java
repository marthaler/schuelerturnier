/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.bus;

import com.googlecode.madschuelerturnier.business.integration.jms.JmsMessageListener;
import com.googlecode.madschuelerturnier.business.integration.jms.JmsMessageSender;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Zaehlt rueckwaerts bis zu einem bestimmen Zeitpunkt
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Controller
public class BusController implements ApplicationEventPublisherAware {

    @Autowired
    private JmsMessageSender sender;

    @Autowired
    private JmsMessageListener listener;

    private ApplicationEventPublisher publisher;

    private Map<String,Object> map = new HashMap<String,Object>();

    @PostConstruct
    private void init(){

    }

    public void sendMessage(Serializable obj){
        String id = UUID.randomUUID().toString();
        sender.sendMessage(id,obj);
        map.put(id,obj);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            this.publisher = applicationEventPublisher;
    }

}
