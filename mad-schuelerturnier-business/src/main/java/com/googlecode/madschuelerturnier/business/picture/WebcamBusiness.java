package com.googlecode.madschuelerturnier.business.picture;

import com.googlecode.madschuelerturnier.model.Spiel;

/**
 * Created by dama on 09.05.14.
 */
public interface WebcamBusiness {
    Spiel findeSpiel(String code);

    void save(Spiel spiel, byte[] rawPicture);

    Spiel findSpielByDecodedPic(byte[] rawPicture);
}
