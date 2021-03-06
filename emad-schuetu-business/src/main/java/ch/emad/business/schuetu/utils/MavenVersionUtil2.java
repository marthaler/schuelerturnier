/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.utils;

import ch.emad.business.common.stages.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Zur Anzeige des Build Datums und der Version im GUI, die Ausgabe des Datums muss explizit eingestellt werden (setShowVersion())
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Controller
public class MavenVersionUtil2 {

    private static final Logger LOG = Logger.getLogger(SpielInformationExpert.class);

    private String version = "-";
    private String time = "-";

    @Autowired(required = false)
    private Stage stage;

    private boolean showTime = false;

    public MavenVersionUtil2() {
        init();
    }

    public final void init() {
        InputStream is = null;
        try {
            final Properties p = new Properties();
            is = getClass().getResourceAsStream("/version.properties");
            if (is != null) {
                p.load(is);

                MavenVersionUtil2.LOG.info("maven version util keys: " + p.stringPropertyNames());

                this.time = p.getProperty("timestamp", "--");
                this.version = p.getProperty("version", "--");
            }

        } catch (final Exception e) {
            MavenVersionUtil2.LOG.error("fehler mit maven version: " + e.getMessage());
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
        if (this.showTime) {
            return this.time;
        }
        return "";
    }

    public String getVersion() {
        String stageS = "";
        if (this.stage != null) {
            stageS = " (" + stage.getStage().getText() + ")";
        }
        return this.version + stageS;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }
}
