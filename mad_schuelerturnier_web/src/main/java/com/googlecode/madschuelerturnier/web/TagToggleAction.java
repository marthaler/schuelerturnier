package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class TagToggleAction implements Action {

    @Autowired
    private SpielZeilenRepository spielzeilenRepo;

    public Event execute(RequestContext context) {

        ParameterMap request = context.getRequestParameters();
        Long id = request.getLong("id");

        SpielZeile zeile = spielzeilenRepo.findOne(id);
        zeile.setPause(!zeile.isPause());

        spielzeilenRepo.save(zeile);

        return new Event(this, "weiter");

    }
}
