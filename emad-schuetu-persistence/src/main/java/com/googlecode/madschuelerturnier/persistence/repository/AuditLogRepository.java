/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for {@link com.googlecode.madschuelerturnier.model.Gruppe} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("select o from AuditLog o  order by o.datum desc")
    public List<AuditLog> findAllAscending();

}