package com.googlecode.mad_schuelerturnier.web;

import com.googlecode.mad_schuelerturnier.business.impl.Business;
import com.googlecode.mad_schuelerturnier.model.Kategorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import java.util.List;

@Component
public class KategorieZuordnungsAction implements Action {

    @Autowired
    Business business;

    public Event execute(RequestContext context) {

        List<Kategorie> kategorie = (List<Kategorie>) context.getFlowScope().get("kategorien");

        ParameterMap request = context.getRequestParameters();
        String mannschaftName = request.get("man_id");
        String ziel = request.get("ziel");

        business.manuelleZuordnungDurchziehen(mannschaftName, ziel);


        Event ev = null;
        if (kategorie.get(0).getName().startsWith("M")) {
            ev = new Event(this, "kategorien_m");
        } else {
            ev = new Event(this, "kategorien_k");
        }

        return ev;

    }
}
