/**
 * Apache License 2.0
 */
package ch.emad.business;

import ch.emad.model.schuetu.model.Spiel;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.CaptureEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Controller mit Session Scope, welcher den Schirizettel Scan unterstuetzen
 *
 * @author marthaler.worb@gmail.com
 * @since 1.3.0
 */
@Component
@RequestMapping("/webcam")
@Scope("session")
public class WebcamController implements Serializable {

    @Autowired
    ApplicationContext ctx;

    private static final Logger LOG = Logger.getLogger(WebcamController.class);

    @Autowired(required = false)
    private WebcamBusiness webcamBusiness;

    @Autowired
    private SaveController saveController;

    private Map<String, Spiel> spiele;

    private Spiel spiel;
    private String code;

    private byte[] rawPicture;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) {
        try {
            InputStream is = new ByteArrayInputStream(rawPicture);
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @PostConstruct
    private void init() {
      spiele = this.webcamBusiness.loadSpieleCache();
    }

    public void oncapture(CaptureEvent captureEvent) {
        LOG.info("capture: event here");
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            LOG.fatal(e.getMessage(),e);
        }
        this.rawPicture = captureEvent.getData();
        LOG.info("capture: event ok");
    }

    public void reset() {
        this.rawPicture = null;
        this.spiel = null;
        this.code = null;
    }

    public void search() {
        this.spiel = findSpiel(code);
        if (spiel == null) {
            createError("Spiel mit dem Code: " + this.code + " nicht gefunden");
            this.code = null;
        }
    }

    public void save() {
        this.saveController.save(this.spiel, this.rawPicture);
        reset();
    }

    private void createError(String text) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, text, text);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Spiel getSpiel() {
        return spiel;
    }

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    public boolean hasCode() {
        if (code != null && code.length() > 0) {
            return true;
        }
        return false;
    }

    public boolean hasPic() {
        if (rawPicture == null) {
            return false;
        }
        return true;
    }

    public boolean hasSpiel() {
        if (spiel == null) {
            return false;
        }
        return true;
    }

    private Spiel findSpiel(String code) {
        Spiel spiel = this.spiele.get(code.toUpperCase());
        if (spiel == null) {
            return null;
        }
        if (spiel.getToreA() == -1) {
            spiel.setToreA(0);
        }
        if (spiel.getToreB() == -1) {
            spiel.setToreB(0);
        }
        return spiel;
    }

}
                        