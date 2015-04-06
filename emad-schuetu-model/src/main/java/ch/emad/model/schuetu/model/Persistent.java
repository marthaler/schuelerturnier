package ch.emad.model.schuetu.model;

import ch.emad.model.schuetu.model.callback.ModelChangeListenerManager;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@MappedSuperclass
public class Persistent extends AbstractPersistable<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date creationdate = new Date();

    public Date getCreationdate() {
        return creationdate;
    }

    @PostPersist
    @PostUpdate
    void onChangeInDatabase() {
        ModelChangeListenerManager.getInstance().publish(this);
    }

}