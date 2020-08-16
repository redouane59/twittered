package com.github.redouane59.twitter.unit;

import com.github.redouane59.twitter.helpers.ConverterHelper;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.time.LocalDateTime;

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
