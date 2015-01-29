package com.googlecode.madschuelerturnier.web;

import com.googlecode.madschuelerturnier.business.vorbereitung.helper.KorrekturenHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


/**
 * StartAction DateUtil
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class TagToggleActionTest {

    @Mock
    private KorrekturenHelper helper;

    @Mock
    private RequestContext context;

    @Mock
    ParameterMap request;

    @InjectMocks
    private TagToggleAction obj = new TagToggleAction();

    @Test
    public void testExecute() {
        when(context.getRequestParameters()).thenReturn(request);
        Event ret = obj.execute(context);
        assertEquals("weiter", ret.getId());

        verify(this.helper, times(1)).spielZeileKorrigieren(anyString());
        verify(this.context, times(1)).getRequestParameters();

    }

}



