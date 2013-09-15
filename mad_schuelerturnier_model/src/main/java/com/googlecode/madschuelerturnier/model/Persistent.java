package com.googlecode.madschuelerturnier.model;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@MappedSuperclass
public class Persistent extends AbstractPersistable<Long> implements Serializable, IPersistent {

    private static final long serialVersionUID = 1L;

    @Column(name = "ID2")
    private String id = UUID.randomUUID().toString();

    private DateTime creationdate = new DateTime();

    /* (non-Javadoc)
     * @see com.googlecode.madschuelerturnier.model.IPersistent#getId()
     */
    public String getIdString() {
        return id;
    }

    public DateTime getCreationdate() {
        return creationdate;
    }

}