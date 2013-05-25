/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model;

import org.joda.time.DateTime;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface IPersistent {

    public abstract String getIdString();

    public DateTime getCreationdate();

}