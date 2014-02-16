/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Controller zum Files aus der DB zu laden
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
@RequestMapping("/files/{typ}")
public class DBFileDownloadController {

    private static final Logger LOG = Logger.getLogger(DBFileDownloadController.class);

    @Autowired
    private FileRepository repo;

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public void getFile(
            @PathVariable("typ") String typ, @PathVariable("id") String pearID,
            HttpServletResponse response) {

        File file = getFile(typ, pearID);

        try {

            if(file == null){
                file = new File();
                file.setContentBase64(getNullFile());
            }

            // get your file as InputStream
            InputStream is = new ByteArrayInputStream(file.getContent());
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            LOG.error(ex.getMessage(),ex);
        }

    }

    public File getFile(String typ, String pearID) {
        File file=null;
        try {
        file = repo.findByTypAndPearID(typ, Integer.parseInt(pearID));
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return file;
    }

    public StreamedContent getFileAsStreamedContent(String typ, String pearID){
        File f = getFile( typ,  pearID);
        InputStream stream = new ByteArrayInputStream(f.getContent());
        return new DefaultStreamedContent(stream, f.getMimeType(), f.getDateiName());
    }

    public boolean hasFile(String typ, String pearID){
        File file = repo.findByTypAndPearID(typ,Integer.parseInt(pearID));
        if(file != null){
            return true;
        }
        return false;
    }

    private String getNullFile(){
        return ""+
        "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"+
        "ypTGAbC3ZEXUkjFXVeWZIiLpkIiIAIiIA//Z";
    }

}
