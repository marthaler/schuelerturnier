/**
 * Apache License 2.0
 */
package ch.emad.schuetu.business.web.utils;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URL;
import java.util.TimeZone;

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
    private static TimeZone zone;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOG.info("ContextInformationListener:" + event);
        URL url = this.getClass().getResource("/log4j.xml");
        String tempPath = url.getPath();
        path = tempPath.replace("classes/log4j.xml", "");
        LOG.info("ContextInformationListener, path:" + path);


        LOG.info("ContextInformationListener, TimeZone: " + TimeZone.getDefault().getDisplayName());
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));
        LOG.info("ContextInformationListener, set TimeZone: Europe/Zurich");
        LOG.info("ContextInformationListener, TimeZone now: " + TimeZone.getDefault().getID());

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public static String getPath() {
        return path;
    }

}