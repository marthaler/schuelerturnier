package ch.emad.schuetu.reports.interfaces;

public interface RechnungReportable extends CouvertReportable{

    public void setAnzahl(int anzahl);
    public void setPreis(float preis);

    public int getAnzahl();
    public float getPreis();

    public float getBetrag();

    public String getESR();
    public void setESR(String esr);

}