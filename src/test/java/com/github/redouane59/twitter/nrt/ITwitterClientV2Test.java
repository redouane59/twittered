package com.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetListV2;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponse;
import com.github.redouane59.twitter.dto.tweet.TweetType;
import com.github.redouane59.twitter.dto.tweet.TweetV2;
import com.github.redouane59.twitter.dto.tweet.TweetsCountsList;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserListV2;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.scribejava.core.model.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ITwitterClientV2Test {

  private static TwitterClient twitterClient;
  private        String        userId = "1120050519182016513";


  @BeforeAll
  public static void init() {
    twitterClient = new TwitterClient();
  }

  @Test
  public void getUserByUserName() {
    String userName = "RedouaneBali";
    User   result   = twitterClient.getUserFromUserName(userName);
    assertEquals(userName, result.getName());
  }

  @Test
  public void getUsersByUserNames() {
    List<User> result = twitterClient.getUsersFromUserNames(Arrays.asList("Zidane", "Ronaldo", "RedouaneBali"));
    assertEquals(3, result.size());
    assertEquals(userId, result.get(2).getId());
  }

  @Test
  public void getUsersByUserIds() {
    List<User> result = twitterClient.getUsersFromUserIds(Arrays.asList("22848599", "1976143068", userId));
    assertEquals(3, result.size());
    assertEquals("RedouaneBali", result.get(2).getName());
  }

  @Test
  public void testGetUserInfoName() {
    User user = twitterClient.getUserFromUserId(userId);
    assertEquals("RedouaneBali", user.getName());
  }

  @Test
  public void testGetUserInfoId() {
    User user = twitterClient.getUserFromUserId(userId);
    assertEquals(userId, user.getId());
  }

  @Test
  public void testGetUserInfoFavouritesDateOfCreation() {
    User user = twitterClient.getUserFromUserId(userId);
    assertNotNull(user.getDateOfCreation());
  }

  @Test
  public void testGetUserInfoStatusesCount() {
    User user = twitterClient.getUserFromUserId(userId);
    assertTrue(user.getTweetCount() > 0);
  }

  @Test
  public void testGetUserWithCache() {
    User user = twitterClient.getUserFromUserId(userId);
    assertEquals("RedouaneBali", user.getName());
    user = twitterClient.getUserFromUserId(userId);
    assertEquals("RedouaneBali", user.getName());
  }

  @Test
  public void testGetUsersFromUserIds() {
    List<String> ids = new ArrayList<>();
    ids.add(userId);
    ids.add("22848599"); // Soltana
    List<User> result = twitterClient.getUsersFromUserIds(ids);
    assertEquals("RedouaneBali", result.get(0).getName());
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
    List<Tweet> result = twitterClient.searchForTweetsWithin7days("@lequipe bonjour -RT");
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
    TweetSearchResponse result = twitterClient.searchForTweetsWithin7days("@lequipe -RT", null, null, 100, null);
    assertEquals(100, result.getTweets().size());
    assertNotNull(result.getNextToken());
    TweetSearchResponse result2 = twitterClient.searchForTweetsWithin7days("@lequipe -RT", null, null, 100,
                                                                           result.getNextToken());
    assertTrue(result2.getTweets().size() > 0);
    assertNotEquals(result.getTweets().get(0).getId(), result2.getTweets().get(0).getId());
    assertNotNull(result2.getNextToken());
  }

  @Test
  public void testSearchTweetsFullArchiveWithCount() {
    TweetSearchResponse result = twitterClient.searchForTweetsFullArchive("Twitter",
                                                                          ConverterHelper.dayBeforeNow(2),
                                                                          ConverterHelper.dayBeforeNow(1),
                                                                          10,
                                                                          null);
    assertEquals(10, result.getTweets().size());
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
    assertEquals("1264255917043920904",
                 twitterClient.getTweet("1264256827690270722").getInReplyToStatusId(TweetType.RETWEETED));
    assertEquals("1263783602485157889",
                 twitterClient.getTweet("1264255917043920904").getInReplyToStatusId(TweetType.QUOTED));
  }

  @Test
  public void testAddAndDeleteAndGetFilteredStreamRules() {
    String     ruleName = "test_rule";
    StreamRule result   = twitterClient.addFilteredStreamRule(ruleName, "1");
    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals("test_rule", result.getValue());
    assertEquals("1", result.getTag());
    List<StreamRule> rules = twitterClient.retrieveFilteredStreamRules();
    assertTrue(rules.size() > 0);
    StreamMeta streamMeta = twitterClient.deleteFilteredStreamRule(ruleName);
    assertNotNull(streamMeta);
  }

  @Test
  public void testStartStream() throws InterruptedException, ExecutionException {
    Future<Response> future = twitterClient.startFilteredStream(System.out::println);
    try {
      future.get(5, TimeUnit.SECONDS);
    } catch (TimeoutException exc) {
      // It's OK
    }
    ;

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
  public void testGetUserTimelineWithDatesThenWithIds() {
    List<Tweet> result = twitterClient.getUserTimeline(this.userId, 10, ConverterHelper.dayBeforeNow(30),
                                                       ConverterHelper.dayBeforeNow(1), null, null);
    assertEquals(10, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());

    result = twitterClient.getUserTimeline(this.userId, 5, null, null, result.get(6).getId(),
                                           result.get(0).getId());
    assertEquals(5, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

  @Test
  public void testGetUserMentions() {
    List<Tweet> result = twitterClient.getUserMentions("1307302673318895621", 150);
    assertEquals(150, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

  @Test
  public void testGetUserMentionsWithDatesThenWithIds() {
    List<Tweet> result = twitterClient.getUserMentions("1307302673318895621", 10, ConverterHelper.dayBeforeNow(30),
                                                       ConverterHelper.dayBeforeNow(1), null, null);
    assertEquals(10, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());

    result = twitterClient.getUserMentions("1307302673318895621", 5, null, null, result.get(6).getId(),
                                           result.get(0).getId());
    assertEquals(5, result.size());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
  }

  @Test
  public void testGetTweetByIdWithExpansions() {
    String  tweetId = "1361010662714007557";
    TweetV2 tweet   = (TweetV2) twitterClient.getTweet(tweetId);
    assertNotNull(tweet);
    assertEquals(3, tweet.getIncludes().getUsers().length);
    assertEquals("RedouaneBali", tweet.getIncludes().getUsers()[0].getName());
    assertEquals("TwitterDev", tweet.getIncludes().getUsers()[1].getName());
    assertEquals("jessicagarson", tweet.getIncludes().getUsers()[2].getName());
    assertEquals(1, tweet.getIncludes().getTweets().length);
    assertEquals("2244994945", tweet.getIncludes().getTweets()[0].getAuthorId());
    assertEquals("1341761599976181763", tweet.getIncludes().getTweets()[0].getId());
    assertNotNull(tweet.getIncludes().getTweets()[0].getEntities());
  }

  @Test
  public void testGetLikingUsers() {
    UserListV2 result = twitterClient.getLikingUsers("1395447825366847488");
    assertTrue(result.getData().size() > 50);
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getName());
    assertNotNull(result.getData().get(0).getCreatedAt());
  }

  @Test
  public void testGetLikedTweets() {
    TweetListV2 result = twitterClient.getLikedTweets("1120050519182016513");
    assertTrue(result.getData().size() > 0);
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());
    assertNotNull(result.getData().get(0).getCreatedAt());
  }

  @Test
  public void testGetTweetCount() {
    TweetsCountsList result = twitterClient.getTweetsCounts("@Twitter");
    assertTrue(result.getData().size() > 0);
    assertTrue(result.getData().get(0).getTweetCount() > 0);
  }

}