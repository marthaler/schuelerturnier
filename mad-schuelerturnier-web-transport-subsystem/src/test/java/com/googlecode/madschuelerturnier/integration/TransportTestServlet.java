package com.googlecode.madschuelerturnier.integration; /**
 * Apache License 2.0
 */

import com.googlecode.madschuelerturnier.business.integration.IntegrationControllerImpl;
import com.googlecode.madschuelerturnier.web.controllers.TransportReceiver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet fuer den Transport test auf dem Tomcat
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
public class TransportTestServlet extends HttpServlet {

    private TransportReceiver controller;

    @Override
    public void init() throws ServletException {
        super.init();

        String ownConnectionString = System.getProperty("ownConnectionString");
        String remoteConnectionString = System.getProperty("remoteConnectionString");

        IntegrationControllerImpl brain = new IntegrationControllerImpl();
        controller = new TransportReceiver();
        controller.setController(brain);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String payload = request.getParameter("payload");
        String respo = controller.handleRequest(payload);

        out.print(respo);

    }
}