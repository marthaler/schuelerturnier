/**
 * Apache License 2.0
 */
package ch.emad.persistence.schuetu.repository;

import ch.emad.model.schuetu.model.Kategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for {@link ch.emad.model.schuetu.model.Mannschaft} instances. Provides basic CRUD operations due to the extension of
 * {@link JpaRepository}. Includes custom implemented functionality by extending {@link JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface KategorieRepository extends JpaRepository<Kategorie, Long> {

    @Query("select o from Kategorie o where o.gruppeA.geschlecht = 0")
    List<Kategorie> getKategorienMList();

    @Query("select o from Kategorie o where o.gruppeA.geschlecht = 1")
    List<Kategorie> getKategorienKList();

}