/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.persistence.repository;

import com.googlecode.mad_schuelerturnier.model.Paarung;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Paarung} instances. Provides basic CRUD operations due to the extension of
 * {@link JpaRepository}. Includes custom implemented functionality by extending {@link JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface PaarungRepository extends JpaRepository<Paarung, Long> {


}