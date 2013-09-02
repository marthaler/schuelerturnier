package com.googlecode.madschuelerturnier.web.delegate;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class MannschaftRepositoryDelegate {

    @Autowired
    private MannschaftRepository mannschaftRepository;

    public Mannschaft findOne(String id) {
        return mannschaftRepository.findOne(Long.valueOf(id));
    }

}
