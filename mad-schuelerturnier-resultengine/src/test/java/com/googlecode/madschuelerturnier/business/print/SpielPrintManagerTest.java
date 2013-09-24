package com.googlecode.madschuelerturnier.business.print;

import com.googlecode.madschuelerturnier.business.controller.leiter.converter.XHTMLOutputUtil;
import com.googlecode.madschuelerturnier.model.Mannschaft;
import com.googlecode.madschuelerturnier.model.Spiel;


import org.apache.commons.io.FileUtils;
import org.apache.velocity.texen.util.FileUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;


/**
 * KorrekturHelper Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class SpielPrintManagerTest {

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
            s.setStart(new Date());
            s.setId(Long.parseLong("" + i));
            spiele.add(s);

            obj.saveSpiel(s);
        }


        verify(cleaner, times(1)).cleanup(argument.capture(), anyBoolean());

        //verify(printAgent,times(1)).saveFileToPrint(argument.capture(),anyString());


        System.out.println(argument.getValue());

        try {
            FileUtils.write(new File("/test.html"), argument.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}



