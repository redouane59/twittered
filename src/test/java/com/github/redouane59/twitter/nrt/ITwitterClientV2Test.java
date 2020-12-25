package com.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponse;
import com.github.redouane59.twitter.dto.tweet.TweetType;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ITwitterClientV2Test {

  private static TwitterClient twitterClient;

  @BeforeAll
  public static void init() throws IOException {
    String credentialPath = "C:/Users/Perso/Documents/GitHub/twitter-credentials.json";
    twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                          .readValue(new File(credentialPath), TwitterCredentials.class));
  }

  @Test
  public void getUserByUserName() {
    String userName = "RedTheOne";
    User   result   = twitterClient.getUserFromUserName(userName);
    assertEquals("92073489", result.getId());
    userName = "RedouaneBali";
    result   = twitterClient.getUserFromUserName(userName);
    assertEquals("RedouaneBali", result.getName());
  }

  @Test
  public void getUsersByUserNames() {
    List<User> result = twitterClient.getUsersFromUserNames(Arrays.asList("Zidane", "Ronaldo", "RedTheOne"));
    assertEquals(3, result.size());
    assertEquals("92073489", result.get(2).getId());
  }

  @Test
  public void getUsersByUserIds() {
    List<User> result = twitterClient.getUsersFromUserIds(Arrays.asList("22848599", "1976143068", "92073489"));
    assertEquals(3, result.size());
    assertEquals("RedTheOne", result.get(2).getName());
  }

  @Test
  public void testGetUserInfoName() {
    String userId = "92073489";
    User   user   = twitterClient.getUserFromUserId(userId);
    assertEquals("RedTheOne", user.getName());
  }

  @Test
  public void testGetUserInfoId() {
    String userId = "92073489";
    User   user   = twitterClient.getUserFromUserId(userId);
    assertEquals(userId, user.getId());
  }

  @Test
  public void testGetUserInfoFavouritesDateOfCreation() {
    String userId = "92073489";
    User   user   = twitterClient.getUserFromUserId(userId);
    assertNotNull(user.getDateOfCreation());
  }

  @Test
  public void testGetUserInfoStatusesCount() {
    String userId = "92073489";
    User   user   = twitterClient.getUserFromUserId(userId);
    assertTrue(user.getTweetCount() > 0);
  }

  @Test
  public void testGetUserWithCache() {
    String userId = "92073489";
    User   user   = twitterClient.getUserFromUserId(userId);
    assertEquals("RedTheOne", user.getName());
    user = twitterClient.getUserFromUserId(userId);
    assertEquals("RedTheOne", user.getName());
  }

  @Test
  public void testGetUsersFromUserIds() {
    List<String> ids = new ArrayList<>();
    ids.add("92073489"); // RedTheOne
    ids.add("22848599"); // Soltana
    List<User> result = twitterClient.getUsersFromUserIds(ids);
    assertEquals("RedTheOne", result.get(0).getName());
    assertEquals("Soltana", result.get(1).getName());
  }


  @Test
  public void testGetTweetById() {
    String tweetId = "1224041905333379073";
    Tweet  tweet   = twitterClient.getTweet(tweetId);
    assertNotNull(tweet);
  }

  @Test
  public void testGetFollowingById() {
    List<User> followings = twitterClient.getFollowing("882266619115864066");
    assertTrue(followings.size() > 200);
  }

  @Test
  public void testGetFollowersById() {
    List<User> followers = twitterClient.getFollowers("882266619115864066");
    assertTrue(followers.size() > 500);
  }

  @Test
  public void testGetTweetsByIds() {
    List<String> tweetIds = Arrays.asList("1294174710624849921,1294380029430960128,1294375095746666496");
    List<Tweet>  tweets   = twitterClient.getTweets(tweetIds);
    assertTrue(tweets.size() > 0);
    assertTrue(tweets.get(0).getText().length() > 0);
    assertTrue(tweets.get(1).getText().length() > 0);
    assertTrue(tweets.get(2).getText().length() > 0);
  }

  @Test
  public void testSearchTweets7days() {
    List<Tweet> result = twitterClient.searchForTweetsWithin7days("@RedTheOne -RT");
    assertTrue(result.size() > 10);
    Tweet tweet = result.get(0);
    assertNotNull(tweet.getId());
    assertNotNull(tweet.getText());
    assertNotNull(tweet.getCreatedAt());
    assertNotNull(tweet.getAuthorId());
    assertTrue(tweet.getRetweetCount() >= 0);
    assertTrue(tweet.getReplyCount() >= 0);
    assertTrue(tweet.getLikeCount() >= 0);
    assertNotNull(tweet.getLang());
  }

  @Test
  public void testSearchTweets7daysWithNexTokenAndCount() {
    TweetSearchResponse result = twitterClient.searchForTweetsWithin7days("@RedTheOne -RT", null, null, 100, null);
    assertEquals(100, result.getTweets().size());
    assertNotNull(result.getNextToken());
    TweetSearchResponse result2 = twitterClient.searchForTweetsWithin7days("@RedTheOne -RT", null, null, 100, result.getNextToken());
    assertTrue(result2.getTweets().size() > 0);
    assertNotEquals(result.getTweets().get(0).getId(), result2.getTweets().get(0).getId());
    assertNotNull(result2.getNextToken());
  }

  @Test
  public void testSearchTweetsFullArchiveWithNexTokenAndCount() {
    TweetSearchResponse
        result =
        twitterClient.searchForTweetsFullArchive("@TwitterSupport", ConverterHelper.dayBeforeNow(150), ConverterHelper.dayBeforeNow(1), 100, null);
    assertTrue(result.getTweets().size() > 10);
    assertNotNull(result.getNextToken());
  }

  @Test
  public void testGetTweetType() {
    assertEquals(TweetType.QUOTED, twitterClient.getTweet("1267115291991068673").getTweetType());
    assertEquals(TweetType.REPLIED_TO, twitterClient.getTweet("1267132388632604673").getTweetType());
    assertEquals(TweetType.DEFAULT, twitterClient.getTweet("1267010053040672768").getTweetType());
  }

  @Test
  public void testGetTweetIdWithTwoTypes() {
    assertEquals("1264255917043920904", twitterClient.getTweet("1264256827690270722").getInReplyToStatusId(TweetType.RETWEETED));
    assertEquals("1263783602485157889", twitterClient.getTweet("1264256827690270722").getInReplyToStatusId(TweetType.QUOTED));
  }

  @Test
  public void testHideAndUnideReply() {
    String  tweetId = "1298226351653056514";
    boolean reply   = twitterClient.hideReply(tweetId, true);
    assertTrue(reply);
    reply = twitterClient.hideReply(tweetId, false);
    assertFalse(reply);
  }

  @Test
  public void testGetFilteredStreamRules() {
    List<StreamRule> result = twitterClient.retrieveFilteredStreamRules();
    assertTrue(result.size() > 0);
  }

  @Test
  public void testAddAndDeleteFilteredStreamRules() {
    String     ruleName = "test_rule";
    StreamRule result   = twitterClient.addFilteredStreamRule(ruleName, "1");
    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals("test_rule", result.getValue());
    assertEquals("1", result.getTag());
    StreamMeta streamMeta = twitterClient.deleteFilteredStreamRule(ruleName);
    assertNotNull(streamMeta);
  }

  @Test
  public void testGetBearerToken() {
    String token = twitterClient.getBearerToken();
    assertNotNull(token);
    assertTrue(token.length() > 50);
  }

  @Test
  public void testGetUserTimeline() {
    List<Tweet> result = twitterClient.getUserTimeline("1120050519182016513", 150);
    assertEquals(result.size(), 150);
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

  @Test
  public void testGetUserTimelineWithDates() {
    List<Tweet> result = twitterClient.getUserTimeline("1120050519182016513", 20,
                                                       ConverterHelper.dayBeforeNow(5),
                                                       ConverterHelper.dayBeforeNow(1),
                                                       null, null);
    assertEquals(result.size(), 20);
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

  @Test
  public void testGetUserTimelineWithIds() {
    List<Tweet> result = twitterClient.getUserTimeline("1307302673318895621", 10,
                                                       null, null,
                                                       "1339662509201121280",
                                                       "1339667017109032966");
    assertEquals(2, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

  @Test
  public void testGetUserMentions() {
    List<Tweet> result = twitterClient.getUserMentions("1120050519182016513", 150);
    assertEquals(150, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

  @Test
  public void testGetUserMentionsWithDates() {
    List<Tweet> result = twitterClient.getUserTimeline("1120050519182016513", 5,
                                                       ConverterHelper.dayBeforeNow(20),
                                                       ConverterHelper.dayBeforeNow(1),
                                                       null, null);
    assertEquals(5, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

  @Test
  public void testGetUserMentionsWithIds() {
    List<Tweet> result = twitterClient.getUserTimeline("1307302673318895621", 10,
                                                       null, null,
                                                       "1339659629228384256",
                                                       "1339993046377766912");
    assertEquals(5, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

}