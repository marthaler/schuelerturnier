/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.DBAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for {@link com.googlecode.madschuelerturnier.model.Gruppe} instances. Provides basic CRUD operations due to the extension of
 * {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom implemented functionality by extending {@link org.springframework.data.jpa.repository.JpaRepository}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public interface DBAuthUserRepository extends JpaRepository<DBAuthUser, Long> {

    @Query("select o from DBAuthUser o where o.username = ?1")
    DBAuthUser findByUsername(String username);

    @Query("select o from DBAuthUser o where o.mail = ?1")
    DBAuthUser findByMail(String mail);

    @Query("select o from DBAuthUser o where o.linktoken like ?1%")
    DBAuthUser findByLinktoken(String linktoken);


}