/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Repository interface for {@link com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface SpielZeilenRepository extends PagingAndSortingRepository<SpielZeile, Long> {

    @Query(value = "select o from SpielZeile o where o.finale = 'true' order by o.start asc ")
    List<SpielZeile> findFinalSpielZeilen();

    @Query("select o from SpielZeile o where o.finale = 'false' order by o.start asc ")
    List<SpielZeile> findGruppenSpielZeilen();

    @Query("select o from SpielZeile o where o.sonntag = 'true'")
    List<SpielZeile> findSpieleSonntag();

    @Query("select o from SpielZeile o where o.sonntag = 'false'")
    List<SpielZeile> findSpieleSammstag();

    @Query("select o from SpielZeile o where o.phase = 0 order by o.start asc")
    List<SpielZeile> findNextZeile();

    @Query("select o from SpielZeile o where o.phase = 1 order by o.start asc")
    List<SpielZeile> findBZurVorbereitung();

    @Query("select o from SpielZeile o where o.phase = 2 order by o.start asc")
    List<SpielZeile> findCVorbereitet();

    @Query("select o from SpielZeile o where o.phase = 3 order by o.start asc")
    List<SpielZeile> findDSpielend();

}