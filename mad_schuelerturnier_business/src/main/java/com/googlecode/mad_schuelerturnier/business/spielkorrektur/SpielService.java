package com.googlecode.mad_schuelerturnier.business.spielkorrektur;

import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dama
 * Date: 20.06.13
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public interface SpielService {

    public List<Spiel> readAllSpiele();

    public Spiel findSpiel(String id);

    public void doKorrektur(Spiel Spiel);
}
