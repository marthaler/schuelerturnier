/**
 * Copyright (C) eMad, 2014.
 */
package ch.emad.business.schuetu.dropbox;

import ch.emad.business.schuetu.Business;
import ch.emad.business.schuetu.xls.FromXLSLoader2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sobald die Verbindung zur Dropbox steht wird hiermit dafuer gesorgt, dass
 * die alten Matches geladen werden um die Autocomplete Sets auf dem Business
 * zu laden
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class DropboxOldDataLoader {

    private static final Logger LOG = Logger.getLogger(DropboxOldDataLoader.class);

    @Autowired
    FromXLSLoader2 loader;

    @Autowired
    Business business;

    @Async
    public void loadData(DropboxConnector connector) {
        List<String> files = connector.getFilesInAltFolder();
        for (String file : files) {
            business.updateAutocompletesMannschaften(loader.convertXLSToMannschaften(connector.loadFile(file)));
        }
    }

}
