/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.backup;

import ch.emad.business.schuetu.Business;
import ch.emad.business.schuetu.dropbox.DropboxConnector;
import ch.emad.business.schuetu.xls.ToXLSDumper2;
import ch.emad.model.schuetu.model.callback.ModelChangeListener;
import ch.emad.model.schuetu.model.callback.ModelChangeListenerManager;
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
    private ToXLSDumper2 dumper;

    @Autowired
    private Business business;

    @Autowired
    @Qualifier("dropboxConnector2")
    private DropboxConnector dropbox;


    public DropboxDumper() {

    }

    @PostConstruct
    public void init() {
        ModelChangeListenerManager.getInstance().addListener(this);
        this.init = true;
    }

    @Scheduled(fixedRate = 60000)
    public void run() {

        if (changed && init && business.getSpielEinstellungen() != null && business.getSpielEinstellungen().isMaster()) {
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