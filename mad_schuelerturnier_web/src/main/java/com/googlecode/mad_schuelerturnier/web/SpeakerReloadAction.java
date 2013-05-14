package com.googlecode.mad_schuelerturnier.web;

import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

@Component
public class SpeakerReloadAction implements Action {

    public Event execute(RequestContext context) throws Exception {
        return new Event(this, "speaker_view");
    }
}