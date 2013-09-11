/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.vorbereitung.KorrekturenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Dient dazu bei den Spielzeilen die einzelnen Zeilen zu togglen um sie al Pause zu markieren oder eben nicht
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class TagToggleAction implements Action {

    @Autowired
    private KorrekturenHelper helper;

    public Event execute(RequestContext context) {

        ParameterMap request = context.getRequestParameters();

        helper.spielZeileKorrigieren(request.get("id"));

        return new Event(this, "weiter");

    }
}
