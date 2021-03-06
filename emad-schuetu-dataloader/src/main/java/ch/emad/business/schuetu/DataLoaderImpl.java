/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu;

import ch.emad.business.schuetu.xls.FromXLSLoader2;
import ch.emad.model.common.model.DBAuthUser;
import ch.emad.model.common.model.File;
import ch.emad.model.common.model.Text;
import ch.emad.model.schuetu.model.Kontakt;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Penalty;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.enums.GeschlechtEnum;
import com.google.common.io.Resources;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dient dazu zuvor gespeicherte XLS Dumps zu laden
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.5
 */
public final class DataLoaderImpl implements DataLoader {

    private static final Logger LOG = Logger.getLogger(DataLoaderImpl.class);

    private int jahr;

    private FromXLSLoader2 xls = new FromXLSLoader2();

    public static DataLoader getDataLoader() {
        return new DataLoaderImpl(2013);
    }

    public static DataLoader getDataLoader(int jahr) {
        return new DataLoaderImpl(jahr);
    }

    private DataLoaderImpl(int jahr) {
        this.jahr = jahr;
    }

    public byte[] loadFile() {
        return readFile("schuetu-" + jahr + ".xls");
    }

    @Override
    public List<Mannschaft> loadMannschaften() {
        return xls.convertXLSToMannschaften(readFile("schuetu-" + jahr + ".xls"));
    }

    @Override
    public List<Spiel> loadSpiele() {
        return xls.convertXLSToSpiele(readFile("schuetu-" + jahr + ".xls"));
    }

    @Override
    public List<DBAuthUser> loadDBUser() {
        return xls.convertXLSToDBAuthUsers(readFile("schuetu-" + jahr + ".xls"));
    }

    @Override
    public List<File> loadAttachements() {
        return xls.convertXLSToFiles(readFile("schuetu-" + jahr + ".xls"));
    }

    @Override
    public List<Text> loadTexte() {
        return xls.convertXLSToTexte(readFile("schuetu-" + jahr + ".xls"));
    }

    @Override
    public List<Penalty> loadPenalty() {
        return xls.convertXLSToPenalty(readFile("schuetu-" + jahr + ".xls"));
    }

    @Override
    public List<Kontakt> loadKontakte() {
        return xls.convertXLSToKontakt(readFile("schuetu-" + jahr + ".xls"));
    }

    @Override
    public List<Mannschaft> loadMannschaften(boolean knaben, boolean maedchen, Integer... klassenIn) {

        List<Mannschaft> result = new ArrayList<Mannschaft>();

        List<Mannschaft> temp = loadMannschaften();
        List<Integer> klassen = null;

        if (klassenIn != null && klassenIn.length > 0) {
            klassen = Arrays.asList(klassenIn);
        }

        for (Mannschaft mannschaft : temp) {
            if (knaben) {
                if (mannschaft.getGeschlecht() == GeschlechtEnum.K && containsKlasse(mannschaft, klassen)) {
                    result.add(mannschaft);
                }
            }
            if (maedchen) {
                if (mannschaft.getGeschlecht() == GeschlechtEnum.M && containsKlasse(mannschaft, klassen)) {
                    result.add(mannschaft);
                }
            }

        }

        return result;
    }

    private boolean containsKlasse(Mannschaft mannschaft, List<Integer> klassen) {
        return klassen == null || klassen.contains(mannschaft.getKlasse());
    }

    public static byte[] readFile(String file) {
        byte[] in = null;
        URL url = Resources.getResource(file);
        try {
            in = Resources.toByteArray(url);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return in;
    }
}