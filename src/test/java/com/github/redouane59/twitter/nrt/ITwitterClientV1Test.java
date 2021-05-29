package com.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.RelationType;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.others.RequestToken;
import com.github.redouane59.twitter.dto.tweet.MediaCategory;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.UploadMediaResponse;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
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
  public void testGetRetweetersId() {
    String tweetId = "1078358350000205824";
    assertTrue(twitterClient.getRetweetersId(tweetId).size() > 10);
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
  public void testPostAndRTandDeleteTweet() {
    String text       = "API Test " + LocalDateTime.now() + " #TwitterAPI";
    Tweet  resultPost = twitterClient.postTweet(text);
    assertNotNull(resultPost);
    assertNotNull(resultPost.getId());
    assertEquals(text, resultPost.getText());
    Tweet resultPostAnswer = twitterClient.postTweet(text, resultPost.getId());
    assertNotNull(resultPostAnswer);
    assertNotNull(resultPostAnswer.getId());
    assertEquals(resultPostAnswer.getInReplyToStatusId(), resultPost.getId());
    Tweet resultRT = twitterClient.retweetTweet(resultPost.getId());
    assertNotNull(resultRT);
    assertNotNull(resultRT.getId());
    assertEquals(resultPost.getAuthorId(), resultRT.getAuthorId());
    Tweet resultDelete = twitterClient.deleteTweet(resultPost.getId());
    assertNotNull(resultDelete);
    assertEquals(resultPost.getId(), resultDelete.getId());
    Tweet resultDelete2 = twitterClient.deleteTweet(resultPostAnswer.getId());
    assertNotNull(resultDelete2);
    assertEquals(resultPostAnswer.getId(), resultDelete2.getId());
  }

  @Test
  public void postTweetWithUrl() {
    Tweet resultPost = twitterClient.postTweet("test", null, null, "https://twitter.com/TwitterDev/status/1392465174708187137");
    assertNotNull(resultPost);
    assertNotNull(resultPost.getId());
    Tweet resultDelete = twitterClient.deleteTweet(resultPost.getId());
    assertNotNull(resultDelete);
  }

  @Test
  public void testGetFavorites() {
    int         count     = 750;
    List<Tweet> favorites = twitterClient.getFavorites("1120050519182016513", count);
    assertNotNull(favorites);
    assertTrue(favorites.size() > count);
  }

  @Test
  public void testSearchTweets30days() {
    List<Tweet>
        result =
        twitterClient.searchForTweetsWithin30days("@Twitter -RT", ConverterHelper.dayBeforeNow(25), ConverterHelper.dayBeforeNow(1), "30days");
    assertTrue(result.size() > 0);
  }

  @Test
  public void testUploadMedia() throws Exception {

    try (InputStream is = ITwitterClientV1Test.class.getResourceAsStream("/twitter.png");
         ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
      byte[] buf = new byte[1024];
      int    k;
      while ((k = is.read(buf)) > 0) {
        baos.write(buf, 0, k);
      }
      UploadMediaResponse response = twitterClient.uploadMedia("twitter.png", baos.toByteArray(), MediaCategory.TWEET_IMAGE);
      assertNotNull(response);
      assertNotNull(response.getMediaId());
      Tweet tweet = twitterClient.postTweet("Test", null, response.getMediaId());
      assertNotNull(tweet);
      assertNotNull(tweet.getId());
      twitterClient.deleteTweet(tweet.getId());
    }
  }

  @Test
  public void testAnswerToSeveralUsers() {
    Tweet tweet = twitterClient.postTweet(".", "1396580851274682370");
    assertNotNull(tweet);
    assertNotNull(tweet.getId());
    twitterClient.deleteTweet(tweet.getId());
  }

  @Test
  public void testGetFollowersIds() {
    List<String> ids = twitterClient.getFollowersIds("786491");
    assertNotNull(ids);
    assertTrue(ids.size() > 10000);
  }

}