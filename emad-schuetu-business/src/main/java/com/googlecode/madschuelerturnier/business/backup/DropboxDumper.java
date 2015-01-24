/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.backup;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.integration.IntegrationController;
import com.googlecode.madschuelerturnier.business.xls.ToXLSDumper;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListener;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListenerManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * Speichert die ganze DB als XLS in die Dropbox falls dieser Server die Master Instanz ist
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class DropboxDumper implements ModelChangeListener {

    private static final Logger LOG = Logger.getLogger(DropboxDumper.class);

    private boolean changed = false;

    private boolean init = false;

    @Autowired
    private ToXLSDumper dumper;

    @Autowired
    @Qualifier("dropboxConnector")
    private DropboxConnector dropbox;

    @Autowired
    private IntegrationController master;

    public DropboxDumper() {

    }

    @PostConstruct
    public void init() {
        ModelChangeListenerManager.getInstance().addListener(this);
        this.init = true;
    }

    @Scheduled(fixedRate = 60000)
    public void run() {

        if (changed && init && master.isMaster()) {
            if (dropbox.isConnected()) {
                dropbox.saveGame(dumper.mannschaftenFromDBtoXLS());
            }
            changed = false;
        }
    }

    @Override
    public void onChangeModel(Serializable object) {
        changed = true;
    }

    public void initialize() {
        this.init = true;
    }
}