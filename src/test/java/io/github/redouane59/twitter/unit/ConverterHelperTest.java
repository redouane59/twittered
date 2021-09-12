package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Includes;
import io.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import io.github.redouane59.twitter.dto.user.UserV2.UserData;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    assertEquals("1435990839013126149", ConverterHelper.getTweetIdFromUrl("https://twitter.com/Twitter/status/1435990839013126149"));
    assertEquals("1435990839013126149", ConverterHelper.getTweetIdFromUrl("https://twitter.com/Twitter/status/1435990839013126149/"));
  }

  @Test
  public void testGetTweetUrlFromId() {
    assertEquals("https://twitter.com/Twitter/status/1435990839013126149", ConverterHelper.getTweetUrl(
        TweetV2.builder()
               .data(TweetData.builder()
                              .id("1435990839013126149")
                              .build())
               .includes(Includes.builder()
                                 .users(Arrays.asList(UserData.builder().name("Twitter").build()))
                                 .build())
               .build()).get());
  }
}
