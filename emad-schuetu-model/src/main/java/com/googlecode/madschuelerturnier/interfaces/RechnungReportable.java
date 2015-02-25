package com.googlecode.madschuelerturnier.interfaces;

import java.io.ByteArrayInputStream;

public interface RechnungReportable extends CouvertReportable{

    public String getAnrede();
    public String getNameVorname();
    public String getStrasse();
    public String getPLZOrt();

    public ByteArrayInputStream getStamp();
    public void setStamp(byte [] stamp);

    public int getAnzahl();
    public void setAnzahl(int anzahl);

    public float getPreis();
    public void setPreis(float preis);

    public void setESR(String esr);
    public String getESR();

    public String getBetrag();

    public void setEnabled(boolean enabled);
    public boolean getEnabled();

}