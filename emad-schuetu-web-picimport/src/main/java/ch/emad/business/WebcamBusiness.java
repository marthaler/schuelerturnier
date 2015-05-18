package ch.emad.business;

import ch.emad.model.schuetu.model.Spiel;

import java.util.Map;

/**
 * Created by dama on 09.05.14.
 */
public interface WebcamBusiness {

    Spiel findeSpiel(String code);

    void save(Spiel spiel, byte[] rawPicture);

    Spiel findSpielByDecodedPic(byte[] rawPicture);

    Map<String,Spiel> loadSpieleCache();

}
