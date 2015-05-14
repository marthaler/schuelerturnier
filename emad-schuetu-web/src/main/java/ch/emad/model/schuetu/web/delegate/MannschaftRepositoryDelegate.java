/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.web.delegate;

import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.persistence.schuetu.repository.MannschaftRepository;
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
