/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.picture;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Dient der Verarbeitung der hochgeladenenSchirzettel
 *
 * @author marthaler.worb@gmail.com
 * @since 1.3.0
 */
@Component
public final class WebcamBusiness {

    private static final Logger LOG = Logger.getLogger(WebcamBusiness.class);

    @Autowired
    private Business business;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private FileRepository fileRepo;

    public Spiel findeSpiel(String code) {
        if (business.getSpielEinstellungen().isWebcamdemomode() && code != null && code.length() > 0) {
            return getSpiel(code);
        } else {
            return spielRepo.findSpielByIdString(code);
        }
    }

    private Spiel getSpiel(String id) {
        Spiel s = new Spiel();
        s.setIdString(id);

        Mannschaft a = new Mannschaft();
        a.setTeamNummer(1);
        a.setKlasse(3);
        a.setGeschlecht(GeschlechtEnum.M);

        s.setMannschaftA(a);

        Mannschaft b = new Mannschaft();
        b.setTeamNummer(2);
        b.setKlasse(3);
        b.setGeschlecht(GeschlechtEnum.M);

        s.setMannschaftB(b);

        return s;
    }

    public void save(Spiel spiel, byte[] rawPicture) {

        if (business.getSpielEinstellungen().isWebcamdemomode()) {
            LOG.warn("Speichern eines Spiels im Demomodus, schreibe nicht in die Datenbank!");
            return;
        }

        com.googlecode.madschuelerturnier.model.support.File file = new com.googlecode.madschuelerturnier.model.support.File();
        file.setContent(rawPicture);
        file.setDateiName("schirizettel.png");
        file.setMimeType("image/png");
        file.setPearID(spiel.getId());
        file.setTyp("schirizettel");
        fileRepo.save(file);

        this.spielRepo.save(spiel);

    }

    public Spiel findSpielByDecodedPic(byte[] rawPicture) {
        InputStream in = new ByteArrayInputStream(rawPicture);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String code = BarcodeUtil.decode(image);

        return findeSpiel(code);

    }

}

