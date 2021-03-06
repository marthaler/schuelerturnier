/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model;


import ch.emad.model.schuetu.interfaces.CouvertReportable;
import ch.emad.model.schuetu.interfaces.RechnungReportable;
import ch.emad.model.schuetu.model.enums.AnredeEnum;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.ByteArrayInputStream;

/**
 * Ein Kontakt zum Versand von Couverts und Serienbriefen
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.13
 */
@Entity
public class Kontakt extends Persistent implements CouvertReportable, RechnungReportable {

    @Transient
    private byte[] stamp;

    private String esr;

    private AnredeEnum anrede = AnredeEnum.AN;
    private String name;
    private String vorname;
    private String strasse;
    private String PLZ;
    private String ort;

    private String liste;
    private String ressor;

    private boolean rechnung = false;

    private int anzahl;
    private float preis;
    private String betrag;

    private boolean enabled;

    public String getListe() {
        return liste;
    }

    public void setListe(String liste) {
        this.liste = liste;
    }

    public String getRessor() {
        return ressor;
    }

    public void setRessor(String ressor) {
        this.ressor = ressor;
    }


    @Override
    public String getAnrede() {
        return this.anrede.getText();
    }

    @Override
    public String getNameVorname() {
        return name + " " + vorname;
    }

    @Override
    public String getStrasse() {
        return this.strasse;
    }

    @Override
    public String getPLZOrt() {
        return this.PLZ + " " + ort;
    }

    @Override
    public ByteArrayInputStream getStamp() {
        return new ByteArrayInputStream(stamp);
    }

    @Override
    public void setStamp(byte[] stamp) {
        this.stamp = stamp;
    }

    public void setAnrede(String anrede) {
        this.anrede = AnredeEnum.fromString(anrede);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getPLZ() {
        return PLZ;
    }

    public void setPLZ(String PLZ) {
        this.PLZ = PLZ;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }


    public boolean isRechnung() {
        return rechnung;
    }

    public void setRechnung(boolean rechnung) {
        this.rechnung = rechnung;
    }

    @Override
    public int getAnzahl() {
        return anzahl;
    }

    @Override
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }


    public String getBetrag() {
        return "" + betrag;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public String getESR() {
        return esr;
    }

    @Override
    public void setESR(String esr) {
        this.esr = esr;
    }

    public void setBetrag(String betrag) {
        this.betrag = betrag;
    }


    @Override
    public float getPreis() {
        return preis;
    }

    @Override
    public void setPreis(float preis) {
        this.preis = preis;
    }

    // jxls
    public void setId(Long id) {  // NOSONAR
        super.setId(id);
    }
}
