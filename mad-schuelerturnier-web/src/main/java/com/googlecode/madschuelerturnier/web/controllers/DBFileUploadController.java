/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.controllers;

import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.DBAuthUserRepository;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import org.apache.log4j.Logger;
import org.hsqldb.HsqlException;
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
@Scope("session")
public class DBFileUploadController implements Serializable{

    private static final Logger LOG = Logger.getLogger(DBFileUploadController.class);

    private String idToMatchWith;

    private String typeToSet;

    private String mimeType;

    @Autowired
    private FileRepository repo;


    public void handleFileUpload(FileUploadEvent event) {

        File file = repo.findByTypAndPearID(typeToSet,Integer.parseInt(idToMatchWith));

        if(file == null){
            file = new File();
        }

        file.setContent(event.getFile().getContents());
        file.setDateiName(event.getFile().getFileName());

        file.setPearID(Integer.parseInt(idToMatchWith));

        file.setTyp(typeToSet);

        file.setMimeType(mimeType);

        this.idToMatchWith = null;
        this.typeToSet = null;
try {
    this.repo.save(file);
} catch(Exception e){
    LOG.info("file musste aufgrund der org.hsqldb.HsqlException vor dem speichern zuerst geloescht werden");
    // murks wegen der: org.hsqldb.HsqlException: data exception: string data, right truncation
    this.repo.delete(file);
    this.repo.save(file);
}


    }

    public String getTypeToSet() {
        return typeToSet;
    }

    public void setTypeToSet(String typeToSet) {
        this.typeToSet = typeToSet;
    }
    public String getIdToMatchWith() {
        return idToMatchWith;
    }

    public void setIdToMatchWith(String idToMatchWith) {
        this.idToMatchWith = idToMatchWith;
    }

    public void prepare(){
        LOG.debug("noop beim upload");
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}