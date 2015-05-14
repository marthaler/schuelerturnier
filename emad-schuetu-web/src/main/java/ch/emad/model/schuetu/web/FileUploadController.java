/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.web;

import ch.emad.business.schuetu.Business;
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