

package ch.emad.business.schuetu.websiteinfo.model;

import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.SpielEinstellungen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u203244 on 24.03.14.
 */
public class WebsiteInfoJahresDump {

    private List<Spiel> gruppenspiele = null;

    private List<Spiel> finalspiele = null;

    private List<TeamGruppen> knabenMannschaften = null;

    private List<TeamGruppen> maedchenMannschaften = null;

    private List<KlassenrangZeile> rangliste = new ArrayList<KlassenrangZeile>();

    private SpielEinstellungen einstellung;

    public List<Spiel> getGruppenspiele() {
        return gruppenspiele;
    }

    public void setGruppenspiele(List<Spiel> gruppenspiele) {
        this.gruppenspiele = gruppenspiele;
    }

    public List<Spiel> getFinalspiele() {
        return finalspiele;
    }

    public void setFinalspiele(List<Spiel> finalspiele) {
        this.finalspiele = finalspiele;
    }

    public List<TeamGruppen> getKnabenMannschaften() {
        return knabenMannschaften;
    }

    public void setKnabenMannschaften(List<TeamGruppen> knabenMannschaften) {
        this.knabenMannschaften = knabenMannschaften;
    }

    public List<TeamGruppen> getMaedchenMannschaften() {
        return maedchenMannschaften;
    }

    public void setMaedchenMannschaften(List<TeamGruppen> maedchenMannschaften) {
        this.maedchenMannschaften = maedchenMannschaften;
    }

    public List<KlassenrangZeile> getRangliste() {
        return rangliste;
    }

    public void setRangliste(List<KlassenrangZeile> rangliste) {
        this.rangliste = rangliste;
    }

    public SpielEinstellungen getEinstellung() {
        return einstellung;
    }

    public void setEinstellung(SpielEinstellungen einstellung) {
        this.einstellung = einstellung;
    }
}
