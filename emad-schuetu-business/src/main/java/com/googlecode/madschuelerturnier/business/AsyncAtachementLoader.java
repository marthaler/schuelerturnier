/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Auslagerung des Fileladevorgangs (mit Dropbox ueber Aspekt gekoppelt) in einen Thread
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.1
 */
@Component
public class AsyncAtachementLoader {

    @Autowired
    private FileRepository fileRepo;

    private static final Logger LOG = Logger.getLogger(BusinessImpl.class);

    @Async
    public void loadfiles(List<File> attachements){
        // Attachements laden und updaten
        LOG.info("attachements geladen: " + attachements.size());
        for (com.googlecode.madschuelerturnier.model.support.File f : attachements) {
            fileRepo.save(f);
            LOG.info("attachements gespeicher: " + f);
        }
    }
}
