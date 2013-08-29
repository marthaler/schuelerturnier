/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.persistence.repository;

import com.googlecode.mad_schuelerturnier.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for {@link com.googlecode.mad_schuelerturnier.model.Text} instances. Provides basic CRUD operations due to the
 * extension of {@link org.springframework.data.jpa.repository.JpaRepository}. Includes custom
 * implemented functionality by extending {@link UserRepositoryCustom}.
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface TextRepository extends JpaRepository<Text, Long> {

    @Query("select o from Text o where o.key = ?1")
    public Text findTextByKey(String key);

}