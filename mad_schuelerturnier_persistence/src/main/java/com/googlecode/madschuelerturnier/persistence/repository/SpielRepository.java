/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.spiel.Spiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for {@link Spiel} instances. Provides basic CRUD operations due to the extension of
 * {@link JpaRepository}. Includes custom implemented functionality by extending {@link JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface SpielRepository extends JpaRepository<Spiel, Long> {

    @Query("select o from Spiel o where o.typ = 0")
    List<Spiel> findGruppenSpiel();

    @Query("select o from Spiel o where o.typ = 1 or o.typ = 2")
    List<Spiel> findFinalSpiel();

    @Query("select o from Spiel o where o.typ = 0 order by o.start,o.platz asc")
    List<Spiel> findGruppenSpielAsc();

    @Query("select o from Spiel o where o.typ = 1 or o.typ = 2 order by o.start,o.platz asc ")
    List<Spiel> findFinalSpielAsc();

    @Query("select o from Spiel o where o.fertigGespielt = TRUE and o.fertigEingetragen = FALSE")
    List<Spiel> findAllEinzutragende();

    @Query("select o from Spiel o where o.fertigGespielt = TRUE and o.fertigEingetragen = TRUE and o.fertigBestaetigt = FALSE")
    List<Spiel> findAllZuBestaetigen();

    @Query("select o from Spiel o where o.idString = ?1")
    Spiel findSpielByIdString(String idString);


}