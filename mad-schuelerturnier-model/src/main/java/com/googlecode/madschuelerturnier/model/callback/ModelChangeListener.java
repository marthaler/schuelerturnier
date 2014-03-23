package com.googlecode.madschuelerturnier.model.callback;

import java.io.Serializable;

/**
 * Callback fuer geeaenderte Models
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public interface ModelChangeListener {

    public void onChangeModel(Serializable object);
}
