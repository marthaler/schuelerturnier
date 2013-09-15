/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for {@link com.googlecode.madschuelerturnier.model.Penalty} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

}