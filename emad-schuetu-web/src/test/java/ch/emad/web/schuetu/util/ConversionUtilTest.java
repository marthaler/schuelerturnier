/***************************************************************************************************************************************************************************************************************************
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2014.
 */

package ch.emad.web.schuetu.util;

import ch.emad.web.schuetu.utils.ConversionUtil2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;


/**
 * StartAction DateUtil
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 1.2.6
 */
@RunWith(MockitoJUnitRunner.class)
public class ConversionUtilTest {

    private ConversionUtil2 obj = new ConversionUtil2();

    @Test
    public void testStart() {


        assertTrue(obj.stringToLong("4") == 4L);



    }

}



