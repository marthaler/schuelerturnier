/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2015.
 */

/**
 * Apache License 2.0
 */
package ch.emad.persistence.schuetu.repository;

import ch.emad.model.schuetu.model.Penalty;
import ch.emad.model.schuetu.model.Rechnung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Repository interface for {@link ch.emad.model.schuetu.model.Penalty} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface RechnungRepository extends JpaRepository<Rechnung, Long> {


}