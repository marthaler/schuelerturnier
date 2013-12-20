/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.backup;

import com.googlecode.madschuelerturnier.business.dropbox.DropboxConnector;
import com.googlecode.madschuelerturnier.business.xls.ToXLSDumper;
import com.googlecode.madschuelerturnier.model.Persistent;
import com.googlecode.madschuelerturnier.model.callback.ModelChaneListener;
import com.googlecode.madschuelerturnier.model.callback.ModelChangeListenerManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Speichert die ganze DB asl XLS in die Dropbox
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class DropboxDumper implements  ModelChaneListener {

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
    public void onChangeModel(Persistent p) {
        changed = true;
    }

    public void initialize() {
        this.init = true;
    }
}