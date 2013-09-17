package com.googlecode.madschuelerturnier.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@MappedSuperclass
public class Persistent extends AbstractPersistable<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date creationdate = new Date();

    public Date getCreationdate() {
        return creationdate;
    }

}