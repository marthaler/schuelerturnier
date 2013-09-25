package com.googlecode.madschuelerturnier.business.templateengine;

import com.googlecode.madschuelerturnier.business.controller.leiter.converter.XHTMLOutputUtil;
import com.googlecode.madschuelerturnier.business.print.PrintAgent;
import com.googlecode.madschuelerturnier.business.print.SpielPrintManager;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;
import com.googlecode.madschuelerturnier.model.enums.PlatzEnum;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


/**
 * KorrekturHelper Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class SpielPrintManagerTest {

    private static final Logger LOG = Logger.getLogger(SpielPrintManagerTest.class);
    @Mock
    private XHTMLOutputUtil cleaner;

    @Mock
    private PrintAgent printAgent;

    @Mock
    private Mannschaft a;

    @Mock
    private Mannschaft b;

    private List<Spiel> spiele = new ArrayList<Spiel>();

    @InjectMocks
    private SpielPrintManager obj = new SpielPrintManager();

    private ArgumentCaptor<String> argument;

    @Before
    public void init() {
        argument = ArgumentCaptor.forClass(String.class);
        when(a.getName()).thenReturn("a-Name");
        when(b.getName()).thenReturn("b-Name");
    }

    @Test
    public void testSpielzeilenkorrekturAusDbAnwenden() throws IOException {

        for (int i = 0; i < 17; i++) {
            Spiel s = new Spiel();
            s.setKategorieName("kat name");
            s.setIdString("AA" + i);
            s.setFertigGespielt(true);
            s.setMannschaftA(a);
            s.setMannschaftB(b);
            s.setToreABestaetigt(i);
            s.setToreBBestaetigt(i + 1);
            s.setStart(new Date(77777));
            s.setId(Long.parseLong("" + i));
            s.setPlatz(PlatzEnum.A);
            spiele.add(s);

        }
        String st = Templates.printSpiel(spiele, 8);
        LOG.info(st);

        assertTrue(st.contains("<td>16 : 17</td>"));
        assertTrue(st.contains(" <td align=\"right\" colspan=\"5\">Seite: 8</td>"));

    }
}



