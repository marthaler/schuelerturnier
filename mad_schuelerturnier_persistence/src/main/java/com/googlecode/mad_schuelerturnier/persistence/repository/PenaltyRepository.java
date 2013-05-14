/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.persistence.repository;

import com.googlecode.mad_schuelerturnier.model.spiel.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link com.googlecode.mad_schuelerturnier.model.spiel.Penalty} instances. Provides basic CRUD operations due to the extension of {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom
 * implemented functionality by extending {@link UserRepositoryCustom}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

}