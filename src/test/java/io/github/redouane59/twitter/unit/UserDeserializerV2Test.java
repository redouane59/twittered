package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserV2;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class UserDeserializerV2Test {

  private File userFile2 = new File(getClass().getClassLoader().getResource("tests/user_example_v2.json").getFile());
  private User userV2    = TwitterClient.OBJECT_MAPPER.readValue(userFile2, UserV2.class);

  public UserDeserializerV2Test() throws IOException {
  }

  @Test
  public void testGetUserIdV2() {
    assertEquals("92073489", userV2.getId());
  }

  @Test
  public void testGetUserScreenNameV2() {
    assertEquals("RedouaneBali", userV2.getName());
  }

  @Test
  public void testGetUserDisplayedName() {
    assertEquals("Red'1", userV2.getDisplayedName());
  }

  @Test
  public void testGetUserLocationV2() {
    assertEquals("Madrid, Spain", userV2.getLocation());
  }

  @Test
  public void testGetUserDescriptionV2() {
    assertEquals(
        "En vérité, j'suis à ma place quand je les dérange. Jamais dans la tendance, toujours dans la bonne direction... #Lille #Montréal #Madrid \uD83D\uDC8D\uD83C\uDDE9\uD83C\uDDFF\uD83C\uDDEE\uD83C\uDDF9",
        userV2.getDescription());
  }

  @Test
  public void testGetUserFollowersCount() {
    assertEquals(5708, userV2.getFollowersCount());
  }

  @Test
  public void testGetUserFollowingCount() {
    assertEquals(2357, userV2.getFollowingCount());
  }

  @Test
  public void testGetUserTweetsCount() {
    assertEquals(38793, userV2.getTweetCount());
  }

  @Test
  public void testGetUserDateOfCreation() {
    assertEquals(ConverterHelper.getDateFromTwitterStringV2("2009-11-23T17:53:15.000Z"), userV2.getDateOfCreation());
  }

  @Test
  public void testIsVerified() {
    assertTrue(userV2.isVerified());
  }

  @Test
  public void testGetUserPinnedTweet() {
    Tweet pinnedTweet = userV2.getPinnedTweet();
    assertNotNull(pinnedTweet);
    assertEquals("92073489", pinnedTweet.getAuthorId());
    assertEquals(ConverterHelper.getDateFromTwitterStringV2("2018-08-30T15:50:15.000Z"), pinnedTweet.getCreatedAt());
    assertEquals("1035192987008020480", pinnedTweet.getId());
    assertEquals("fr", pinnedTweet.getLang());
    assertEquals(1910, pinnedTweet.getRetweetCount());
    assertEquals(232, pinnedTweet.getReplyCount());
    assertEquals(2286, pinnedTweet.getLikeCount());
    assertEquals(237, pinnedTweet.getQuoteCount());
    assertTrue(pinnedTweet.getText().contains("Thread"));
  }

  @Test
  public void testUrl() {
    assertEquals("www.google.com", userV2.getUrl());
  }

  @Test
  public void testEntities() {
    assertNotNull(userV2.getEntities());
    assertNotNull(userV2.getEntities().get("description"));
    assertNotNull(userV2.getEntities().get("description").get("hashtags"));
  }

  @Test
  public void testSerialization() throws JsonProcessingException {
    String userAsString = TwitterClient.OBJECT_MAPPER.writeValueAsString(userV2);
    assertNotNull(userAsString);
    assertTrue(userAsString.contains("RedouaneBali"));
  }

  @Test
  public void testProtectedACcount() {
    assertTrue(userV2.isProtectedAccount());
  }

}



