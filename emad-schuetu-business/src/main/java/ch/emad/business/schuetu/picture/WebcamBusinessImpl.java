/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.picture;

import ch.emad.business.schuetu.Business;
import ch.emad.model.common.model.File;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.enums.GeschlechtEnum;
import ch.emad.persistence.common.FileRepository;
import ch.emad.persistence.schuetu.repository.SpielRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dient der Verarbeitung der hochgeladenenSchirzettel
 *
 * @author marthaler.worb@gmail.com
 * @since 1.3.0
 */
@RestController
public final class WebcamBusinessImpl implements WebcamBusiness {

    private static final Logger LOG = Logger.getLogger(WebcamBusinessImpl.class);

    @Autowired
    private Business business;

    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private FileRepository fileRepo;

    @Override
    public Spiel findeSpiel(String code) {
        if (business.getSpielEinstellungen().isWebcamdemomode() && code != null && code.length() > 0 && !business.getSpielEinstellungen().isWebcamdemomodescharf()) {
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

    @Override
    public void save(Spiel spiel, byte[] rawPicture) {

        Spiel orig = spielRepo.findSpielByIdString(spiel.getIdString());

        if (orig == null || orig.isFertigBestaetigt()) {
            LOG.info("achtung, es wurde versucht ein spiel zu speichern, das bereits bestaetigt war oder nicht existiert= geht nicht");
            return;
        }

        if (business.getSpielEinstellungen().isWebcamdemomode() && !business.getSpielEinstellungen().isWebcamdemomodescharf()) {
            LOG.warn("Speichern eines Spiels im Demomodus, schreibe nicht in die Datenbank!");
            return;
        }

        File file2 = fileRepo.findByTypAndPearID("schirizettel", spiel.getId());
        if (file2 == null) {
            file2 = new File();
        }


        file2.setContent(rawPicture);
        file2.setDateiName("schirizettel.png");
        file2.setMimeType("image/png");
        file2.setPearID(spiel.getId());
        file2.setTyp("schirizettel");

        try {
            this.fileRepo.save(file2);
        } catch (Exception e) {
            LOG.info("file musste aufgrund der org.hsqldb.HsqlException vor dem speichern zuerst geloescht werden");
            // murks wegen der: org.hsqldb.HsqlException: data exception: string data, right truncation
            this.fileRepo.delete(file2);
            this.fileRepo.save(file2);
        }


        orig.setFertigEingetragen(true);
        orig.setToreA(spiel.getToreA());
        orig.setToreB(spiel.getToreB());

        this.spielRepo.save(orig);

    }

    @Override
    public Spiel findSpielByDecodedPic(byte[] rawPicture) {
        InputStream in = new ByteArrayInputStream(rawPicture);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        String code = BarcodeUtil.decode(image);

        return findeSpiel(code);

    }

    @Override
    public Map<String, Spiel> loadSpieleCache() {
        List<Spiel> spieleList = this.spielRepo.findAll();
        Map<String, Spiel> result = new HashMap<String, Spiel>();
        for (Spiel s : spieleList) {
            result.put(s.getIdString(), s);
        }
        return result;
    }

}

