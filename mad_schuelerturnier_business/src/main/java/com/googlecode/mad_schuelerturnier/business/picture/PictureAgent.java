/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.picture;

import com.googlecode.mad_schuelerturnier.business.scanner.BarcodeDecoder;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import com.googlecode.mad_schuelerturnier.persistence.repository.SpielRepository;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class PictureAgent {

    private static final Logger LOG = Logger.getLogger(PictureAgent.class);

    private static final int DELAY = 1000;

    public String picToCheck;

    private String selected;

    private boolean init = false;

    private String tempPath = "";

    private String schirizettel = "";

    @Autowired
    private SpielRepository spielRepository;

    public void init(String path) {
        this.tempPath = path + "schirizetteltemp" + System.getProperty("file.separator");
        this.schirizettel = path + "schirizettel" + System.getProperty("file.separator");

        new File(tempPath).mkdirs();
        new File(schirizettel).mkdirs();

        LOG.info("tempPath erstellt: " + tempPath);
        LOG.info("schirizettel erstellt: " + schirizettel);

        this.init = true;

    }

    public boolean existiert(String id) {
        return new File(schirizettel + id + ".png").exists();
    }

    public void delPicToCheck() {
        if (this.hasPicToCheck()) {
            FileUtils.deleteQuietly(new File(getPicToCheck()));
        }
    }

    public void zuweisen() {
        try {

            if (this.selected != null) {
                this.selected = this.selected.toLowerCase();
            } else {
                LOG.error("keine auswahl bei der schirizettelzuweisung");
            }

            File source = new File(tempPath + picToCheck);
            File dest = new File(schirizettel + this.selected + ".png");
            FileUtils.copyFile(source, dest);
            FileUtils.deleteQuietly(source);

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        this.picToCheck = null;
    }

    public boolean hasPicToCheck() {
        if (getPicToCheck() == null) {
            return false;
        }
        return true;
    }

    public String getPicToCheck() {
        String ret = "";
        if (picToCheck == null) {
            Collection<File> files = FileUtils.listFiles(new File(tempPath), null, false);
            for (File f : files) {
                if (f.getName().contains("Bildschirmfoto")) {
                    picToCheck = f.getName();
                }
            }
        }

        if (picToCheck == null) {
            return null;
        }

        ret = "resources/static/schirizetteltemp/" + picToCheck;
        return ret;
    }

    @Scheduled(fixedDelay = DELAY)
    private void check() { //NOSONAR

        if (!init) {
            return;
        }

        Collection<File> files = FileUtils.listFiles(new File("/Users/dama/Desktop"), null, false);
        for (File f : files) {
            if (f.getName().contains("Bildschirmfoto")) {
                try {

                    String neu = this.tempPath + f.getName().replace(" ", "");
                    File fneu = new File(neu);

                    FileUtils.copyFile(f, fneu);

                    String res = BarcodeDecoder.decode(neu);
                    if (res.length() == 2) {
                        Spiel sp = this.spielRepository.findSpielByIdString(res);
                        try {
                            FileUtils.moveFile(fneu, new File(schirizettel + System.getProperty("file.separator") + sp.getId() + ".png"));
                        } catch (IOException e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                    FileUtils.deleteQuietly(f);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
