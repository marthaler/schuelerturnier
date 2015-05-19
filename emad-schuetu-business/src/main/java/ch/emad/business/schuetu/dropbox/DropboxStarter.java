/**
 * Copyright (C) eMad, 2014.
 */
package ch.emad.business.schuetu.dropbox;

import ch.emad.business.common.stages.Stage;
import ch.emad.business.common.stages.StageEnum;
import ch.emad.business.schuetu.Business;
import ch.emad.business.schuetu.spieldurchfuehrung.SpielDurchfuehrung;
import ch.emad.model.schuetu.model.SpielEinstellungen;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;

/**
 * Stellt die Verbindung zum gesharten Dropbox Ordner her
 * liest und Speichert Files aus diesem
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Component
public class DropboxStarter {

    private static final Logger LOG = Logger.getLogger(DropboxStarter.class);

    @Autowired
    Business business;

    @Autowired(required = false)
    Stage stage;

    private boolean noNeedToConnect = false;

    private String selectedGame;

    @Autowired
    private SpielDurchfuehrung durchfuehrung;

    @Async
    public void doTheStuff(DropboxConnectorImpl driver) {

        byte[] arr = driver.loadFile("startup.properties");
        Properties res;

        if (arr != null && arr.length > 0) {
            res = new Properties();
            try {
                res.load(new StringReader(new String(arr)));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            if (res.getProperty("file") == null || res.getProperty("file").isEmpty()) {
                return;
            }
            noNeedToConnect = true;
            driver.setSelectedGame(res.getProperty("file"));
            this.selectedGame = res.getProperty("file");
            business.generateSpielFromXLS(driver.loadFile(res.getProperty("file") + "/" + res.getProperty("file") + ".xls"));


            if (stage != null && !stage.equals(StageEnum.PRODUCTION) && !res.getProperty("testmode").isEmpty()) {
                driver.setSelectedGame(res.getProperty("file"));
                String testdate = res.getProperty("testmode");
                noNeedToConnect = true;
                business.generateSpielFromXLS(driver.loadFile(res.getProperty("file") + "/" + res.getProperty("file") + ".xls"));


                SpielEinstellungen einst = business.getSpielEinstellungen();
                einst.setStarttag(new Date());
                einst.setStartJetzt(true);
                business.saveEinstellungen(einst);
                business.startClock();

            }
        }
    }

    public boolean noNeedToConnect() {
        return noNeedToConnect;
    }

    public String getSelectedGame() {
        return selectedGame;
    }

    public void setSelectedGame(String selectedGame) {
        this.selectedGame = selectedGame;
    }
}
