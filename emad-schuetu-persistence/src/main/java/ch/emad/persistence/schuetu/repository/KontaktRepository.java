/**
 * Apache License 2.0
 */
package ch.emad.persistence.schuetu.repository;

import ch.emad.model.schuetu.model.Kontakt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for {@link ch.emad.model.schuetu.model.Mannschaft} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.1.13
 */
public interface KontaktRepository extends JpaRepository<Kontakt, Long> {

    @Query("SELECT DISTINCT o.liste FROM Kontakt o")
    public List<String> slectAllLists();

    @Query("SELECT DISTINCT o.ressor FROM Kontakt o")
    public List<String> slectAllRessorts();

    @Query("SELECT o FROM Kontakt o where o.ressor LIKE ?1 and o.liste LIKE ?2")
    public List<Kontakt> findFiltredKontakteRessor(String ressor, String list);

}