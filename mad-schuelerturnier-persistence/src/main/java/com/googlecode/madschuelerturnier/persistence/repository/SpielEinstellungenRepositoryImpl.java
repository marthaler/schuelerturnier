/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.persistence.repository;

import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.Text;
import com.googlecode.madschuelerturnier.model.util.XstreamUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Wandelt Einstellung in Xstream Text um, der Text wird hier zwischengespeichert
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.0
 */
@Component
public class SpielEinstellungenRepositoryImpl implements SpielEinstellungenRepository {

    private final static String KEY = "einstellung";
    @Autowired
    private TextRepository repo;

    private Text cache;

    @Override
    public SpielEinstellungen getEinstellungen() {

        if(!isInitialized()){
            initialize();
        }

        if(cache != null){
            SpielEinstellungen temp = (SpielEinstellungen) XstreamUtil.deserializeFromString(cache.getValue());
            return temp;
        }
        return null;
    }

    @Override
    public void save(SpielEinstellungen einstellung) {
        String xstream = XstreamUtil.serializeToString(einstellung);
        // cache noch nicht hier, neuen, leeren erstellen
        if(cache == null){
            cache = new Text();
            cache.setKey(KEY);
        }
        // ist anders, also speichern
        if(!xstream.equals(cache.getValue())){
            cache.setValue(xstream);
            cache = repo.save(cache);
        }
    }

    @Override
    public boolean isInitialized() {

        if(this.cache != null && !this.cache.getValue().isEmpty()){
            return true;
        }

        return false;

    }

    private void initialize(){
        if(cache == null){
            cache = repo.findTextByKey(KEY);
        }
    }

}
