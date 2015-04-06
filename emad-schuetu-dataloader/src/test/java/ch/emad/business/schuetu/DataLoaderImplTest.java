/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu;

import ch.emad.model.common.model.DBAuthUser;
import ch.emad.model.common.model.File;
import ch.emad.model.common.model.Text;
import ch.emad.model.schuetu.model.DBAuthUser2;
import ch.emad.model.schuetu.model.Kontakt;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.support.File2;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Testet den DataLoaderImpl
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.6
 */
public class DataLoaderImplTest {

    @Test
    public void testLoadAllMannschaftenDefault() {
        List<Mannschaft> mannschaften = DataLoaderImpl.getDataLoader().loadMannschaften();
        Assert.assertEquals(86, mannschaften.size());
    }

    @Test
    public void testLoadAllMannschaften2013() {
        List<Mannschaft> mannschaften = DataLoaderImpl.getDataLoader(2013).loadMannschaften();
        Assert.assertEquals(86, mannschaften.size());
    }

    @Test
    public void testLoad2013MannschaftenMaedchen() {
        List<Mannschaft> mannschaften = DataLoaderImpl.getDataLoader(2013).loadMannschaften(false, true);
        Assert.assertEquals(31, mannschaften.size());
    }

    @Test
    public void testLoadDefaultMannschaftenKnaben1and4() {
        List<Mannschaft> mannschaften = DataLoaderImpl.getDataLoader().loadMannschaften(true, false, 1, 4);
        Assert.assertEquals(13, mannschaften.size());
    }

    @Test
    public void testLoadDefaultAllSpiele() {
        List<Spiel> spiele = DataLoaderImpl.getDataLoader().loadSpiele();
        Assert.assertEquals(221, spiele.size());
    }

    @Test
    public void testLoadDefaultAllUsers() {
        List<DBAuthUser> user = DataLoaderImpl.getDataLoader().loadDBUser();
        Assert.assertEquals(5, user.size());
    }

    @Test
    public void testLoadAllAttachements() {
        List<File> file2s = DataLoaderImpl.getDataLoader().loadAttachements();
        Assert.assertEquals(0, file2s.size());
    }

    @Test
    public void testLoadAllTexte() {
        List<Text> texte = DataLoaderImpl.getDataLoader().loadTexte();
        Assert.assertEquals(1, texte.size());
        Assert.assertTrue(texte.get(0).getValue().length() > 30);
    }

    @Test
    public void testLoadAllKontakte() {
        List<Kontakt> kontakte = DataLoaderImpl.getDataLoader().loadKontakte();
        Assert.assertEquals(2, kontakte.size());
    }

}
