/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.zeit;

import com.googlecode.madschuelerturnier.business.integration.jms.JmsMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * Dient als Bridge zum versenden von Zeitpulsen über JMS
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class UhrJmsBridge implements ApplicationListener<ZeitPuls> {

    public boolean on = true;

    @Autowired
    public JmsMessageSender sender;

    public UhrJmsBridge() {

    }

    public void onApplicationEvent(final ZeitPuls event) {
        if(on){
         sender.sendMessage(event);
        }
    }

    public void setOn(boolean on){
        this.on = on;
    }

}