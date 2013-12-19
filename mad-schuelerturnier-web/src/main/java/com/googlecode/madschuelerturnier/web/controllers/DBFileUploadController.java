/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Dient dem Hochladen von Files
 * Die ID des neuen Datensatzes in der DB kann im Anschluss abgeholt werden
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.8
 */
@Component
public class DBFileUploadController implements Serializable{

    private static final Logger LOG = Logger.getLogger(DBFileUploadController.class);

    private String lastId = "0";

    @Autowired
    private FileRepository repo;

    @Autowired
    private DBAuthUserRepository repoUser;


    public void handleFileUpload(FileUploadEvent event) {
        event.getFile().getContents();

        File f = new File();
        f.setContent(event.getFile().getContents());
        f.setDateiName(event.getFile().getFileName());

        f = repo.save(f);
        lastId = ""+f.getId();

    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String id) {
         lastId = id;
    }



    public void matchPicture(DBAuthUser user) {
        user.setPortraitId(lastId);
        repoUser.save(user);
    }


}