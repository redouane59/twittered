package com.github.redouane59.twitter.unit;

import com.github.redouane59.twitter.helpers.ConverterHelper;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConverterHelperTest {

    @Test
    public void testGetDateFromString(){
        assertNotNull(ConverterHelper.getDateFromString("20170101"));
        assertNotNull(ConverterHelper.getDateFromString("201701010000"));
        assertNull(ConverterHelper.getDateFromString("202001"));
    }

    @Test
    public void testGetStringFromDate(){
        assertNotNull(ConverterHelper.getStringFromDate(new Date()));
    }

    @Test
    public void testDayBefore(){
        assertNotNull(ConverterHelper.dayBeforeNow(1));
    }

    @Test
    public void testMinutesBefore(){
        assertNotNull(ConverterHelper.minutesBeforeNow(1));
    }
}
