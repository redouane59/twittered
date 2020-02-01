package com.socialmediaraiser.twitter.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConverterHelper {

    public static String DATE_PATTERN_SIMPLE = "yyyyMMdd";
    public static String DATE_PATTERN_LARGE = "yyyyMMddHHmm";

    public static Date getDateFromString(String stringDate){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_LARGE);
        if(stringDate.length()==8){
            formatter = new SimpleDateFormat(DATE_PATTERN_SIMPLE);
        }
        try {
           return formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
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
}