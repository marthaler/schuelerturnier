/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.integration;

import java.io.Serializable;

/**
 * Wrapper fuer das Startfile (xls mit einem db dump)
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Deprecated
public class StartFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] content = null;


    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
