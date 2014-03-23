/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.dropbox;

import com.googlecode.madschuelerturnier.model.support.File;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Zur asynchronen bedienung der Dropbox
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class DropboxFileAsyncBean {

    private static final Logger LOG = Logger.getLogger(DropboxConnectorImpl.class);

    @Autowired
    private DropboxConnector dropbox;

    @Async
    protected void saveToDropbox(File f) {
        dropbox.saveGameAttachemt(getFileName(f), f.getSuffix(), f.getContent());
    }

    @Async
    protected void deleteFromDropbox(File f) {
        dropbox.deleteGameAttachemt(getFileName(f));
    }

    protected byte[] loadFromDropbox(File f) {
        return dropbox.loadGameAttachemt(getFileName(f));
    }

    private String getFileName(File f) {
        return f.getTyp() + "_" + f.getPearID();
    }

}