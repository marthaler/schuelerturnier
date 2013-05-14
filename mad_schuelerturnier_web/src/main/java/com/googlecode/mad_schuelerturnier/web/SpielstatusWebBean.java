package com.googlecode.mad_schuelerturnier.web;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.mad_schuelerturnier.business.ISpielKontroller;
import com.googlecode.mad_schuelerturnier.model.enums.SpielPhasenEnum;

@Component
public class SpielstatusWebBean implements Serializable {
	// TODO weiter in den guis anpassen
	@Autowired
	private ISpielKontroller kontroller;

	private boolean anmeldephase = false;
	private boolean kategoriezuordnungsphase = false;
	private boolean spieltagezuordnungsphase = false;

	public boolean isSpieltagezuordnungsphase() {
		return this.spieltagezuordnungsphase;
	}

	private boolean spielzuordnungsphase = false;
	private boolean spielphase = false;

	private void init() {
		final SpielPhasenEnum en = this.kontroller.readSpielPhase();

		if (en == SpielPhasenEnum.A_ANMELDEPHASE) {
			this.anmeldephase = true;
			this.kategoriezuordnungsphase = false;
			this.spieltagezuordnungsphase = false;
			this.spielzuordnungsphase = false;
			this.spielphase = false;
		}

		if (en == SpielPhasenEnum.B_KATEGORIE_ZUORDNUNG) {
			this.anmeldephase = false;
			this.kategoriezuordnungsphase = true;
			this.spieltagezuordnungsphase = false;
			this.spielzuordnungsphase = false;
			this.spielphase = false;
		}

		if (en == SpielPhasenEnum.C_SPIELTAGE_DEFINIEREN) {
			this.anmeldephase = false;
			this.kategoriezuordnungsphase = false;
			this.spieltagezuordnungsphase = true;
			this.spielzuordnungsphase = false;
			this.spielphase = false;
		}

		if (en == SpielPhasenEnum.D_SPIELE_ZUORDNUNG) {
			this.anmeldephase = false;
			this.kategoriezuordnungsphase = false;
			this.spieltagezuordnungsphase = false;
			this.spielzuordnungsphase = true;
			this.spielphase = false;
		}

		if (en == SpielPhasenEnum.E_SPIELBEREIT) {
			this.anmeldephase = false;
			this.kategoriezuordnungsphase = false;
			this.spieltagezuordnungsphase = false;
			this.spielzuordnungsphase = false;
			this.spielphase = true;
		}

		if (en == SpielPhasenEnum.F_SPIELEN) {
			this.anmeldephase = false;
			this.kategoriezuordnungsphase = false;
			this.spieltagezuordnungsphase = false;
			this.spielzuordnungsphase = false;
			this.spielphase = true;
		}
		if (en == SpielPhasenEnum.G_ABGESCHLOSSEN) {
			this.anmeldephase = false;
			this.kategoriezuordnungsphase = false;
			this.spieltagezuordnungsphase = false;
			this.spielzuordnungsphase = false;
			this.spielphase = false;
		}

	}

	public void shiftSpielPhase() {
		this.kontroller.shiftSpielPhase();
		this.init();
	}

	public SpielstatusWebBean() {

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
		return this.spielzuordnungsphase;
	}

	public void setSpielzuordnungsphase(final boolean spielzuordnungsphase) {
		this.spielzuordnungsphase = spielzuordnungsphase;
	}

	public boolean isSpielphase() {
		return this.spielphase;
	}

    public boolean isBeendet() {
        if(this.kontroller.readSpielPhase() == SpielPhasenEnum.G_ABGESCHLOSSEN){
            return true;
        }
        return false;
    }

}
