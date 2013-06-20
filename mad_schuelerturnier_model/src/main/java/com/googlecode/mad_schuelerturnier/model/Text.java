/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model;

import com.googlecode.mad_schuelerturnier.model.enums.GeschlechtEnum;
import com.googlecode.mad_schuelerturnier.model.spiel.Spiel;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.*;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Text extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1L;

    @Column(length = Integer.MAX_VALUE)
    @Lob
    private String value = null;

    @Column(unique=true)
    private String key = null;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
