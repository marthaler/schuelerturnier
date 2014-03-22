package com.googlecode.madschuelerturnier.web.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.madschuelerturnier.business.picture.BarcodeUtil;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@Scope("session")
@RequestMapping("/webcam")
public class WebcamController {

    @Autowired
    private SpielRepository repo;

    @Autowired
    private FileRepository fileRepo;

    private Spiel spiel;
    private  byte[] rawPicture;
    private String code;

    private boolean demomode = true;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void getFile( HttpServletResponse response) {
        try {
            InputStream is = new ByteArrayInputStream(rawPicture);
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {

        }
    }

    public boolean hasCode(){
        if(code != null && code.length() > 0){
            return true;
        }
        return false;
    }

    public boolean hasPic(){
        if(rawPicture == null){
            return false;
        }
        return true;
    }

    public boolean hasSpiel(){
        if(spiel == null){
            return false;
        }
        return true;
    }

    public void fileupload(FileUploadEvent event) {
       this.rawPicture = event.getFile().getContents();
        treatPic();
     }

    public void oncapture(CaptureEvent captureEvent) {
        this.rawPicture = captureEvent.getData();
        treatPic();
    }

    public void treatPic(){
        InputStream in = new ByteArrayInputStream(this.rawPicture);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        code = BarcodeUtil.decode(image);

        findeSpiel();


        if(spiel == null || !spiel.isFertiggespielt()){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Spiel eintragen nicht möglich",  "Spiel ist nicht fertig gespielt");
                FacesContext.getCurrentInstance().addMessage(null, message);
                code = null;
            }
    }

    private void findeSpiel() {
        if(this.demomode && code != null && code.length() > 0){
            spiel = getSpiel(code);
        } else{
            spiel = repo.findSpielByIdString(code);
        }
    }

    public void reset() {
        this.rawPicture = null;
        this.spiel = null;
    }

    public void search() {
        findeSpiel();
        if(spiel == null){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Spiel mit dem Code: " + this.code + " nicht gefunden",  "Spiel mit dem Code: " + this.code + " nicht gefunden");
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.code = null;
        }
    }

    public void save() {
        com.googlecode.madschuelerturnier.model.support.File file = new com.googlecode.madschuelerturnier.model.support.File();
        file.setContent(this.rawPicture);
        file.setDateiName("schirizettel.png");
        file.setMimeType("image/png");
        file.setPearID(spiel.getId());
        file.setTyp("schirizettel");
        fileRepo.save(file);
        this.rawPicture = null;
        this.repo.save(spiel);
        this.spiel = null;
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

    private Spiel getSpiel(String id){
        Spiel s = new Spiel();
        s.setIdString(id);

        Mannschaft a = new Mannschaft();
        a.setTeamNummer(1);
        a.setKlasse(3);
        a.setGeschlecht(GeschlechtEnum.M);

        s.setMannschaftA(a);

        Mannschaft b = new Mannschaft();
        b.setTeamNummer(2);
        b.setKlasse(3);
        b.setGeschlecht(GeschlechtEnum.M);

        s.setMannschaftB(b);

        return s;
    }
}
                        