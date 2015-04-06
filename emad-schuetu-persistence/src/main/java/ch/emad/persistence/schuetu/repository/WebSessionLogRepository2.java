/**
 * Apache License 2.0
 */
package ch.emad.persistence.schuetu.repository;

import ch.emad.model.schuetu.model.support.WebSessionLog2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for {@link ch.emad.model.schuetu.model.support.WebSessionLog2} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface WebSessionLogRepository2 extends JpaRepository<WebSessionLog2, Long> {

    @Query("select o from WebSessionLog2 o where o.sessionid = ?1")
    WebSessionLog2 findWebSessionLogById(String id);

}