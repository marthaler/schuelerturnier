/**
 * Apache License 2.0
 */
package com.googlecode.mad_schuelerturnier.model.spiel;

import com.googlecode.mad_schuelerturnier.model.Gruppe;
import com.googlecode.mad_schuelerturnier.model.Mannschaft;
import com.googlecode.mad_schuelerturnier.model.Paarung;
import com.googlecode.mad_schuelerturnier.model.Text;
import com.googlecode.mad_schuelerturnier.model.enums.PlatzEnum;
import com.googlecode.mad_schuelerturnier.model.enums.SpielEnum;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Ein Spiel, es wird mittels Enum unterschieden ob es sich um ein Gruppen- oder Finalspiel handelt
 *
 * @author marthaler.worb@gmail.com
 * @since 0.7
 */
@Entity
public class Spiel extends AbstractPersistable<Long> {

    private static final int NOT_INIT_FLAG = -1;
    private static final int PUNKTE_UNENTSCHIEDEN = 1;
    private static final int PUNKTE_NIEDERLAGE = 0;
    private static final int PUNKTE_SIEG = 3;

    private static final long serialVersionUID = 1L;

    private SpielEnum typ = SpielEnum.GRUPPE;

    private PlatzEnum platz = null;
    private Date start = null;

    private int toreA = NOT_INIT_FLAG;
    private int toreB = NOT_INIT_FLAG;

    private int toreABestaetigt = NOT_INIT_FLAG;
    private int toreBBestaetigt = NOT_INIT_FLAG;

    private String idString = null;

    private boolean fertigGespielt = false;

    private boolean amSpielen = false;
    private boolean fertigEingetragen = false;
    private boolean fertigBestaetigt = false;
    private boolean zurueckgewiesen = false;

    // hilfsfeld zum ausgeben der finale, falls noch keine mannschaft bestimmt wurde
    private String kategorieName;

    @OneToOne(cascade = {CascadeType.ALL})
    private Text notizen = new Text();

    // fuer jxl import
    @Transient
    private int mannschaftAId;

    @Transient
    private int mannschaftBId;

    @OneToOne
    @Deprecated
    private Paarung paarung;

    @OneToOne
    private Mannschaft mannschaftA;

    @OneToOne
    private Mannschaft mannschaftB;

    public Spiel() {

    }

    // getter und setter fuer xls export und import
    // NOSONAR
    public void setId(Long id) {
        super.setId(id);
    }

    public void setNotes(String notes) {
        if (this.notizen == null) {
            this.notizen = new Text();
        }
        this.notizen.setValue(notes);
    }

    public void setTypString(String typ) {
        typ = typ.toLowerCase();
        if (typ.equals("gfinal")) {
            this.typ = SpielEnum.GFINAL;
        }
        if (typ.equals("kfinal")) {
            this.typ = SpielEnum.KFINAL;
        }
        if (typ.equals("gruppe")) {
            this.typ = SpielEnum.GRUPPE;
        }
    }

    public void setPlatzString(String platz) {
        platz = platz.toLowerCase();
        if (platz.equals("a")) {
            this.platz = PlatzEnum.A;
        }
        if (platz.equals("b")) {
            this.platz = PlatzEnum.B;
        }
        if (platz.equals("c")) {
            this.platz = PlatzEnum.C;
        }
    }

    public String getMannschaftAName() {
        if (mannschaftA != null) {
            return mannschaftA.getName();
        }

        if ((paarung == null || paarung.getMannschaftA() == null) && typ == SpielEnum.GFINAL) {
            return "A, GF";
        }

        if ((paarung == null || paarung.getMannschaftA() == null) && typ == SpielEnum.KFINAL) {
            return "A, KF ";
        }

        return paarung.getMannschaftA().getName();
    }

    public String getMannschaftBName() {

        if (mannschaftB != null) {
            return mannschaftB.getName();
        }

        if ((paarung == null || paarung.getMannschaftB() == null) && typ == SpielEnum.GFINAL) {
            return "B, GF";
        }

        if ((paarung == null || paarung.getMannschaftB() == null) && typ == SpielEnum.KFINAL) {
            return "B, KF ";
        }

        return paarung.getMannschaftB().getName();
    }

    public boolean isFertiggespielt() {
        if ((this.toreABestaetigt == NOT_INIT_FLAG)
                || (this.toreBBestaetigt == NOT_INIT_FLAG)) {
            return false;
        }
        return true;
    }

    public boolean hatDieseMannschaftMitGespielt(final Mannschaft m) {
        if (m.equals(this.getMannschaftA()) || m.equals(this.getMannschaftB())) {
            return true;
        }
        return false;
    }

    public int getToreKassiert(final Mannschaft m) {
        int ret = 0;
        if (m.equals(this.getMannschaftA())) {
            if (this.toreBBestaetigt > NOT_INIT_FLAG) {
                ret = this.toreBBestaetigt;
            }
        }

        if (m.equals(this.getMannschaftB())) {
            if (this.toreABestaetigt > NOT_INIT_FLAG) {
                ret = this.toreABestaetigt;
            }
        }

        return ret;
    }

    public int getToreErziehlt(final Mannschaft m) {
        int ret = 0;
        if (m.equals(this.getMannschaftA())) {
            if (this.toreABestaetigt > NOT_INIT_FLAG) {
                ret = this.toreABestaetigt;
            }
        }

        if (m.equals(this.getMannschaftB())) {
            if (this.toreBBestaetigt > NOT_INIT_FLAG) {
                ret = this.toreBBestaetigt;
            }

        }

        return ret;
    }

