/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.web.controllers;

import ch.emad.business.schuetu.BarcodeUtil;
import ch.emad.business.schuetu.SaveController;
import ch.emad.business.schuetu.WebcamBusiness;
import ch.emad.model.schuetu.model.Spiel;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
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
@Scope("session")
@RequestMapping("/webcam")
public class WebcamController implements Serializable {

    private static final Logger LOG = Logger.getLogger(WebcamController.class);

    @Autowired
    WebcamBusiness webcamBusiness;

    @Autowired
    SaveController saveController;

    private Map<String, Spiel> spiele;

    private Spiel spiel;
    private byte[] rawPicture;
    private String code;

    Boolean gefunde = null;

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

    public void fileupload(FileUploadEvent event) {
        this.rawPicture = event.getFile().getContents();
        decodePicAndSearchSpiel();
    }

    public void oncapture(CaptureEvent captureEvent) {
        LOG.info("capture: event here");

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.rawPicture = captureEvent.getData();
        LOG.info("capture: event ok");
    }

    public void decodePicAndSearchSpiel() {

        this.spiel = this.findSpielByDecodedPic(this.rawPicture);

        if (this.spiel == null) {
            createError("Spiel mit dem Code: " + this.code + " nicht gefunden");
            code = null;
        } else if (!this.spiel.isFertiggespielt()) {
            createError("Spiel mit dem Code: " + this.code + " ist nicht fertig gespielt und kan somit noch nicht eingetragen werden");
            code = null;
        }
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

    public Spiel findSpielByDecodedPic(byte[] rawPicture) {
        InputStream in = new ByteArrayInputStream(rawPicture);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        String code = BarcodeUtil.decode(image);

        return findSpiel(code);

    }

    private Spiel findSpiel(String code) {
        Spiel s = this.spiele.get(code.toUpperCase());
        if (s == null) {
            return null;
        }
        if (s.getToreA() == -1) {
            s.setToreA(0);
        }
        if (s.getToreB() == -1) {
            s.setToreB(0);
        }
        return s;
    }

}
                        