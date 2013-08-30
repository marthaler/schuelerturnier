/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business.spielkorrektur;

import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;

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
