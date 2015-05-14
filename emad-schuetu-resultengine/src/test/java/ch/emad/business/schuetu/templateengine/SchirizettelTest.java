package ch.emad.business.schuetu.templateengine;

import ch.emad.business.schuetu.controller.leiter.converter.HTMLSchiriConverter;
import ch.emad.business.schuetu.controller.leiter.converter.XHTMLOutputUtil;
import ch.emad.business.schuetu.print.PrintAgent;
import ch.emad.business.schuetu.print.SpielPrintManager;
import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.enums.PlatzEnum;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;


/**
 * KorrekturHelper DateUtil
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class SchirizettelTest {

    private static final Logger LOG = Logger.getLogger(SchirizettelTest.class);
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
    @Ignore // todo fix
    public void testSpielzeilenkorrekturAusDbAnwenden() throws IOException {

        for (int i = 0; i < 25; i++) {
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

        HTMLSchiriConverter c = new HTMLSchiriConverter();
        String st = Templates.printSpiel(spiele, 8);
        st = c.getTable(spiele);
        IOUtils.write(st, new FileWriter("/test.html"));
        LOG.info(st);

    }
}



