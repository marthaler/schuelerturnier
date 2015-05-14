package ch.emad.business.schuetu.websiteinfo.model;

import ch.emad.model.schuetu.model.Mannschaft;

public class MannschaftEintrag {

    public boolean hasMannschaft() {
        if (this.mannschaft == null) {
            return false;
        }
        return true;
    }

    private ch.emad.model.schuetu.model.Mannschaft mannschaft;

    public void setMannschaft(Mannschaft mannschaft) {
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