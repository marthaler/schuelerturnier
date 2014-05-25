/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.support.WebSessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for {@link com.googlecode.madschuelerturnier.model.support.WebSessionLog} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface WebSessionLogRepository extends JpaRepository<WebSessionLog, Long> {

    @Query("select o from WebSessionLog o where o.sessionid = ?1")
    WebSessionLog findWebSessionLogById(String id);

}