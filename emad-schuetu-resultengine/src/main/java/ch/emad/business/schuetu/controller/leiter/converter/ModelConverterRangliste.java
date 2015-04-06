/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.controller.leiter.converter;

import ch.emad.business.schuetu.websiteinfo.model.KlassenrangZeile;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.compusw.RanglisteneintragHistorie;
import ch.emad.model.schuetu.model.compusw.RanglisteneintragZeile;
import ch.emad.model.schuetu.model.enums.GeschlechtEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * konvertiert die RanglisteneintragHistorie in ein Endresultat
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.3.0
 */
@Component
public class ModelConverterRangliste {

    private final String[] kategorien = {"M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "K1", "K2", "K3", "K4", "K5", "K6", "K7", "K8", "K9"};



    public List<KlassenrangZeile> convertKlassenrangZeile(final Collection<RanglisteneintragHistorie> kat) {  // NOSONAR

        List<KlassenrangZeile> ret = new ArrayList();

        for (final String k : this.kategorien) {

            GeschlechtEnum geschlecht = null;
            if (k.contains("K")) {

                geschlecht = GeschlechtEnum.K;
            } else {

                geschlecht = GeschlechtEnum.M;
            }

            int klasse = Integer.parseInt(k.substring(1, 2));
            final List<Mannschaft> mannschaften = this.getRangliste(kat, klasse, geschlecht);

            KlassenrangZeile zeile = new KlassenrangZeile();
            zeile.setGeschlecht(geschlecht);
            zeile.setKlasse(klasse);

            for (int i = 0; i < 4; i++) {
                Mannschaft ma = null;
                if ((mannschaften.size() > 0) && (mannschaften.size() > i)) {
                    ma = mannschaften.get(i);
                }

                if ((ma != null && ma.getGruppe().getKategorie().isFertigGespielt())) {

                    zeile.addNext(ma);
                }
            }
            ret.add(zeile);

        }
        return ret;
    }

    private List<Mannschaft> getRangliste(final Collection<RanglisteneintragHistorie> kategorien, int klasse, GeschlechtEnum geschlecht) {

        ArrayList<Mannschaft> res = new ArrayList<Mannschaft>();

        for (RanglisteneintragHistorie ranglisteneintragHistorie : kategorien) {
            List<RanglisteneintragZeile> m = ranglisteneintragHistorie.getZeilen();

            for (RanglisteneintragZeile temp : m) {

                if (temp.getMannschaft().getGeschlecht() == geschlecht) {
                    if (temp.getMannschaft().getKlasse() == klasse) {
                        res.add(temp.getMannschaft());
                    }
                }
            }
        }
        return res;
    }
}