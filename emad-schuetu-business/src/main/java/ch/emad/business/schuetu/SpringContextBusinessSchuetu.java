package ch.emad.business.schuetu;

import ch.emad.business.common.properties.PropProvider;
import ch.emad.business.schuetu.picture.WebcamBusiness;
import ch.emad.business.schuetu.picture.WebcamBusinessImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringContextBusinessSchuetu {

    private static final Logger LOG = Logger.getLogger(SpringContextBusinessSchuetu.class);

    @Autowired
    private WebcamBusinessImpl webcam;

    @Autowired
    private PropProvider props;

    @Bean
    public org.springframework.remoting.rmi.RmiServiceExporter initBean() {
        org.springframework.remoting.rmi.RmiServiceExporter exp = new org.springframework.remoting.rmi.RmiServiceExporter();
        exp.setServiceName("WebcamBusiness");
        exp.setService(webcam);
        exp.setServiceInterface(WebcamBusiness.class);

        String a = "335";
        String stage = props.readString("st");

        if (stage.equals("d")) {
            a = a + "0";
        }

        if (stage.equals("t")) {
            a = a + "1";
        }

        if (stage.equals("i")) {
            a = a + "2";
        }

        if (stage.equals("p")) {
            a = a + "3";
        }

        LOG.info("set the port for webcam: " + Integer.parseInt(a));
        exp.setRegistryPort(Integer.parseInt(a));
        return exp;
    }
}