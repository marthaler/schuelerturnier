/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Trick fuer die Speicherung und den Zugriff auf das Spieleinstellungsobjekt, welches bloss als
 * Xstream Text abgespeichert ist
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.0
 */
public interface SpielEinstellungenRepository {

    public SpielEinstellungen getEinstellungen();

    public void save(SpielEinstellungen einstellung);

    public boolean isInitialized();

}