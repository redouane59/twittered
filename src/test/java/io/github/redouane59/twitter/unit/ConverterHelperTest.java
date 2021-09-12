package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ConverterHelperTest {

  @Test
  public void testGetDateFromString() {
    assertNotNull(ConverterHelper.getDateFromString("20170101"));
    assertNotNull(ConverterHelper.getDateFromString("201701010000"));
    assertNull(ConverterHelper.getDateFromString("202001"));
  }

  @Test
  public void testGetStringFromDate() {
    assertNotNull(ConverterHelper.getStringFromDate(LocalDateTime.now()));
  }

  @Test
  public void testDayBefore() {
    assertNotNull(ConverterHelper.dayBeforeNow(1));
  }

  @Test
  public void testMinutesBefore() {
    assertNotNull(ConverterHelper.minutesBeforeNow(1));
  }

  @Test
  public void testGetTweetIdFromUrl() {
    assertEquals("1435990839013126149", ConverterHelper.getTweetIdFromUrl("https://twitter.com/Twitter/status/1435990839013126149").get());
    assertEquals("1435990839013126149", ConverterHelper.getTweetIdFromUrl("https://twitter.com/Twitter/status/1435990839013126149/").get());
  }

  @Test
  public void testGetTweetUrlFromId() {
    assertEquals("https://twitter.com/Twitter/status/1435990839013126149", ConverterHelper.getTweetUrlFromTweet(
        ConverterHelper.buildTweet("1435990839013126149", null, null, "Twitter")).get());
  }

  @Test
  public void testCreateTweet() {
    Tweet tweet = ConverterHelper.buildTweet("12345", "hello", "00000", "me");
    assertEquals("12345", tweet.getId());
    assertEquals("hello", tweet.getText());
    assertEquals("00000", tweet.getUser().getId());
    assertEquals("me", tweet.getUser().getName());
  }
}
