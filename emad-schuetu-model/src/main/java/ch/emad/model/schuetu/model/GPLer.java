
/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model;

import javax.persistence.Entity;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@Entity
public class GPLer extends Persistent {

    private String nummer;

    private String platz;
    private String alterskategorieplatz;
    private String platzbeidegeschlechter;

    boolean weiblich;

    private String vorname;
    private String name;

    private String land;

    private String kategorie;

    public String getVerein() {
        return verein;
    }

    public void setVerein(String verein) {
        this.verein = verein;
    }

    private String verein;

    private String rel;

    private String km05;
    private String km10;

    private String km16;

    public String getKm16() {
        return km16;
    }

    @Override
    public String toString() {
        return "GPLer{" +
                "vorname='" + vorname + '\'' +
                ", name='" + name + '\'' +
                ", land='" + land + '\'' +
                ", verein='" + verein + '\'' +
                '}';
    }

    public void setNam(String nam) {
        try {
            String[] a = nam.split(",");
            name = a[0].trim();
            String[] b = a[1].split("\\(");
            vorname = b[0].trim();
            land = b[1].replace(")", "").trim();
        } catch (Exception e) {
            System.out.println("->" + e.getMessage());
            name = nam;
        }
    }

    public void setKm16(String km16) {
        this.km16 = km16;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getPlatz() {
        return platz;
    }

    public void setPlatz(String platz) {
        this.platz = platz;
    }

    public String getAlterskategorieplatz() {
        return alterskategorieplatz;
    }

    public void setAlterskategorieplatz(String alterskategorieplatz) {
        this.alterskategorieplatz = alterskategorieplatz;
    }

    public String getPlatzbeidegeschlechter() {
        return platzbeidegeschlechter;
    }

    public void setPlatzbeidegeschlechter(String platzbeidegeschlechter) {
        this.platzbeidegeschlechter = platzbeidegeschlechter;
    }

    public boolean isWeiblich() {
        return weiblich;
    }

    public void setWeiblich(boolean weiblich) {
        this.weiblich = weiblich;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getKm05() {
        return km05;
    }

    public void setKm05(String km05) {
        this.km05 = km05;
    }

    public String getKm10() {
        return km10;
    }

    public void setKm10(String km10) {
        this.km10 = km10;
    }


}
