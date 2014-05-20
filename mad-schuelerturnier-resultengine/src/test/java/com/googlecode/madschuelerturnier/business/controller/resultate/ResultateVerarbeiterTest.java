/**
 * Apache License 2.0
 */
package com.googlecode.madschuelerturnier.business.controller.resultate;

import com.googlecode.madschuelerturnier.model.Kategorie;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.compusw.RanglisteneintragHistorie;
import junit.extensions.PA;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * KorrekturHelper DateUtil
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class ResultateVerarbeiterTest {

    @Mock
    private Kategorie kat;
    @Mock
    private Map<String, RanglisteneintragHistorie> map;
    @InjectMocks
    private ResultateVerarbeiter obj = new ResultateVerarbeiter();

    @Before
    public void init() {
        Spiel spiel = new Spiel();
        //  RanglisteneintragHistorie rl = new RanglisteneintragHistorie(spiel,null,null);

        when(kat.getName()).thenReturn("neu");
        when(map.get(anyString())).thenReturn(null);
    }

    @Test
    public void finaleSuchenNormal() {

        PA.setValue(obj, "map", map);

        // PA.invokeMethod(obj, "finaleSuchenNachKlasse(com.googlecode.madschuelerturnier.model.Kategorie, java.util.List, java.util.List)", null,null,null);

        //PA.invokeMethod()

    }

    @Test
    public void testFinaleSuchenNachKlasse() {


    }
}

