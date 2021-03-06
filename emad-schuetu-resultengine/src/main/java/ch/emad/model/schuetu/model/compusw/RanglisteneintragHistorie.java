/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.compusw;

import ch.emad.model.schuetu.model.*;
import ch.emad.model.schuetu.model.comperators.MannschaftsNamenComperator;
import ch.emad.model.schuetu.model.enums.RangierungsgrundEnum;
import ch.emad.model.schuetu.model.enums.SpielEnum;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class RanglisteneintragHistorie {

    private static final Logger LOG = Logger.getLogger(RanglisteneintragHistorie.class);

    private RanglisteneintragHistorie vorherigerEintrag = null;
    private Spiel spiel = null;
    private Gruppe gruppe = null;

    private List<Mannschaft> mannschaften = null;
    private final List<RangierungsgrundEnum> rangierungsGrund = new ArrayList<RangierungsgrundEnum>();

    private List<RanglisteneintragZeile> zeilen = new ArrayList<RanglisteneintragZeile>();

    private SpielEnum grund = null;

    private Kategorie kat = null;

    public RanglisteneintragHistorie(final Spiel spiel, final RanglisteneintragHistorie vorherigerEintrag, Boolean a) {

        this.vorherigerEintrag = vorherigerEintrag;

        if (spiel == null) {
            this.gruppe = vorherigerEintrag.getGruppe();
            this.cloneZeilen();
            this.kat = this.vorherigerEintrag.getKategorie();
        } else {

            this.kat = spiel.getGruppe().getMannschaften().get(0).getKategorie();
            this.gruppe = spiel.getGruppe();
            this.spiel = spiel;

            if (a == null) {
                List<Mannschaft> list = gruppe.getKategorie().getMannschaften();
                Collections.sort(list, new MannschaftsNamenComperator());
                this.mannschaften = list;
            } else if (a) {
                this.mannschaften = gruppe.getKategorie().getGruppeA().getMannschaften();
            } else {
                this.mannschaften = gruppe.getKategorie().getGruppeB().getMannschaften();
            }
            this.addNewZeilen();
        }

        if (spiel == null) {
            this.sortNachPenalty();
        } else {

            this.sortNachPunkten();
            this.sortNachTorverhaeltnis();
            MehrToreSortierer.sortNachMehrToren(this);
            DirektbegegnungSortierer.sortNachDirektbegegnung(this);
            this.penaltyBestimmen();
            this.penaltyBestimmen();
            this.sortNachFinal();
        }

    }

    public boolean isFertigGespielt() {
        for (RanglisteneintragZeile zeile : this.zeilen) {
            if (zeile.getSpieleAnstehend() > 0) {
                return false;
            }
            if (zeile.getRangierungsgrund() == RangierungsgrundEnum.PENALTY) {
                return false;
            }
        }
        return true;
    }

    public boolean isPenaltyAuswertungNoetig() {
        for (RanglisteneintragZeile zeile : this.zeilen) {
            if (zeile.getSpieleAnstehend() != 0) {
                return false;
            }
            // auch nicht wenn der penalty noch nicht bestaetigt wurde
            if (this.getPenaltyA() != null && !this.getPenaltyA().isBestaetigt()) {
                return false;
            }
            if (this.getPenaltyB() != null && !this.getPenaltyB().isBestaetigt()) {
                return false;
            }
            if (zeile.getRangierungsgrund() == RangierungsgrundEnum.PENALTY) {
                return true;
            }
        }
        return false;
    }

    private void cloneZeilen() {
        for (RanglisteneintragZeile zeile : this.vorherigerEintrag.getZeilen()) {
            RanglisteneintragZeile zeileN = new RanglisteneintragZeile();

            zeileN.setSpieleVorbei(zeile.getSpieleVorbei());
            zeileN.setMannschaft(zeile.getMannschaft());

            zeileN.setSpieleAnstehend(zeile.getSpieleAnstehend());
            zeileN.setToreErziehlt(zeile.getToreErziehlt());
            zeileN.setToreKassiert(zeile.getToreKassiert());

            zeileN.setRangierungsgrund(zeile.getRangierungsgrund());

            zeileN.setPunkte(zeile.getPunkte());

            this.zeilen.add(zeileN);
        }
    }

    private void addNewZeilen() {

        // ist erster eintrag, also werden alle zeilen neu erstellt
        if (this.vorherigerEintrag == null) {
            for (Mannschaft mannschaft : mannschaften) {
                RanglisteneintragZeile zeile = new RanglisteneintragZeile();

                zeile.setSpieleVorbei(0);
                zeile.setMannschaft(mannschaft);

                zeile.setSpieleAnstehend(mannschaft.getGesammtzahlGruppenSpiele());
                zeile.setToreErziehlt(0);
                zeile.setToreKassiert(0);

                zeile.setPunkte(0);

                this.zeilen.add(zeile);
            }
        } else {
            // kopie von vorherigen zeilen machen
            for (Mannschaft mannschaft : mannschaften) {
                RanglisteneintragZeile zeile = new RanglisteneintragZeile();
                RanglisteneintragZeile zeileAlt = getZeileByMannschaft(mannschaft, this.vorherigerEintrag.getZeilen());

                zeile.setSpieleVorbei(zeileAlt.getSpieleVorbei());
                zeile.setMannschaft(mannschaft);

                zeile.setSpieleAnstehend(zeileAlt.getSpieleAnstehend());
                zeile.setToreErziehlt(zeileAlt.getToreErziehlt());
                zeile.setToreKassiert(zeileAlt.getToreKassiert());

                zeile.setRangierungsgrund(zeileAlt.getRangierungsgrund());

                zeile.setPunkte(zeileAlt.getPunkte());

                this.zeilen.add(zeile);
            }
        }

        // Eintragungen machen
        // Zeilen holen
        RanglisteneintragZeile zeileA = getZeileByMannschaft(spiel.getMannschaftA(), this.zeilen);
        RanglisteneintragZeile zeileB = getZeileByMannschaft(spiel.getMannschaftB(), this.zeilen);

        if (zeileA == null) {
            LOG.fatal("zeile nicht gefunden! " + spiel.getMannschaftA());
        }

        if (zeileB == null) {
            LOG.fatal("zeile nicht gefunden! " + spiel.getMannschaftB());
        }

        // anstehende Spiele
        if (zeileA != null) {
            zeileA.setSpieleAnstehend(zeileA.getSpieleAnstehend() - 1);
        }
        if (zeileB != null) {
            zeileB.setSpieleAnstehend(zeileB.getSpieleAnstehend() - 1);
        }

        // Spiele vorbei
        zeileA.setSpieleVorbei(zeileA.getSpieleVorbei() + 1);
        zeileB.setSpieleVorbei(zeileB.getSpieleVorbei() + 1);

        // erziehlte tore
        zeileA.setToreErziehlt(zeileA.getToreErziehlt() + spiel.getToreABestaetigt());
        zeileB.setToreErziehlt(zeileB.getToreErziehlt() + spiel.getToreBBestaetigt());

        // kassierte tore
        zeileA.setToreKassiert(zeileA.getToreKassiert() + spiel.getToreBBestaetigt());
        zeileB.setToreKassiert(zeileB.getToreKassiert() + spiel.getToreABestaetigt());


        if (spiel.getToreABestaetigt() > spiel.getToreBBestaetigt()) {
            zeileA.setPunkte(zeileA.getPunkte() + 3);
        } else if (spiel.getToreABestaetigt() < spiel.getToreBBestaetigt()) {
            zeileB.setPunkte(zeileB.getPunkte() + 3);
        } else {
            zeileA.setPunkte(zeileA.getPunkte() + 1);
            zeileB.setPunkte(zeileB.getPunkte() + 1);
        }
    }

    private RanglisteneintragZeile getZeileByMannschaft(Mannschaft mannschaft, List<RanglisteneintragZeile> zeilen) {
        for (RanglisteneintragZeile ranglisteneintragZeile : zeilen) {
            if (mannschaft.getName().equals(ranglisteneintragZeile.getMannschaft().getName())) {
                return ranglisteneintragZeile;
            }
        }
        return null;
    }

    private void sortNachPunkten() {

        RanglisteneintragZeile mA;

        for (int i = 0; i < this.zeilen.size(); i++) {

            mA = this.zeilen.get(i);

            for (int j = i + 1; j < this.zeilen.size(); j++) {

                final RanglisteneintragZeile mB = this.zeilen.get(j);

                if (mA.getPunkte() < mB.getPunkte()) {
                    this.zeilen.set(i, mB);
                    this.zeilen.set(j, mA);
                    mA = mB;
                }
            }

            if (mA.getSpieleVorbei() == 0) {
                mA.setRangierungsgrund(RangierungsgrundEnum.KEINSPIEL);
            } else {
                mA.setRangierungsgrund(RangierungsgrundEnum.PUNKTE);
            }
        }

        // markiere die mit gleicher punktzahl, welche aber nicht ohne spiel
        // sind
        RanglisteneintragZeile last = null;
        for (final RanglisteneintragZeile now : this.zeilen) {
            if ((last != null) && (now.getRangierungsgrund() != RangierungsgrundEnum.KEINSPIEL) && (now.getPunkte() == last.getPunkte())) {
                now.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
                last.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
            }
            last = now;
        }
    }

    private void sortNachTorverhaeltnis() {

        final List<RanglisteneintragZeile> su = new ArrayList<RanglisteneintragZeile>();
        int startindex = -1;
        RanglisteneintragZeile last = null;
        for (int i = 0; i < this.zeilen.size(); i++) {
            final RanglisteneintragZeile temp = this.zeilen.get(i);

            if ((last != null) && (last.getPunkte() != temp.getPunkte())) {
                this.subSortTorverhaeltnis(su, startindex);
                startindex = -1;
                su.clear();
            }

            if ((temp.getRangierungsgrund().equals(RangierungsgrundEnum.WEITERSUCHEN))) {

                temp.setRangierungsgrund(RangierungsgrundEnum.TORDIFFERENZ);

                su.add(temp);

                if (su.size() == 1) {
                    startindex = i;
                }
            }
            last = temp;
        }
        this.subSortTorverhaeltnis(su, startindex);
    }


    private void subSortTorverhaeltnis(final List<RanglisteneintragZeile> su, int startindexIn) {

        int startindex = startindexIn;

        if (su.size() > 1) {

            Collections.sort(su, new TorverhaeltnisComperator());
            RanglisteneintragZeile last = null;

            for (final RanglisteneintragZeile ranglisteneintragZeile : su) {

                if (last != null) {
                    if (last.getTordifferenz() == ranglisteneintragZeile.getTordifferenz()) {
                        ranglisteneintragZeile.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
                        last.setRangierungsgrund(RangierungsgrundEnum.WEITERSUCHEN);
                    }
                }

                last = ranglisteneintragZeile;
            }

            for (final RanglisteneintragZeile ranglisteneintragZeile : su) {
                this.zeilen.set(startindex, ranglisteneintragZeile);
                startindex = startindex + 1;
            }
            su.clear();
        }
    }


    private void penaltyBestimmen() {  // NOSONAR

        if (this.isGroupWith2Untergruppen() && (this.zeilen.size() > this.gruppe.getMannschaften().size())) {
            RanglisteneintragHistorie.LOG.warn("gruppe mit 2 untergruppen, hauptrangliste wird nicht nach penalty gesucht !!!");
            return;
        }

        final List<RanglisteneintragZeile> su = new ArrayList<RanglisteneintragZeile>();

        RanglisteneintragZeile last = null;

        for (int i = 0; i < this.zeilen.size(); i++) {

            if ((last == null) && (i > 3)) {
                return;
            }

            final RanglisteneintragZeile temp = this.zeilen.get(i);

            if ((temp.getRangierungsgrund().equals(RangierungsgrundEnum.PENALTY) || temp.getRangierungsgrund().equals(RangierungsgrundEnum.WEITERSUCHEN))) {
                if ((last != null) && (temp.getToreErziehlt() != last.getToreErziehlt())) {
                    this.penaltyBestimmenSub(su);

                    if (this.isGroupWith2Untergruppen()) {
                        if (i > 1) {
                            return;
                        }
                    } else {
                        if (i > 3) {
                            return;
                        }

                    }

                    su.clear();
                }

                su.add(temp);
            } else {

                if (su.size() > 1) {

                    this.penaltyBestimmenSub(su);
                    last = null;
                    if (this.isGroupWith2Untergruppen()) {
                        if (i > 1) {
                            return;
                        }
                    } else {
                        if (i > 3) {
                            return;
                        }

                    }

                    su.clear();
                }

            }
            if ((temp.getRangierungsgrund().equals(RangierungsgrundEnum.PENALTY) || temp.getRangierungsgrund().equals(RangierungsgrundEnum.WEITERSUCHEN))) {
                last = temp;
            }

        }

        if (su.size() > 1) {

            this.penaltyBestimmenSub(su);

            su.clear();
        }

    }

    private void penaltyBestimmenSub(final List<RanglisteneintragZeile> su) {

        for (RanglisteneintragZeile ze : su) {
            if (ze.getSpieleAnstehend() > 0) {
                LOG.debug("penaltyBestimmenSub(): wird nicht mehr weiterverfolgt, weil noch nicht das letzte spielkorrektur gespielt wurde");
                return;
            }
        }

        List<RanglisteneintragZeile> pList = new ArrayList<RanglisteneintragZeile>();
        for (final RanglisteneintragZeile ranglisteneintragZeile : su) {
            ranglisteneintragZeile.setRangierungsgrund(RangierungsgrundEnum.PENALTY);
            pList.add(ranglisteneintragZeile);
        }

        if (pList.size() < 1) {
            return;
        }

        // penalty bereits gesetzt
        Penalty penalty = null;
        List<Mannschaft> kandidaten = new ArrayList<Mannschaft>();

        for (RanglisteneintragZeile ranglisteneintragZeile : pList) {
            kandidaten.add(ranglisteneintragZeile.getMannschaft());
        }

        penalty = PenaltyLoaderFactory.getInstance().getPenalty(kandidaten);


        // verhindert, dass selber penalty bei a und b vorkommt
        if (kat.getPenaltyA() != null && kat.getPenaltyA().toMannschaftsString().equals(penalty.toMannschaftsString())) {
            LOG.debug("kein neuer penaltyA: " + kat.getPenaltyA().toMannschaftsString() + " + " + penalty.toMannschaftsString());
            return;
        }

        if (this.kat.getPenaltyB() != null) {
            LOG.debug("kein neuer penaltyB: " + this.kat.getPenaltyB().toMannschaftsString() + " + " + penalty.toMannschaftsString());
            return;
        }

        penalty.setGr(this.gruppe);

        if ((this.kat.getPenaltyA() == null)) {
            LOG.info("neuer penalty st to A: " + penalty);
            this.kat.setPenaltyA(penalty);

        } else if ((this.kat.getPenaltyB() == null)) {
            LOG.info("neuer penalty st to B: " + penalty);
            this.kat.setPenaltyB(penalty);
        }

    }

    private void sortNachPenalty() {

        if (this.spiel != null) {
            return;
        }

        final List<RanglisteneintragZeile> su = new ArrayList<RanglisteneintragZeile>();
        int startindex = -1;
        RanglisteneintragZeile last = null;
        for (int i = 0; i < this.zeilen.size(); i++) {
            final RanglisteneintragZeile temp = this.zeilen.get(i);

            if (temp.getRangierungsgrund().equals(RangierungsgrundEnum.WEITERSUCHEN)
                    || temp.getRangierungsgrund().equals(RangierungsgrundEnum.PENALTY)) {
                if (temp.getRangierungsgrund().equals(RangierungsgrundEnum.PENALTY) && ((last == null) || (last.getToreErziehlt() == temp.getToreErziehlt()))) {
                    su.add(temp);
                    last = temp;
                    if (su.size() == 1) {
                        startindex = i;
                    }
                } else {

                    this.subSortNachPenalty(su, startindex);
                    last = null;

                    if (temp.getRangierungsgrund().equals(RangierungsgrundEnum.PENALTY)) {
                        su.add(temp);
                    }
                    if (su.size() == 1) {
                        startindex = i;
                    }
                }
            }
            this.subSortNachPenalty(su, startindex);
        }
    }

    private void subSortNachPenalty(final List<RanglisteneintragZeile> su, int startindexin) {

        int startindex = startindexin;

        if (su.size() > 1) {
            Penalty p = null;

            if (this.kat != null && (this.kat.getPenaltyA() != null) && this.kat.getPenaltyA().contains(su.get(0).getMannschaft())) {
                p = this.kat.getPenaltyA();
            }

            if (this.kat != null && (this.kat.getPenaltyB() != null) && this.kat.getPenaltyB().contains(su.get(0).getMannschaft())) {
                p = this.kat.getPenaltyB();
            }

            if ((p == null) || !p.contains(su.get(0).getMannschaft())) {
                return;
            }

            Collections.sort(su, new PenaltyComperator(p));

            for (final RanglisteneintragZeile ranglisteneintragZeile : su) {

                ranglisteneintragZeile.setRangierungsgrund(RangierungsgrundEnum.PENALTYOK);

            }

            for (final RanglisteneintragZeile ranglisteneintragZeile : su) {
                this.zeilen.set(startindex, ranglisteneintragZeile);
                startindex = startindex + 1;
            }

            su.clear();

        }
    }


    public int compareWithLast(final RanglisteneintragZeile dieZuPruefende) {
        if (this.vorherigerEintrag == null) {
            return 0;
        }
        final int indexJetzt = this.zeilen.indexOf(dieZuPruefende);

        final List<RanglisteneintragZeile> str = this.vorherigerEintrag.zeilen;
        for (final RanglisteneintragZeile ranglisteneintragZeile : str) {
            if (dieZuPruefende.getMannschaft().getName().equals(ranglisteneintragZeile.getMannschaft().getName())) {
                return str.indexOf(ranglisteneintragZeile) - indexJetzt;
            }
        }
        return 0;
    }


    public void print() {
        int i = 1;
        for (final RanglisteneintragZeile zeile : this.zeilen) {

            RanglisteneintragHistorie.LOG.debug(i + " " + zeile.print());
            i++;
        }
        RanglisteneintragHistorie.LOG.debug("");
    }

    private void sortNachFinal() {

        if (this.spiel.getTyp() == SpielEnum.GRUPPE) {
            LOG.info("sortNachFinal(): wird nicht ausgefuehrt, weil spiel ein gruppenspiel ist");
            return;
        }

        this.grund = spiel.getTyp();

        final List<RanglisteneintragZeile> grFinalList = new ArrayList<RanglisteneintragZeile>();

        final List<RanglisteneintragZeile> klFinalList = new ArrayList<RanglisteneintragZeile>();

        final List<RanglisteneintragZeile> restList = new ArrayList<RanglisteneintragZeile>();

        restList.addAll(this.zeilen);

        for (final RanglisteneintragZeile z : this.zeilen) {
            if (z.getRangierungsgrund() == RangierungsgrundEnum.FINAL_GR) {
                grFinalList.add(z);
            }
        }

        if (behandleGrosserFinal(grFinalList, restList)) {
            return;
        }

        restList.removeAll(grFinalList);

        for (final RanglisteneintragZeile z : this.zeilen) {
            if (z.getRangierungsgrund() == RangierungsgrundEnum.FINAL_KL) {
                klFinalList.add(z);
            }
        }

        if (behandleKleinerFinal(klFinalList, restList)) {
            return;
        }

        restList.removeAll(klFinalList);

        this.zeilen.clear();

        this.zeilen.addAll(grFinalList);
        this.zeilen.addAll(klFinalList);
        this.zeilen.addAll(restList);

    }

    private boolean behandleGrosserFinal(List<RanglisteneintragZeile> grFinalList, List<RanglisteneintragZeile> restList) {
        Spiel sp = this.gruppe.getKategorie().getGrosserFinal();

        if (sp.isFertiggespielt()) {
            RanglisteneintragZeile aZ = null;
            RanglisteneintragZeile bZ = null;

            for (final RanglisteneintragZeile ranglisteneintragZeile : restList) {
                if (ranglisteneintragZeile.getMannschaft() == sp.getMannschaftA()) {
                    aZ = ranglisteneintragZeile;
                }
                if (ranglisteneintragZeile.getMannschaft() == sp.getMannschaftB()) {
                    bZ = ranglisteneintragZeile;
                }
            }

            if (bZ == null || aZ == null) {
                LOG.debug("resultat mit mannschaft, die nicht in der liste ist bei grossem finale: return");
                return true;
            }

            final int a = sp.getToreABestaetigt();
            final int b = sp.getToreBBestaetigt();
            if (a < b) {
                bZ.setRangierungsgrund(RangierungsgrundEnum.FINAL_GR);
                grFinalList.add(bZ);
                aZ.setRangierungsgrund(RangierungsgrundEnum.FINAL_GR);
                grFinalList.add(aZ);
            } else {
                aZ.setRangierungsgrund(RangierungsgrundEnum.FINAL_GR);
                grFinalList.add(aZ);
                bZ.setRangierungsgrund(RangierungsgrundEnum.FINAL_GR);
                grFinalList.add(bZ);
            }
        }
        return false;
    }

    private boolean behandleKleinerFinal(List<RanglisteneintragZeile> klFinalList, List<RanglisteneintragZeile> restList) {
        Spiel sp2 = this.gruppe.getKategorie().getKleineFinal();
        if (sp2 != null) {
            if (sp2.isFertiggespielt()) {
                RanglisteneintragZeile aZ = null;
                RanglisteneintragZeile bZ = null;

                for (final RanglisteneintragZeile ranglisteneintragZeile : restList) {
                    if (ranglisteneintragZeile.getMannschaft() == sp2.getMannschaftA()) {
                        aZ = ranglisteneintragZeile;
                    }
                    if (ranglisteneintragZeile.getMannschaft() == sp2.getMannschaftB()) {
                        bZ = ranglisteneintragZeile;
                    }
                }

                final int a = sp2.getToreABestaetigt();
                final int b = sp2.getToreBBestaetigt();

                if (bZ == null || aZ == null) {
                    LOG.debug("resultat mit mannschaft, die nicht in der liste ist bei kleinem finale: return");
                    return true;
                }

                if (a < b) {
                    bZ.setRangierungsgrund(RangierungsgrundEnum.FINAL_KL);
                    klFinalList.add(bZ);
                    aZ.setRangierungsgrund(RangierungsgrundEnum.FINAL_KL);
                    klFinalList.add(aZ);
                } else {
                    aZ.setRangierungsgrund(RangierungsgrundEnum.FINAL_KL);
                    klFinalList.add(aZ);
                    bZ.setRangierungsgrund(RangierungsgrundEnum.FINAL_KL);
                    klFinalList.add(bZ);
                }
            }
        }
        return false;
    }

    public Penalty getPenaltyA() {
        if (this.kat != null && this.kat.getPenaltyA() != null) {
            return this.kat.getPenaltyA();
        } else {
            if (this.vorherigerEintrag != null) {
                this.vorherigerEintrag.getPenaltyA();
            }
        }
        return null;
    }

    public Penalty getPenaltyB() {
        if (this.kat != null && this.kat.getPenaltyB() != null) {
            return this.kat.getPenaltyB();
        } else {
            if (this.vorherigerEintrag != null) {
                this.vorherigerEintrag.getPenaltyB();
            }
        }
        return null;
    }

    private boolean isGroupWith2Untergruppen() {
        return this.gruppe.getKategorie().getGruppeA() != null && this.gruppe.getKategorie().getGruppeB() != null;
    }


    public SpielEnum getGrund() {
        return this.grund;
    }

    public void setGrund(final SpielEnum grund) {
        this.grund = grund;
    }

    public List<RangierungsgrundEnum> getRangierungsGrund() {
        return this.rangierungsGrund;
    }

    public Spiel getSpiel() {
        return this.spiel;
    }

    public RanglisteneintragHistorie getVorherigerEintrag() {
        return this.vorherigerEintrag;
    }

    public List<RanglisteneintragZeile> getZeilen() {
        return this.zeilen;
    }

    public Kategorie getKategorie() {
        return this.gruppe.getKategorie();
    }

    public Gruppe getGruppe() {
        return this.gruppe;
    }

}