package com.googlecode.madschuelerturnier.web.controller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;

@Controller
public class FileUploadController {

    private static final Logger LOG = Logger.getLogger(FileUploadController.class);

    public void handleFileUpload(FileUploadEvent event) {

        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");

        try {
            FileUtils.writeByteArrayToFile(new File("/test.txt"), event.getFile().getContents());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
                    