/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.web.integration;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.business.DataLoaderImpl;
import com.googlecode.madschuelerturnier.model.enums.SpielPhasenEnum;
import com.googlecode.madschuelerturnier.web.FileUploadController;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.mockito.Mockito.when;

/**
 * TestSpielKontrollerTest
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-business-context-webtest.xml"})
@Transactional
public class FileUploadIntegrationTest {

    private static final Logger LOG = Logger.getLogger(FileUploadIntegrationTest.class);

    @Autowired
    private FileUploadController mrepo;

    @Autowired
    private Business business;

    private FileUploadEvent event;

    private UploadedFile file;

    private byte[] doc;

    private JunitAppender appender;

    @Before
    public void init() {

        doc = DataLoaderImpl.getDataLoader(2013).loadFile();

        file = Mockito.mock(UploadedFile.class);
        event = Mockito.mock(FileUploadEvent.class);

        when(event.getFile()).thenReturn(file);
        when(file.getContents()).thenReturn(doc);

        appender  = new JunitAppender();
        Logger.getRootLogger().addAppender(appender);

    }

    @Test
    @Rollback(true)
    public void testHandleFileUpload() {

        //Assume.assumeTrue(System.getProperty("user.name").contains("dama"));

        UploadedFile temp = event.getFile();
        byte[] doct = temp.getContents();

        Assert.assertNotNull(doct);

        mrepo.handleFileUpload(event);
        boolean fertig = false;
        while (!fertig) {

            if (business.getSpielEinstellungen().getPhase() == SpielPhasenEnum.G_ABGESCHLOSSEN) {
                fertig = true;
            } else {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            LOG.info("Phase: " + business.getSpielEinstellungen().getPhase());
        }

        Assert.assertFalse("fehler im log gefunden",appender.hasErrors());

    }
}
