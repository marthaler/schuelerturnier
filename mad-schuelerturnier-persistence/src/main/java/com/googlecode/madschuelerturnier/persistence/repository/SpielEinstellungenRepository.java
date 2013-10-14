/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link SpielEinstellungen} instances. Provides basic CRUD operations due to the extension of
 * {@link JpaRepository}. Includes custom implemented functionality by extending {@link JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface SpielEinstellungenRepository extends JpaRepository<SpielEinstellungen, Long> {


}