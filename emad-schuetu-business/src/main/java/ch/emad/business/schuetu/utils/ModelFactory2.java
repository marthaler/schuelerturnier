/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.utils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * dient der erstellung von model-bean per refelction
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class ModelFactory2 {

    private static final Logger LOG = Logger.getLogger(ModelFactory2.class);

    public Object getInstance(String className) {

        Object obj = null;
        try {
            Class cls = Class.forName(className);
            Constructor ct = cls.getConstructor();
            obj = ct.newInstance(null);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return obj;
    }

    public String ok() {
        return "ok";
    }
}
