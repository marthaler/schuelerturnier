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

    private List<ModelChaneListener> listener = new ArrayList<ModelChaneListener>();

    private static ModelChangeListenerManager instance;

    public static ModelChangeListenerManager getInstance() {
        if(instance == null){
            instance = new ModelChangeListenerManager();
        }
        return instance;
    }

    private ModelChangeListenerManager(){

    }

    public void addListener(ModelChaneListener listener){
        this.listener.add(listener);
    }

    public void removeListener(ModelChaneListener listener){
        this.listener.remove(listener);
    }

    public void publish(Persistent p){
        for (ModelChaneListener l :listener) {
            l.onChangeModel(p);
        }
    }

}
