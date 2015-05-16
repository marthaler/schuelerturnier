package ch.emad.web.schuetu;
/**
 * Apache License 2.0
 */
import ch.emad.business.schuetu.Business;
import ch.emad.business.schuetu.vorbereitung.A0SpielVorbereitungsKontroller;
import ch.emad.model.schuetu.model.enums.SpielPhasenEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class SpielstatusWebBean implements Serializable {

    @Autowired
    private Business business;

    @Autowired
    private A0SpielVorbereitungsKontroller kontroller;

    private boolean init = false;

    private boolean anmeldephase = false;
    private boolean kategoriezuordnungsphase = false;
    private boolean spieltagezuordnungsphase = false;
    private boolean spielzuordnungsphase = false;
    private boolean spielphase = false;

    private boolean spielend = false;

    private void init() {

        if (!business.isDBInitialized()) {
            init = false;
            return;
        } else {
            init = true;
        }

        final SpielPhasenEnum en = this.kontroller.readSpielPhase();

        if (en == SpielPhasenEnum.A_ANMELDEPHASE) {
            setAnmeldephase();
        }

        if (en == SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG) {
            setKategorieZuordnung();
        }

        if (en == SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN) {
            setSpieltageDefinieren();
        }

        if (en == SpielPhasenEnum.D_SPIELE_ZUORDNUNG) {
            setSpieleZuordnung();
        }

        if (en == SpielPhasenEnum.E_SPIELBEREIT) {
            setSpielBereit();
        }

        if (en == SpielPhasenEnum.F_SPIELEN) {
            setSpielBereit();
        }
        if (en == SpielPhasenEnum.G_ABGESCHLOSSEN) {
            setAbgeschlossen();
        }

    }

    private void setAbgeschlossen() {
        this.anmeldephase = false;
        this.kategoriezuordnungsphase = false;
        this.spieltagezuordnungsphase = false;
        this.spielzuordnungsphase = false;
        this.spielphase = false;
        this.spielend = true;
    }

    private void setSpielBereit() {
        this.anmeldephase = false;
        this.kategoriezuordnungsphase = false;
        this.spieltagezuordnungsphase = false;
        this.spielzuordnungsphase = false;
        this.spielphase = true;
        this.spielend = true;
    }

    private void setSpieleZuordnung() {
        this.anmeldephase = false;
        this.kategoriezuordnungsphase = false;
        this.spieltagezuordnungsphase = false;
        this.spielzuordnungsphase = true;
        this.spielphase = false;
        this.spielend = false;
    }

    private void setSpieltageDefinieren() {
        this.anmeldephase = false;
        this.kategoriezuordnungsphase = false;
        this.spieltagezuordnungsphase = true;
        this.spielzuordnungsphase = false;
        this.spielphase = false;
        this.spielend = false;
    }

    private void setKategorieZuordnung() {
        this.anmeldephase = false;
        this.kategoriezuordnungsphase = true;
        this.spieltagezuordnungsphase = false;
        this.spielzuordnungsphase = false;
        this.spielphase = false;
        this.spielend = false;
    }

    private void setAnmeldephase() {
        this.anmeldephase = true;
        this.kategoriezuordnungsphase = false;
        this.spieltagezuordnungsphase = false;
        this.spielzuordnungsphase = false;
        this.spielphase = false;
        this.spielend = false;
    }

    public SpielstatusWebBean() {

    }

    public boolean isInit() {
        return init;
    }

    public void shiftSpielPhase() {
        this.kontroller.shiftSpielPhase();
        this.init();
    }

    public boolean isSpieltagezuordnungsphase() {
        init();
        return this.spieltagezuordnungsphase;
    }

    public boolean isAnmeldephase() {
        this.init();
        return this.anmeldephase;
    }

    public void setAnmeldephase(final boolean anmeldephase) {

        this.anmeldephase = anmeldephase;
        this.init();
    }

    public boolean isKategoriezuordnungsphase() {
        this.init();
        return this.kategoriezuordnungsphase;
    }

    public void setKategoriezuordnungsphase(final boolean kategoriezuordnungsphase) {
        this.init();
        this.kategoriezuordnungsphase = kategoriezuordnungsphase;
    }


    public boolean isSpielzuordnungsphase() {
        init();
        return this.spielzuordnungsphase;
    }

    public void setSpielzuordnungsphase(final boolean spielzuordnungsphase) {
        this.spielzuordnungsphase = spielzuordnungsphase;
    }

    public boolean isSpielphase() {
        return this.spielphase;
    }

    public boolean isBeendet() {
        if (this.kontroller.readSpielPhase() == SpielPhasenEnum.G_ABGESCHLOSSEN) {
            return true;
        }
        return false;
    }

    public boolean isSpielend() {
        return spielend;
    }
}
