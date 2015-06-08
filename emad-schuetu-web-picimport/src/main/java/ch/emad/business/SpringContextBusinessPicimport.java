
/*
 *
 */

package ch.emad.business;

import ch.emad.business.common.properties.PropProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class SpringContextBusinessPicimport {

    private static final Logger LOG = Logger.getLogger(SpringContextBusinessPicimport.class);

    @Autowired
    private PropProvider props;

    @Bean()
    public RmiProxyFactoryBean initBean(){

        String port = "335";
        String stage = props.readString("st");

        if (stage.equals("d")) {
            port = port + "0";
        }

        if (stage.equals("t")) {
            port = port + "1";
        }

        if (stage.equals("i")) {
            port = port + "2";
        }

        if (stage.equals("p")) {
            port = port + "3";
        }

        LOG.info("set the port for webcamsource: " + Integer.parseInt(port));

        org.springframework.remoting.rmi.RmiProxyFactoryBean exp = new org.springframework.remoting.rmi.RmiProxyFactoryBean();
        exp.setServiceUrl("rmi://87.230.15.247:"+port+"/WebcamBusiness");
        exp.setServiceInterface(WebcamBusiness.class);
        return exp;
    }
}