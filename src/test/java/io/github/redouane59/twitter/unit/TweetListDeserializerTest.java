package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.ReplySettings;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.tweet.TweetV2.MediaEntityV2;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class TweetListDeserializerTest {

  private File      tweetListFile = new File(getClass().getClassLoader().getResource("tests/tweet_list_v2_example.json").getFile());
  private TweetList tweetList     = TwitterClient.OBJECT_MAPPER.readValue(tweetListFile, TweetList.class);

  public TweetListDeserializerTest() throws IOException {
  }

  @Test
  public void testData() {
    assertNotNull(tweetList.getData());
    assertEquals(3, tweetList.getData().size());
  }

  @Test
  public void testData0() {
    Tweet tweet = tweetList.getData().get(0);
    assertEquals("1120050519182016513", tweet.getAuthorId());
    assertEquals("1224041905333379073", tweet.getId());
    assertEquals("Twitter Web App", tweet.getSource());
    assertEquals("1224041905333379073", tweet.getConversationId());
    assertEquals("en", tweet.getLang());
    assertEquals(5, tweet.getRetweetCount());
    assertEquals(6, tweet.getReplyCount());
    assertEquals(26, tweet.getLikeCount());
    assertEquals(1, tweet.getQuoteCount());
    assertEquals(ReplySettings.EVERYONE, tweet.getReplySettings());
    assertTrue(tweet.getText().contains("JAVA"));
    assertEquals(ConverterHelper.getDateFromTwitterStringV2("2020-02-02T18:48:26.000Z"), tweet.getCreatedAt());
  }

  @Test
  public void testIncludes() {
    assertNotNull(tweetList.getIncludes().getUsers());
    assertEquals(3, tweetList.getIncludes().getMedia().size());
    assertNotNull(tweetList.getIncludes().getMedia());
    assertEquals(4, tweetList.getIncludes().getUsers().size());
  }

  @Test
  public void testIncludes0() {
    User user = tweetList.getIncludes().getUsers().get(0);
    assertEquals("Redouane", user.getDisplayedName());
    assertEquals("RedouaneBali", user.getName());
    assertEquals("Algiers, Algeria", user.getLocation());
    assertEquals(2018, user.getFollowersCount());
    assertEquals(1251, user.getFollowingCount());
    assertEquals(838, user.getTweetCount());
    assertEquals("1120050519182016513", user.getId());
  }

  @Test
  public void testMedia0() {
    MediaEntityV2 media = tweetList.getIncludes().getMedia().get(0);
    assertEquals("7_1460322142680072196", media.getKey());
    assertEquals(900, media.getHeight());
    assertEquals("video", media.getType());
    assertEquals(11093, media.getDuration());
    assertEquals(64061, media.getPublicMetrics().getViewCount());
    assertEquals(1600, media.getWidth());
  }

  @Test
  public void testUserIncludeInTweetObject() {
    User user = tweetList.getData().get(0).getUser();
    assertEquals("Redouane", user.getDisplayedName());
    assertEquals("RedouaneBali", user.getName());
    assertEquals("Algiers, Algeria", user.getLocation());
    assertEquals(2018, user.getFollowersCount());
    assertEquals(1251, user.getFollowingCount());
    assertEquals(838, user.getTweetCount());
    assertEquals("1120050519182016513", user.getId());
  }

}
