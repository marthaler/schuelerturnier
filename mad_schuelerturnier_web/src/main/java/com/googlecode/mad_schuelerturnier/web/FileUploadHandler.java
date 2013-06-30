package com.googlecode.mad_schuelerturnier.web;

import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;

@Component
public class FileUploadHandler {

    public void handleFileUpload(FileUploadEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("feedback", new FacesMessage("Upload ok!", "->" + event.getFile().getFileName()));

        try {
            FileUtils.writeByteArrayToFile(new File("/" + event.getFile().getFileName()), event.getFile().getContents());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}