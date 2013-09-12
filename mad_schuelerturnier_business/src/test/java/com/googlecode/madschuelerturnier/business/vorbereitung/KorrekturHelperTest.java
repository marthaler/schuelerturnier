package com.googlecode.madschuelerturnier.business.vorbereitung;

import com.googlecode.madschuelerturnier.business.vorbereitung.helper.KorrekturenHelper;
import com.googlecode.madschuelerturnier.model.spiel.tabelle.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.KorrekturPersistence;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


/**
 * KorrekturHelper Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class KorrekturHelperTest {

    @Mock
    private KorrekturPersistence persistence;
    @Mock
    private SpielZeilenRepository repo;
    @InjectMocks
    private KorrekturenHelper obj = new KorrekturenHelper();

    private SpielZeile z;

    @Before
    public void init() {
        z = new SpielZeile();
        z.setPause(false);

        when(this.repo.findOne(anyLong())).thenReturn(z);
    }

    @Test
    public void testSpielZeileKorrigieren() {

        obj.spielZeileKorrigieren("2");
        verify(persistence, times(1)).save(anyString(), anyString());

        assertEquals(true, z.isPause());
    }

    @Test
    public void testSpielzeilenkorrekturAusDbAnwenden() {

        List<String> result = new ArrayList<String>();
        result.add("1");
        result.add("2");

        when(this.persistence.getKorrekturen(anyString())).thenReturn(result);
        obj.spielzeilenkorrekturAusDbAnwenden();

        verify(repo, times(2)).save(any(SpielZeile.class));
    }
}
