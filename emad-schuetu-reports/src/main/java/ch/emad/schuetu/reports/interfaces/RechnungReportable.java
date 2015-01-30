package ch.emad.schuetu.reports.interfaces;

public interface RechnungReportable extends CouvertReportable{

    public String getMannschaftenTotal();
    public String setMannschaftenTotal(String tot);
    public String getAnzahl();
    public String getPreis();
    public String getBetrag();
    public String getESR();
    public String setESR(String esr);

}