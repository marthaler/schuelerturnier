/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.spielkorrektur;

import com.googlecode.madschuelerturnier.model.spiel.Spiel;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface SpielService {

    List<Spiel> readAllSpiele();

    Spiel findSpiel(String id);

    void doKorrektur(Spiel spiel);
}
