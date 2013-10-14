package com.googlecode.madschuelerturnier.web.utils;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class UriViewHandler extends ViewHandlerWrapper {

	private ViewHandler handler;

	public UriViewHandler(ViewHandler h) { // thanks to decorator pattern
        handler = h;
	}

	@Override
	public ViewHandler getWrapped() {
		return handler;
	}


    public java.lang.String deriveViewId(FacesContext context, java.lang.String input) {
        String result = "";
        if(input.contains("WEB-INF")){
            // ja
            result = ((HttpServletRequest)context.getExternalContext().getRequest()).getRequestURI();
        }  else{
            result = handler.getActionURL(context, input);
        }

        return result;
    }

    @Override
	public String getActionURL(FacesContext ctx, String viewId) {
       String result = "";
       if(viewId.contains("WEB-INF")){
             // ja
           result = ((HttpServletRequest)ctx.getExternalContext().getRequest()).getRequestURI();
       }  else{
           result = handler.getActionURL(ctx, viewId);
       }

		return result;
	}

}