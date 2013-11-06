package com.googlecode.madschuelerturnier.business.integration.jms;

import com.googlecode.madschuelerturnier.model.messages.OutgoingMessage;

/**
 * Created by dama on 03.11.13.
 */
public class Main {


    public static void main(String[] arr){
        SchuelerturnierTransportController controller = new SchuelerturnierTransportController("http://localhost:5555/app/transport","http://localhost:8083/app/transport,http://localhost:8084/app/transport");


        OutgoingMessage m = new OutgoingMessage("hallo");
        m.setPayload("Ich bin ein String");

        controller.onApplicationEvent(m);

        try {
            Thread.sleep(4444444);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
