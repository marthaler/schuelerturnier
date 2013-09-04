package com.googlecode.madschuelerturnier.web;

import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

@Component
public class SpeakerReloadAction implements Action {

    public Event execute(RequestContext context) {
        return new Event(this, "speaker_view");
    }
}