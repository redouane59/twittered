package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ConverterHelperTest {

    @Test
    public void testGetDateFromString(){
        assertNotNull(ConverterHelper.getDateFromString("20170101"));
        assertNotNull(ConverterHelper.getDateFromString("201701010000"));
        assertNull(ConverterHelper.getDateFromString("202001"));
    }

    @Test
    public void testGetStringFromDate(){
        assertNotNull(ConverterHelper.getStringFromDate(LocalDateTime.now()));
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