    public int getPunkteA() {

        if ((this.getToreABestaetigt() < 0) || (this.getToreABestaetigt() < 0)) {
            return NOT_INIT_FLAG;
        }

        // punkte
        if (this.getToreABestaetigt() == this.getToreBBestaetigt()) {
            return PUNKTE_UNENTSCHIEDEN;
        } else if (this.getToreABestaetigt() < this.getToreBBestaetigt()) {
            return PUNKTE_NIEDERLAGE;
        } else {
            return PUNKTE_SIEG;
        }
    }

    public int getPunkteB() {

        if ((this.getToreABestaetigt() < 0) || (this.getToreABestaetigt() < 0)) {
            return NOT_INIT_FLAG;
        }

        // punkte
        if (this.getToreABestaetigt() == this.getToreBBestaetigt()) {
            return 1;
        } else if (this.getToreABestaetigt() > this.getToreBBestaetigt()) {
            return PUNKTE_NIEDERLAGE;
        } else {
            return PUNKTE_SIEG;
        }
    }

    public int getPunkteVonMannschaft(final Mannschaft m) {

        if ((this.getPunkteA() < 0) || (this.getPunkteB() < 0)) {
            return NOT_INIT_FLAG;
        }

        if (m.equals(this.getMannschaftA())) {
            return this.getPunkteA();
        }

        if (m.equals(this.getMannschaftB())) {
            return this.getPunkteB();
        }
        return NOT_INIT_FLAG;

    }

    public Mannschaft getMannschaftA() {
        return this.mannschaftA;
    }

    public Mannschaft getMannschaftB() {
        return this.mannschaftB;
    }

    // getter & setter normal

    public boolean isAmSpielen() {
        return amSpielen;
    }

    public void setAmSpielen(boolean amSpielen) {
        this.amSpielen = amSpielen;
    }

    public SpielEnum getTyp() {
        return this.typ;
    }

    public void setTyp(final SpielEnum typ) {
        this.typ = typ;
    }

    public String getIdString() {
        return this.idString;
    }

    public void setIdString(final String id) {
        this.idString = id;
    }

    public PlatzEnum getPlatz() {
        return this.platz;
    }

    public void setPlatz(final PlatzEnum platz) {
        this.platz = platz;
    }

    public Date getStart() {
        return this.start;
    }

    public void setStart(final Date start) {
        this.start = start;
    }

    public int getToreA() {
        return this.toreA;
    }

    public void setToreA(final int toreA) {
        this.toreA = toreA;
    }

    public int getToreB() {
        return this.toreB;
    }

    public void setToreB(final int toreB) {
        this.toreB = toreB;
    }

    public int getToreABestaetigt() {
        return this.toreABestaetigt;
    }

    public void setToreABestaetigt(final int toreABestaetigt) {
        this.toreABestaetigt = toreABestaetigt;
    }

    public Integer getToreBBestaetigt() {
        return this.toreBBestaetigt;
    }

    public void setToreBBestaetigt(final Integer toreBBestaetigt) {
        this.toreBBestaetigt = toreBBestaetigt;
    }

    public boolean isZurueckgewiesen() {
        return this.zurueckgewiesen;
    }

    public void setZurueckgewiesen(final boolean zurueckgewiesen) {
        this.zurueckgewiesen = zurueckgewiesen;
    }

    public Gruppe getGruppe() {


        if (this.paarung == null) {
            if (this.mannschaftA != null) {
                return this.mannschaftA.getGruppe();
            } else {
                return null;
            }
        }
        return this.paarung.getGruppe();
    }

    @Deprecated
    public Paarung getPaarung() {
        return paarung;
    }

    public void setPaarung(Paarung paarung) {
        this.paarung = paarung;
    }

    @Override
    public String toString() {

        String ret = "";

        if (this.mannschaftA != null) {
            ret = this.getMannschaftAName() + ":" + this.getMannschaftBName();
        } else if (this.typ == SpielEnum.GFINAL) {
            ret = ret + "GrFin-" + this.getKategorieName();
        } else if (this.typ == SpielEnum.KFINAL) {
            ret = ret + "KlFin-" + this.getKategorieName();
        } else {
            ret = ret + this.getTyp();
        }
        return ret;
    }

    public boolean isFertigGespielt() {
        return fertigGespielt;
    }

    public void setFertigGespielt(boolean fertigGespielt) {
        this.fertigGespielt = fertigGespielt;
    }

    public boolean isFertigEingetragen() {
        return fertigEingetragen;
    }

    public void setFertigEingetragen(boolean fertigEingetragen) {
        this.fertigEingetragen = fertigEingetragen;
    }

    public boolean isFertigBestaetigt() {
        return fertigBestaetigt;
    }

    public void setFertigBestaetigt(boolean fertigBestaetigt) {
        this.fertigBestaetigt = fertigBestaetigt;
    }

    public void setMannschaftA(Mannschaft mannschaftA) {
        this.mannschaftA = mannschaftA;
    }

    public void setMannschaftB(Mannschaft mannschaftB) {
        this.mannschaftB = mannschaftB;
    }

    public String getKategorieName() {
        return kategorieName;
    }

    public void setKategorieName(String kategorieName) {
        this.kategorieName = kategorieName;
    }

    public Text getNotizen() {
        return notizen;
    }

    public void setNotizen(Text notitzen) {
        this.notizen = notitzen;
    }

    public int getMannschaftAId() {
        return mannschaftAId;
    }

    public void setMannschaftAId(int mannschaftAId) {
        this.mannschaftAId = mannschaftAId;
    }

    public int getMannschaftBId() {
        return mannschaftBId;
    }

    public void setMannschaftBId(int mannschaftBId) {
        this.mannschaftBId = mannschaftBId;
    }
}