/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.korrekturen;

import com.googlecode.mad_schuelerturnier.model.IPersistent;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
@Deprecated
public class MannschftsZuordnungsKorrektur extends AbstractPersistable<Long> implements IPersistent {

    private static final long serialVersionUID = 1L;

    private String mannschaftName = "";
    private String zielKategorie = "";

    private Date creationDate = new Date();

    public String getMannschaftName() {
        return mannschaftName;
    }

    public void setMannschaftName(String mannschaftName) {
        this.mannschaftName = mannschaftName;
    }

    public String getZielKategorie() {
        return zielKategorie;
    }

    public void setZielKategorie(String zielKategorieKey) {
        zielKategorie = zielKategorieKey;
    }

    @Override
    public String toString() {
        return "MannschftsZuordnungsKorrektur [mannschaftName="
                + mannschaftName + ", ZielKategorie=" + zielKategorie
                + "]";
    }

    public String getIdString() {
        return "" + super.getId();
    }

    public DateTime getCreationdate() {
        return new DateTime(this.creationDate);
    }

}
