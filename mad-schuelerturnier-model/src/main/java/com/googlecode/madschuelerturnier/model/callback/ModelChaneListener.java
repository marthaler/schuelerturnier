package com.googlecode.madschuelerturnier.model.callback;

import com.googlecode.madschuelerturnier.model.Persistent;

/**
 * Callback fuer geeaenderte Models
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public interface ModelChaneListener {

    public void onChangeModel(Persistent p);
}
