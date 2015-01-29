/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

package com.googlecode.madschuelerturnier.business.picture;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.SpielEinstellungen;
import com.googlecode.madschuelerturnier.model.support.File;
import com.googlecode.madschuelerturnier.persistence.repository.FileRepository;
import com.googlecode.madschuelerturnier.persistence.repository.SpielRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import static org.mockito.Mockito.when;


/**
 * StartAction DateUtil
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class WebcamBusinessImplTest {

    @Mock
    private Spiel spiel2;

    @Mock
    private Business business;

    @Mock
    private SpielRepository spielRepo;

    @Mock
    private FileRepository fileRepo;

    @Mock
    SpielEinstellungen einstellungen;

    @InjectMocks
    private WebcamBusiness obj = new WebcamBusinessImpl();

    @Test
    public void testFindeSpiel() {

        when(this.business.getSpielEinstellungen()).thenReturn(einstellungen);
        when(this.einstellungen.isWebcamdemomode()).thenReturn(Boolean.TRUE);
        Spiel spiel = obj.findeSpiel("AA");

        Assert.assertNotNull(spiel);

        when(this.einstellungen.isWebcamdemomode()).thenReturn(Boolean.FALSE);

        spiel = obj.findeSpiel("AA");
        Assert.assertNull(spiel);

    }

    @Test
    public void testSave() {

        when(this.business.getSpielEinstellungen()).thenReturn(einstellungen);
        when(this.einstellungen.isWebcamdemomode()).thenReturn(Boolean.TRUE);

         obj.save(spiel2, null);

        verify(this.fileRepo,never()).save(any(File.class));
        verify(this.spielRepo,never()).save(any(Spiel.class));

        when(this.einstellungen.isWebcamdemomode()).thenReturn(Boolean.FALSE);

        obj.save(spiel2, null);

        verify(this.fileRepo,times(0)).save(any(File.class));
        verify(this.spielRepo,times(0)).save(any(Spiel.class));

    }

    @Test
    public void testFindSpielByDecodedPic() throws  Exception{

        when(this.business.getSpielEinstellungen()).thenReturn(einstellungen);
        when(this.einstellungen.isWebcamdemomode()).thenReturn(Boolean.TRUE);

        URL defaultImage = this.getClass().getResource("/pictures/test.png");

        BufferedImage img = ImageIO.read(new java.io.File(defaultImage.getPath().toString()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( img, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        Spiel spiel = obj.findSpielByDecodedPic(imageInByte);
        Assert.assertNotNull(spiel);

    }
}



