package com.googlecode.mad_schuelerturnier.web.primefaces;

import org.primefaces.context.DefaultRequestContext;
import org.primefaces.util.Constants;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class Primefaces35PhaseListener implements PhaseListener {
    @Override
    public void afterPhase(PhaseEvent phaseEvent) {
        // NOP
    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
        FacesContext context = phaseEvent.getFacesContext();

        if (context.getAttributes().get(Constants.REQUEST_CONTEXT_ATTR) == null) {
            context.getAttributes().put(Constants.REQUEST_CONTEXT_ATTR, new DefaultRequestContext());
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}