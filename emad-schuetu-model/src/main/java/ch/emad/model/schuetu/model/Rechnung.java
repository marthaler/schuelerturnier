/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model;

import org.apache.log4j.Logger;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class Rechnung extends Persistent {

    private static final Logger LOG = Logger.getLogger(Rechnung.class);

    private static final long serialVersionUID = 1L;

    private Integer rechnungsnummer = 0;

    private String debitor;

    private Date rechnungsversand;

    private Float rechnungsbetrag;

    private Date zahlungseingang;

    private Float zahlungsbetrag;

    public int getRechnungsnummer() {
        return rechnungsnummer;
    }

    public void setRechnungsnummer(int rechnungsnummer) {
        this.rechnungsnummer = rechnungsnummer;
    }

    public String getDebitor() {
        return debitor;
    }

    public void setDebitor(String debitor) {
        this.debitor = debitor;
    }

    public Date getRechnungsversand() {
        return rechnungsversand;
    }

    public void setRechnungsversand(Date rechnungsversand) {
        this.rechnungsversand = rechnungsversand;
    }

    public float getRechnungsbetrag() {
        return rechnungsbetrag;
    }

    public void setRechnungsbetrag(float rechnungsbetrag) {
        this.rechnungsbetrag = rechnungsbetrag;
    }

    public Date getZahlungseingang() {
        return zahlungseingang;
    }

    public void setZahlungseingang(Date zahlungseingang) {
        this.zahlungseingang = zahlungseingang;
    }

    public float getZahlungsbetrag() {
        return zahlungsbetrag;
    }

    public void setZahlungsbetrag(float zahlungsbetrag) {
        this.zahlungsbetrag = zahlungsbetrag;
    }

    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }
}
