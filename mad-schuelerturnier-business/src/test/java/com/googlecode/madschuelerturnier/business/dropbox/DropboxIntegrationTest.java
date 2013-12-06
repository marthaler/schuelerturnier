/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.dropbox;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.DataLoaderImpl;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.persistence.repository.MannschaftRepository;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.*;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * DropboxController Integration Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.8
 */
public class DropboxIntegrationTest {

    private static final Logger LOG = Logger.getLogger(DropboxConnectorImpl.class);

    //@Test
    public void testDropboxHandling() {
        DropboxConnector conn = new DropboxConnectorImpl();

        Assert.assertFalse(conn.isConnected());

        String url = conn.getLoginURL();
        LOG.info("oeffnen: " + url);
        System.out.println("code von der webseite eingeben (und enter drücken): ");
        String code = null;

        code = (String) JOptionPane.showInputDialog("Bitte Code von der URL eingeben",url);

        conn.insertToken(code);

        Assert.assertTrue(conn.isConnected());
        String ff = "";
        List<String> files = conn.getFilesInFolder();
        Assert.assertTrue(files.size() > 0);

        for(String file : files){
            ff = file;
            LOG.info("file: " + file);
        }

        byte[] dl = conn.loadFile(ff);

        Assert.assertNotNull(dl);

        Assert.assertTrue(dl.length > 2);

        conn.saveFile("file3.txt",dl);

    }

    public static void main(String[] args) {
        DropboxIntegrationTest test = new DropboxIntegrationTest();
        test.testDropboxHandling();
    }
    
}
