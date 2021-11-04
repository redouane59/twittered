package io.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.RelationType;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.dm.DirectMessage;
import io.github.redouane59.twitter.dto.dm.DmEvent;
import io.github.redouane59.twitter.dto.others.RequestToken;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ITwitterClientV1Test {

  private static TwitterClient twitterClient;

  @BeforeAll
  public static void init() {
    twitterClient = new TwitterClient();
  }

  @Test
  public void testFriendshipByIdYes() {
    String       userId1 = "92073489";
    String       userId2 = "723996356";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.FRIENDS, result);
  }

  @Test
  public void testFriendshipByIdNo() {
    String       userId1 = "92073489";
    String       userId2 = "1976143068";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertNotEquals(RelationType.FRIENDS, result);
  }

  @Test
  public void testGetRateLimitStatus() {
    assertNotEquals(null, twitterClient.getRateLimitStatus());
  }

  @Test
  public void testRelationBetweenUsersIdFriends() {
    String       userId1 = "92073489";
    String       userId2 = "723996356";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.FRIENDS, result);
  }

  @Test
  public void testRelationBetweenUsersIdNone() {
    String       userId1 = "92073489";
    String       userId2 = "1976143068";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.NONE, result);
  }

  @Test
  public void testRelationBetweenUsersIdFollowing() {
    String       userId1 = "92073489";
    String       userId2 = "126267113";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.FOLLOWING, result);
  }

  @Test
  public void testRelationBetweenUsersIdFollower() {
    String       userId1 = "92073489";
    String       userId2 = "1218125226095054848";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.FOLLOWER, result);
  }

  @Test
  public void testGetOauth1Token() {
    twitterClient.getTwitterCredentials().setAccessToken("");
    twitterClient.getTwitterCredentials().setAccessTokenSecret("");
    RequestToken result = twitterClient.getOauth1Token("oob");
    assertTrue(result.getOauthToken().length() > 1);
    assertTrue(result.getOauthTokenSecret().length() > 1);
    //twitterClient.getOAuth1AccessToken(result, "12345");
  }

  @Test
  public void testSearchTweets30days() {
    List<Tweet>
        result =
        twitterClient.searchForTweetsWithin30days("Hello World!", ConverterHelper.dayBeforeNow(25), ConverterHelper.dayBeforeNow(1), "30days");
    assertTrue(result.size() > 0);
  }

  @Test
  public void testGetFollowersIds() {
    List<String> ids = twitterClient.getFollowersIds("786491");
    assertNotNull(ids);
    assertTrue(ids.size() > 10000);
  }

  @Test
  public void testGetFollowingIds() {
    List<String> ids = twitterClient.getFollowingIds("786491");
    assertNotNull(ids);
    assertTrue(ids.size() > 1000);
  }

  @Test
  public void testGetDmListWithCountAndGetDm() {
    int                 count  = 55;
    List<DirectMessage> result = twitterClient.getDmList(count);
    assertNotNull(result);
    assertEquals(count, result.size());
    assertNotNull(result.get(0).getText());
    DirectMessage dm = twitterClient.getDm(result.get(0).getId());
    assertNotNull(dm);
    assertNotNull(dm.getText());
  }

  @Test
  public void testPostDM() {
    DmEvent result = twitterClient.postDm("Hello world !", "1120050519182016513");
    assertNotNull(result);
    assertNotNull(result.getEvent().getText());
  }

}