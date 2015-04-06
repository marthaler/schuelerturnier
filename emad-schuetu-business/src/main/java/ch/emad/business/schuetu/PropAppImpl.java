/*
 * Apache License 2.0
 */
package ch.emad.business.schuetu;

import ch.emad.business.common.properties.Prop;
import ch.emad.model.common.model.DBAuthUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Properties fuer die App
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.0.1
 */

@Controller
public class PropAppImpl implements Prop {

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public Map<String, String> getStrings() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appname", "schuetu");
        return map;
    }

    @Override
    public Map<String, Boolean> getBools() {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("changejahr", Boolean.FALSE);
        map.put("registrationon", Boolean.FALSE);
        return map;
    }

    @Override
    public Map<String, Integer> getInts() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        return map;
    }

    @Override
    public Map<String, Class> getClazz() {
        Map<String, Class> map = new HashMap<String, Class>();
        map.put("dbauthuser", DBAuthUser.class);
        return map;
    }
}
