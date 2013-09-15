/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.delegate;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.faces.bean.SessionScoped;


/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
@SessionScoped
@Deprecated
public class EintragDelegate {

    private String text = "todo remove";

    private static final Logger LOG = Logger.getLogger(EintragDelegate.class);

    public void eintragen() {
        LOG.warn("todo remove");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBlockField() {
        LOG.warn("todo remove");
        return false;
    }

}
