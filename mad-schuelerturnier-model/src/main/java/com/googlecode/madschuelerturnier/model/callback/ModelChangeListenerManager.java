package com.googlecode.madschuelerturnier.model.callback;

import com.googlecode.madschuelerturnier.model.Persistent;

import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet die ModelChangeListener
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class ModelChangeListenerManager {

    private List<ModelChangeListener> listener = new ArrayList<ModelChangeListener>();

    private static ModelChangeListenerManager instance;

    public static ModelChangeListenerManager getInstance() {
        if(instance == null){
            instance = new ModelChangeListenerManager();
        }
        return instance;
    }

    private ModelChangeListenerManager(){

    }

    public void addListener(ModelChangeListener listener){
        this.listener.add(listener);
    }

    public void removeListener(ModelChangeListener listener){
        this.listener.remove(listener);
    }

    public void publish(Persistent p){
        for (ModelChangeListener l :listener) {
            l.onChangeModel(p);
        }
    }

}
