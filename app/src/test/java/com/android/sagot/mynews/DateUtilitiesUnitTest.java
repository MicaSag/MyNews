package com.android.sagot.mynews;

import com.android.sagot.mynews.Utils.DateUtilities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *   Unit Test of the app MyNews
 */
public class DateUtilitiesUnitTest {
    @Test
    public void dateReformatTest() {
        String originDate = "2018-05-08T12:37:10-04:00";         //Original date to reformat
        assertEquals("08/05/18", DateUtilities.dateReformat(originDate));
    }
}