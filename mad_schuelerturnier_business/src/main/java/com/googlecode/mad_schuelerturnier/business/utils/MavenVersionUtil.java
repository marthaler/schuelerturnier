/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.utils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.util.Properties;

@Controller
public class MavenVersionUtil {

    private static transient final Logger LOG = Logger.getLogger(SpielInformationExpert.class);

    public String version = "-";
    public String time = "-";

    public MavenVersionUtil() {
        init();
    }

    public void init() {

        try {
            final Properties p = new Properties();
            final InputStream is = getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
            if (is != null) {
                p.load(is);

                MavenVersionUtil.LOG.info("maven version util keys: " + p.stringPropertyNames());

                this.time = p.getProperty("Buildtime", "--");
                this.version = p.getProperty("Implementation-Version", "--");
            }
            is.close();
        } catch (final Exception e) {
            MavenVersionUtil.LOG.error("fehler mit maven version: " + e.getMessage());
        }
    }

    public String getTime() {
        return this.time;
    }

    public String getVersion() {
        return this.version;
    }

}
