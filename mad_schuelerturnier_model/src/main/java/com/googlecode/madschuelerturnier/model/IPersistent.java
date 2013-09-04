/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import org.joda.time.DateTime;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public interface IPersistent {

    String getIdString();

    DateTime getCreationdate();

}