/**
 * Copyright (C) eMad, 2014.
 */
package ch.emad.business.schuetu.dropbox;

import ch.emad.model.common.model.File;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final Logger LOG = Logger.getLogger(DropboxFileAsyncBean.class);

    @Autowired
    @Qualifier("dropboxConnector2")
    private DropboxConnector dropbox;

    @Async
    protected void saveToDropbox(File f) {
        dropbox.saveGameAttachemt(getFileName(f), f.getSuffix(), f.getContent());
    }

    @Async
    protected void deleteFromDropbox(File f) {
        dropbox.deleteGameAttachemt(getFileName(f),f.getSuffix());
    }

    protected byte[] loadFromDropbox(File f) {
        return dropbox.loadGameAttachemt(getFileName(f),f.getSuffix());
    }

    private String getFileName(File f) {
        return f.getTyp() + "_" + f.getPearID();
    }

}