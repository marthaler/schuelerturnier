package com.googlecode.madschuelerturnier.business; /**
 * Apache License 2.0
 */
import com.googlecode.madschuelerturnier.business.integration.jms.SchuelerturnierTransportController;
import com.googlecode.madschuelerturnier.web.controllers.TransportController;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet fuer den Transport test auf dem Tomcat
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
public class TransportTestServlet extends HttpServlet {

    private TransportController controller;

    @Override
    public void init() throws ServletException {
        super.init();

        String ownConnectionString = System.getProperty("ownConnectionString");
        String remoteConnectionString = System.getProperty("remoteConnectionString");

        SchuelerturnierTransportController brain = new SchuelerturnierTransportController(ownConnectionString, remoteConnectionString);
        controller = new TransportController();
        controller.setController(brain);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     PrintWriter out = response.getWriter();
     String payload =  request.getParameter("payload");
     String respo = controller.handleRequest(payload);

    out.print(respo);

  }
}