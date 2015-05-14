/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.integration;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * Ausgehende Message zum verschicken an anderen Instanzen, der Payload beinhaltet das Nachrichtenobjekt
 * Trans bedeutet, dass die Nachrichten nicht in der Transportschicht zwischengespeichert werden und somit
 * nicht noch einmal weitergeschickt werden bei der Anmelung eines neuen Nodes
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class OutgoingMessage extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 1;

    private boolean trans = false;

    private Serializable payload;

    public OutgoingMessage(Object source) {
        super(source);
    }

    public Serializable getPayload() {
        return payload;
    }

    public void setPayload(Serializable payload) {
        this.payload = payload;
    }

    public boolean isTrans() {
        return trans;
    }

    public void setTrans(boolean trans) {
        this.trans = trans;
    }
}