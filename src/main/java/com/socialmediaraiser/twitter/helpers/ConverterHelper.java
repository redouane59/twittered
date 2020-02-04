package com.socialmediaraiser.twitter.helpers;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.CustomLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@CustomLog
public class ConverterHelper {

    public static final String DATE_PATTERN_SIMPLE = "yyyyMMdd";
    public static final String DATE_PATTERN_LARGE = "yyyyMMddHHmm";

    private ConverterHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static Date getDateFromString(String stringDate){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_LARGE);
        if(stringDate.length()==8){
            formatter = new SimpleDateFormat(DATE_PATTERN_SIMPLE);
        }
        try {
           return formatter.parse(stringDate);
        } catch (ParseException e) {
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

    public static String getStringFromDate(Date d){
        DateFormat df = new SimpleDateFormat(DATE_PATTERN_LARGE);
        return df.format(d);
    }

    public static Date dayBeforeNow(int nbDays) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -nbDays);
        return cal.getTime();
    }

    public static Date minutesBeforeNow(int minutes) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -minutes);
        return cal.getTime();
    }

    public static Date getDateFromTwitterString(String date)
    {
        if(date==null) return null;
        final String TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        return Try.of(() -> sf.parse(date)).getOrNull();
    }

    public static Date getDateFromTwitterDateV2(String date)
    {
        return Option.of(date).map(d -> Date.from(Instant.parse(date))).getOrNull();
    }
}