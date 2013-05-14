package com.googlecode.mad_schuelerturnier.web.controller;

import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;

@Controller
public class FileUploadController {

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");


        try {
            FileUtils.writeByteArrayToFile(new File("/test.txt"), event.getFile().getContents());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
                    