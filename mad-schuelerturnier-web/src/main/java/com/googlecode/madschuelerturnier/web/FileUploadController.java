/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.Business;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class FileUploadController {

    private static final Logger LOG = Logger.getLogger(FileUploadController.class);

    @Autowired
    private Business business;


    public void handleFileUpload(FileUploadEvent event) {
        business.generateSpielFromXLS(event.getFile().getContents());
    }


}