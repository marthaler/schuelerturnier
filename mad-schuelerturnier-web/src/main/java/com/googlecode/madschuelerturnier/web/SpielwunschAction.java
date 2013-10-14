package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.model.Kategorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import java.util.List;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class SpielwunschAction implements Action {

    @Autowired
    private Business business;

    public Event execute(RequestContext context) {

        List<Kategorie> kategorie = (List<Kategorie>) context.getFlowScope().get("kategorien");

        ParameterMap request = context.getRequestParameters();
        String id = request.get("id");

        business.toggleSpielwunschOnKategorie(Long.parseLong(id));

        Event ev = null;
        if (kategorie.get(0).getName().startsWith("M")) {
            ev = new Event(this, "kategorien_m");
        } else {
            ev = new Event(this, "kategorien_k");
        }

        return ev;

    }
}
