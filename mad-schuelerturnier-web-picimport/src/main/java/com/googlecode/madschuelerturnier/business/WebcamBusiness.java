package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.model.Spiel;

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
