/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */
package ch.emad.web.schuetu.backingbeans.schiri;

import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class LogoutAction implements Action {

    public Event execute(RequestContext context) {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
      //  request.getSession().invalidate();
        request.getSession().setAttribute("schiriBackingBean",null);

        return new Event(this, "login");
    }
}