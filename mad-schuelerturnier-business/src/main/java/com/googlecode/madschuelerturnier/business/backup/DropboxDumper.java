/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.backup;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.xls.ToXLSDumper;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListener;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListenerManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * Speichert die ganze DB asl XLS in die Dropbox
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
    private DropboxConnector dropbox;

    public DropboxDumper() {

    }

    @PostConstruct
    public void init() {
        ModelChangeListenerManager.getInstance().addListener(this);
    }

    @Scheduled(fixedRate = 60000)
    public void run() {
        if (changed && init) {
            if (dropbox.isConnected()) {
                dropbox.saveFile("schuetu-aktuell.xls", dumper.mannschaftenFromDBtoXLS());
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