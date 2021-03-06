package ch.emad.business.schuetu.print;

import ch.emad.model.schuetu.model.Mannschaft;
import ch.emad.model.schuetu.model.Spiel;
import ch.emad.model.schuetu.model.enums.PlatzEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


/**
 * KorrekturHelper DateUtil
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class SpielPrintManagerTest {


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

        for (int i = 0; i < 25; i++) {
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
            s.setPlatz(PlatzEnum.A);
            spiele.add(s);

            obj.saveSpiel(s);
        }

        verify(printAgent, times(1)).saveFileToPrint(anyString(), argument.capture());

        assertTrue(argument.getValue().contains("<td>14 : 15</td>"));

    }
}



