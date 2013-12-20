/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.model.support.*;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import com.googlecode.madschuelerturnier.web.controllers.util.HalloBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller zum Files aus der DB zu laden
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Controller
public class DBFileDownloadController {

    @Autowired
    private FileRepository repo;

    @RequestMapping(value = "/files/{file_id}", method = RequestMethod.GET)
    public void getFile(
            @PathVariable("file_id") String fileId,
            HttpServletResponse response) {
        File file=null;
        try {
        file = repo.findOne(Long.parseLong(fileId));
        } catch (Exception e) {}

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

            throw new RuntimeException("IOError writing file to output stream");
        }

    }


    private String getNullFile(){
        return ""+
        "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"+
        "ypTGAbC3ZEXUkjFXVeWZIiLpkIiIAIiIA//Z";
    }

}
