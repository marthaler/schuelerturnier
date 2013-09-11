/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.Korrektur;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link com.googlecode.madschuelerturnier.model.Korrektur} instances.
 * Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
public interface KorrekturRepository extends JpaRepository<Korrektur, Long> {

}