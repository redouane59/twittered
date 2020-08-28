package com.github.redouane59.twitter.helpers;

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConverterHelper {

  public static final String DATE_PATTERN_SIMPLE = "yyyyMMdd";
  public static final String DATE_PATTERN_LARGE  = "yyyyMMddHHmm";
  public static final String DATE_PATTERN_V2     = "yyyy-MM-dd'T'HH:mm:ss.000'Z'";

  private ConverterHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static LocalDateTime getDateFromString(String stringDate) {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_LARGE);
    if (stringDate.length() == 8) {
      formatter = new SimpleDateFormat(DATE_PATTERN_SIMPLE);
    }
    try {
      return formatter.parse(stringDate).toInstant()
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    } catch (ParseException e) {
      LOGGER.error(e.getMessage());
    }
    return null;
  }

  public static String getStringFromDate(LocalDateTime d) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_PATTERN_LARGE);
    return df.format(d);
  }

  public static String getStringFromDateV2(LocalDateTime d) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_PATTERN_V2);
    return df.format(d);
  }

  public static LocalDateTime dayBeforeNow(int nbDays) {
    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -nbDays);
    return cal.getTime().toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
  }

  public static LocalDateTime minutesBeforeNow(int minutes) {
    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, -minutes);
    return cal.getTime().toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
  }

  public static LocalDateTime getDateFromTwitterString(String date) {
    if (date == null) {
      return null;
    }
    final String     TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
    SimpleDateFormat sf      = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
    sf.setLenient(true);
    return Try.of(() -> sf.parse(date)).getOrNull().toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
  }

  public static LocalDateTime getDateFromTwitterDateV2(String date) {
    return Option.of(date).map(d -> LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN_V2))).getOrNull();
  }
}