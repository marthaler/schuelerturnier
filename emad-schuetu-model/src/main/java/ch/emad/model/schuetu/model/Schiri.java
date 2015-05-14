/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model;

import ch.emad.model.common.model.DBAuthUser;

import javax.persistence.Entity;
import java.util.List;

/**
 * Schiri User
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
@Entity
public class Schiri extends DBAuthUser {

    private boolean aktiviert = false;

    private int matchcount = 0;

    private String spielIDs ="";

    public Schiri(){
        List<String> auths = super.getAuths();
        auths.add("schiri");
        super.setAuths(auths);
    }

    public String getSpielIDs() {
        return spielIDs;
    }

    public void setSpielIDs(String spielIDs) {
        this.spielIDs = spielIDs;
    }

    public boolean isAktiviert() {
        return aktiviert;
    }

    public void setAktiviert(boolean aktiviert) {
        this.aktiviert = aktiviert;
    }

    public int getMatchcount() {
        return matchcount;
    }

    public void setMatchcount(int matchcount) {
        this.matchcount = matchcount;
    }

}
