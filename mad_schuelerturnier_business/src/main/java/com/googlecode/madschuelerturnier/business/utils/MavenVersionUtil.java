/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.utils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Zur Anzeige des Build Datums und der Version im GUI
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Controller
public class MavenVersionUtil {

    private static final Logger LOG = Logger.getLogger(SpielInformationExpert.class);

    private String version = "-";
    private String time = "-";

    public MavenVersionUtil() {
        init();
    }

    public final void init() {
        InputStream is = null;
        try {
            final Properties p = new Properties();
            is = getClass().getResourceAsStream("/version.properties");
            if (is != null) {
                p.load(is);

                MavenVersionUtil.LOG.info("maven version util keys: " + p.stringPropertyNames());

                this.time = p.getProperty("timestamp", "--");
                this.version = p.getProperty("version", "--");
            }

        } catch (final Exception e) {
            MavenVersionUtil.LOG.error("fehler mit maven version: " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    public String getTime() {
        return this.time;
    }

    public String getVersion() {
        return this.version;
    }

}
