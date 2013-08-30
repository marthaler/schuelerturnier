/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.business;

import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;

import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface ImportExportBusiness {


    List<Spiel> loadAllSpiele();

    void updateSpiele(String spiele, boolean bestaetigt, boolean eingetraegen);

}
