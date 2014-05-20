package com.googlecode.madschuelerturnier.business.dropbox;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.xls.FromXLSLoader;
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
    FromXLSLoader loader;

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
