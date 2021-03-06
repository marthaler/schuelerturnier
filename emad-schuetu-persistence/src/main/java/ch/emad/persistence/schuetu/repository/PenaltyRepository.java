/**
 * Apache License 2.0
 */
package ch.emad.persistence.schuetu.repository;

import ch.emad.model.schuetu.model.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Repository interface for {@link ch.emad.model.schuetu.model.Penalty} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    @Query("select o from Penalty o where o.reihenfolgeOrig = ?1")
    Penalty findPenaltyByOriginalreihenfolge(String reihenfolge);

}