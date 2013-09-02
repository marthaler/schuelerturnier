/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for {@link Mannschaft} instances. Provides basic CRUD operations due to the extension of
 * {@link JpaRepository}. Includes custom implemented functionality by extending {@link JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface MannschaftRepository extends JpaRepository<Mannschaft, Long> {


    @Query("select o from Mannschaft o where o.begleitpersonTelefon = ?1")
    Mannschaft findBy(String telefon);

    @Query("select o from Mannschaft o where o.id = ?1")
    Mannschaft findByStringId(String telefon);

}