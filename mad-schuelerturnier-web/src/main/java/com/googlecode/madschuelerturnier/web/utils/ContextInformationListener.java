/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.utils;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URL;

/**
 * Sammelt Informationen zu Servlet Context um diese per statischen getter von ausserhalb abzufragen
 * Muss zuerst gestartet werden (vor Spring usw.)
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.7
 */
public class ContextInformationListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ContextInformationListener.class);
    private static String path = "not_initialized";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOG.info("ContextInformationListener:" + event);
        URL url = this.getClass().getResource("/log4j.xml");
        String tempPath = url.getPath();
        path = tempPath.replace("classes/log4j.xml", "");
        LOG.info("ContextInformationListener, path:" + path);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public static String getPath() {
        return path;
    }

}