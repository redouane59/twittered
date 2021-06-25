package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetV1;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class TweetDeserializerV1Test {

  private File  tweetFile1 = new File(getClass().getClassLoader().getResource("tests/tweet_example_v1.json").getFile());
  private Tweet tweetV1    = TwitterClient.OBJECT_MAPPER.readValue(tweetFile1, TweetV1.class);

  public TweetDeserializerV1Test() throws IOException {
  }

  @Test
  public void testTweetId() {
    assertEquals("1223664533451026434", tweetV1.getId());
  }

  @Test
  public void testTweetText() {
    assertEquals("@RedTheOne Tu t'es es sorti", tweetV1.getText());
  }

  @Test
  public void testRetweetCount() {
    assertEquals(2, tweetV1.getRetweetCount());
  }

  @Test
  public void testFavoriteCount() {
    assertEquals(1, tweetV1.getLikeCount());
  }

  @Test
  public void testReplyCount() {
    assertEquals(3, tweetV1.getReplyCount());
  }

  @Test
  public void testCreateAt() {
    assertEquals(ConverterHelper.getDateFromTwitterString("Sat Feb 01 17:48:54 +0000 2020"), tweetV1.getCreatedAt());
  }

  @Test
  public void testinReplyToUserId() {
    assertEquals("92073489", tweetV1.getInReplyToUserId());
  }

  @Test
  public void testinReplyToStatusId() {
    assertEquals("1223576831170879489", tweetV1.getInReplyToStatusId());
  }

  @Test
  public void testLang() {
    assertEquals("fr", tweetV1.getLang());
  }

  @Test
  public void testUser() {
    assertNotNull(tweetV1.getUser());
    assertEquals("723996356", tweetV1.getUser().getId());
    assertEquals("naiim75012", tweetV1.getUser().getName());
    assertEquals("France", tweetV1.getUser().getLocation());
    assertEquals(6339, tweetV1.getUser().getFollowersCount());
    assertEquals(392, tweetV1.getUser().getFollowingCount());
    assertEquals(83425, tweetV1.getUser().getTweetCount());
    assertEquals(ConverterHelper.getDateFromTwitterString("Sun Jul 29 13:24:02 +0000 2012"), tweetV1.getUser().getDateOfCreation());
  }
}
