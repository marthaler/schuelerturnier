/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Text extends Persistent {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(length = Integer.MAX_VALUE)
    @Lob
    private String value = null;

    @NotNull
    @Column(unique = true)
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

    // getter und setter fuer xls export und import
    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }


}
