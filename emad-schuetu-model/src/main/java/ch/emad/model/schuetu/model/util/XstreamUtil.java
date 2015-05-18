/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Wrapper fuer Xstream
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public final class XstreamUtil {

    private static XStream xStream = new XStream(new DomDriver());

    private static final Logger LOG = Logger.getLogger(XstreamUtil.class);

    private XstreamUtil() {

    }

    public static void saveObjectToFile(Object obj, String file) {
        try {
            FileUtils.writeStringToFile(new File(file), xStream.toXML(obj));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static Object loadObjectFromFile(String file) {
        return xStream.fromXML(new File(file));
    }

    public static Object deserializeFromString(String string) {
        try {
           string = string.replace("com.googlecode.madschuelerturnier.model.SpielEinstellungen","ch.emad.model.schuetu.model.SpielEinstellungen");

            return xStream.fromXML(string);
        } catch (Exception e) {
            LOG.error("fehler beim deserialisieren: " + string,e);
        }
        return null;
    }

    public static String serializeToString(Object object) {
        return xStream.toXML(object);
    }

}
