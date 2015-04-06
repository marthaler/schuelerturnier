/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.integration;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * Ankommende Message von anderen Instanzen, die Payload beinhaltet das Nachrichtenobjekt
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class IncommingMessage extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 1;

    private Serializable payload;

    public IncommingMessage(Object source) {
        super(source);
    }

    public Serializable getPayload() {
        return payload;
    }

    public void setPayload(Serializable payload) {
        this.payload = payload;
    }
}