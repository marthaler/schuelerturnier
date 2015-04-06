/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.integration.state;

import java.io.Serializable;

/**
 * Signalisiert einen gesetzten Master
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class MasterStateEnum implements Serializable {

    private static final long serialVersionUID = 1;

    private String nameOfMaster;

    public String getNameOfMaster() {
        return nameOfMaster;
    }

    public void setNameOfMaster(String nameOfMaster) {
        this.nameOfMaster = nameOfMaster;
    }
}