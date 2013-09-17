/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.model;

import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Persistent;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.enums.SpielTageszeit;
import com.googlecode.madschuelerturnier.model.enums.SpielZeilenPhaseEnum;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class SpielZeile extends Persistent {

    private static final long serialVersionUID = 1L;

    private boolean pause = false;

    private boolean finale = false;

    private Date start;

    private boolean sonntag;

    private SpielTageszeit spieltageszeit;

    private SpielZeilenPhaseEnum phase = SpielZeilenPhaseEnum.A_ANSTEHEND;

    @Transient
    private String konfliktText;

    @OneToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Spiel a;

    @OneToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Spiel b;

    @OneToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Spiel c;

    private String gId = java.util.UUID.randomUUID().toString();

    public boolean checkEmty() {

        if (a != null) {
            return false;
        }
        if (b != null) {
            return false;
        }
        if (c != null) {
            return false;
        }
        return true;

    }

    public boolean isPause() {
        return this.pause;
    }

    public void setPause(final boolean pause) {
        this.pause = pause;
    }

    public Date getStart() {
        return this.start;
    }

    public void setStart(final Date start) {
        this.start = start;
    }

    public Spiel getA() {
        return this.a;
    }

    public void setA(final Spiel a) {
        this.a = a;
    }

    public Spiel getB() {
        return this.b;
    }

    public void setB(final Spiel b) {
        this.b = b;
    }

    public Spiel getC() {
        return this.c;
    }

    public void setC(final Spiel c) {
        this.c = c;
    }

    public String getgId() {
        return this.gId;
    }

    public boolean isFinale() {
        return this.finale;
    }

    public void setFinale(final boolean finale) {
        this.finale = finale;
    }

    public boolean isSonntag() {
        return this.sonntag;
    }

    public void setSonntag(final boolean sonntag) {
        this.sonntag = sonntag;
    }

    public boolean isKonflikt() {
        if ((this.konfliktText == null) || this.konfliktText.equals("")) {
            return false;
        }
        return true;
    }

    public String getKonfliktText() {
        return this.konfliktText;
    }

    public void setKonfliktText(final String konfliktText) {
        this.konfliktText = konfliktText;
    }

    public List<Mannschaft> getAllMannschaften() {
        final List<Mannschaft> mannschaften = new ArrayList<Mannschaft>();

        if (this.a != null) {
            if (this.a.getMannschaftA() != null) {
                mannschaften.add(this.a.getMannschaftA());
            }
            if (this.a.getMannschaftB() != null) {
                mannschaften.add(this.a.getMannschaftB());
            }
        }
        if (this.b != null) {
            if (this.b.getMannschaftA() != null) {
                mannschaften.add(this.b.getMannschaftA());
            }
            if (this.b.getMannschaftB() != null) {
                mannschaften.add(this.b.getMannschaftB());
            }
        }
        if (this.c != null) {
            if (this.c.getMannschaftA() != null) {
                mannschaften.add(this.c.getMannschaftA());
            }
            if (this.c.getMannschaftB() != null) {
                mannschaften.add(this.c.getMannschaftB());
            }
        }
        return mannschaften;
    }

    public SpielZeilenPhaseEnum getPhase() {
        return this.phase;
    }

    public void setPhase(final SpielZeilenPhaseEnum phase) {
        this.phase = phase;
    }

    public SpielTageszeit getSpieltageszeit() {
        return spieltageszeit;
    }

    public void setSpieltageszeit(SpielTageszeit spieltageszeit) {
        this.spieltageszeit = spieltageszeit;
    }

    @Override
    public String toString() {
        return "SpielZeile [pause=" + this.pause + ", finale=" + this.finale + ", start=" + this.start + ", a=" + this.a + ", b=" + this.b + ", c=" + this.c + ", sonntag=" + isSonntag() + ", zeit=" + getZeitAsString() + "]";
    }

    public boolean isEmty() {
        if (this.a == null && this.b == null && this.c == null) {
            return true;
        }
        return false;
    }

    public String getZeitAsString() {
        SimpleDateFormat form = new SimpleDateFormat("HH:mm");
        return form.format(this.start);
    }

}
