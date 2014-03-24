/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

package com.googlecode.madschuelerturnier.web.util;

import com.googlecode.madschuelerturnier.business.Business;
import com.googlecode.madschuelerturnier.web.StartAction;
import com.googlecode.madschuelerturnier.web.utils.ConversionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.webflow.execution.Event;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


/**
 * StartAction Test
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class ConversionUtilTest {

    private ConversionUtil obj = new ConversionUtil();

    @Test
    public void testStart() {


        assertTrue(obj.stringToLong("4") == 4L);



    }

}



