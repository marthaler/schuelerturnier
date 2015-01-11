/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business;

import com.googlecode.madschuelerturnier.model.DBAuthUser;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.Text;
import com.googlecode.madschuelerturnier.model.support.File;
import org.junit.Assert;
import org.junit.Ignore;
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
    @Ignore
    public void testLoadDefaultAllUsers() {
        List<DBAuthUser> user = DataLoaderImpl.getDataLoader().loadDBUser();
        Assert.assertEquals(5, user.size());
    }

    @Test
    public void testLoadAllAttachements() {
        List<File> files = DataLoaderImpl.getDataLoader().loadAttachements();
        Assert.assertEquals(0, files.size());
    }

    @Test
    public void testLoadAllTexte() {
        List<Text> texte = DataLoaderImpl.getDataLoader().loadTexte();
        Assert.assertEquals(1, texte.size());
        Assert.assertTrue(texte.get(0).getValue().length() > 30);
    }

}
