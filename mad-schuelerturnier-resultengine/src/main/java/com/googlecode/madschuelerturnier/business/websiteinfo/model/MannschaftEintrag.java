package com.googlecode.madschuelerturnier.business.websiteinfo.model;

public class MannschaftEintrag {

    public boolean hasMannschaft() {
        if (this.mannschaft == null) {
            return false;
        }
        return true;
    }

    private com.googlecode.madschuelerturnier.model.Mannschaft mannschaft;

    public void setMannschaft(com.googlecode.madschuelerturnier.model.Mannschaft mannschaft) {
this.mannschaft = mannschaft;
    }

    public MannschaftEintrag() {

    }

    public String getName() {
        if (mannschaft != null) {
            return mannschaft.getName();
        }
        return "";
    }

    public String getSchulhaus() {
        if (mannschaft != null) {
            return mannschaft.getSchulhaus();
        }
        return "";
    }

    public String getCaptain() {
        if (mannschaft != null) {
            return mannschaft.getCaptainName();
        }
        return "";
    }

    public String getBegleitperson() {
        if (mannschaft != null) {
            return mannschaft.getBegleitpersonName();
        }
        return "";
    }}