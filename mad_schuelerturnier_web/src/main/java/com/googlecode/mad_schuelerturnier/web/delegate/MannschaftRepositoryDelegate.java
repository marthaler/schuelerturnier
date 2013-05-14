package com.googlecode.mad_schuelerturnier.web.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.persistence.repository.MannschaftRepository;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Component
public class MannschaftRepositoryDelegate {

	@Autowired
	private MannschaftRepository mannschaftRepository;
	
	public Mannschaft findOne(String id){
		return mannschaftRepository.findOne(Long.valueOf(id));
	}
	
}
