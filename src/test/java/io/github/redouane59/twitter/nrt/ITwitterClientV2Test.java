package io.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.scribejava.core.model.Response;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetCountsList;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.tweet.TweetType;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserList;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Assertions;
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
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getName());
    assertNotNull(result.get(0).getDisplayedName());
    assertNotNull(result.get(0).getDateOfCreation());
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
    ids.add("22848599");
    List<User> result = twitterClient.getUsersFromUserIds(ids);
    assertEquals("RedouaneBali", result.get(0).getName());
    assertEquals("Soltana", result.get(1).getName());
    assertNotNull(result.get(0).getDisplayedName());
    assertNotNull(result.get(0).getDateOfCreation());
  }

  @Test
  public void testGetTweetById() {
    String tweetId = "1224041905333379073";
    Tweet  tweet   = twitterClient.getTweet(tweetId);
    assertNotNull(tweet);
  }

  @Test
  public void testGetFollowing() {
    UserList result = twitterClient.getFollowing(userId);
    assertTrue(result.getData().size() > 1000);
    assertTrue(result.getMeta().getResultCount() > 0);
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getName());
    assertNotNull(result.getData().get(0).getCreatedAt());
    assertNotNull(result.getData().get(0).getProfileImageUrl());
  }

  @Test
  public void testGetFollowingWithParameters() {
    UserList result = twitterClient.getFollowing("882266619115864066", AdditionalParameters.builder().recursiveCall(false).maxResults(150).build());
    assertEquals(150, result.getData().size());
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getMeta().getNextToken());
    UserList result2 = twitterClient.getFollowing("882266619115864066",
                                                  AdditionalParameters.builder()
                                                                      .recursiveCall(false)
                                                                      .maxResults(10)
                                                                      .paginationToken(result.getMeta().getNextToken())
                                                                      .build());
    assertEquals(10, result2.getData().size());
    assertNotNull(result2.getData().get(0).getId());
    assertNotEquals(result.getData().get(0).getId(), result2.getData().get(0).getId());
    assertNotNull(result2.getMeta().getNextToken());
  }

  @Test
  public void
  testGetFollowersWithParameters() {
    UserList result = twitterClient.getFollowers(userId, AdditionalParameters.builder().recursiveCall(false).maxResults(150).build());
    assertEquals(150, result.getData().size());
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getMeta().getNextToken());
    UserList result2 = twitterClient.getFollowers(userId,
                                                  AdditionalParameters.builder()
                                                                      .recursiveCall(false)
                                                                      .maxResults(10)
                                                                      .paginationToken(result.getMeta().getNextToken())
                                                                      .build());
    assertEquals(10, result2.getData().size());
    assertNotNull(result2.getData().get(0).getId());
    assertNotEquals(result.getData().get(0).getId(), result2.getData().get(0).getId());
    assertNotNull(result2.getMeta().getNextToken());
  }

  @Test
  public void testGetFollowers() {
    UserList result = twitterClient.getFollowers(userId);
    assertTrue(result.getData().size() > 1000);
    assertTrue(result.getMeta().getResultCount() > 0);
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getName());
    assertNotNull(result.getData().get(0).getCreatedAt());
    assertNotNull(result.getData().get(0).getProfileImageUrl());
  }

  @Test
  public void testGetTweetsByIds() {
    List<String> tweetIds = Arrays.asList("1294174710624849921,1294380029430960128,1294375095746666496");
    TweetList    tweets   = twitterClient.getTweets(tweetIds);
    assertTrue(tweets.getData().size() > 0);
    assertTrue(tweets.getData().get(0).getText().length() > 0);
    assertTrue(tweets.getData().get(1).getText().length() > 0);
    assertTrue(tweets.getData().get(2).getText().length() > 0);
  }

  @Test
  public void testRecentSearch() {
    TweetList result = twitterClient.searchTweets("@lequipe bonjour -RT");
    assertTrue(result.getData().size() > 0);
    Tweet tweet = result.getData().get(0);
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
  public void testRecentSearchWithParameters() {
    TweetList result = twitterClient.searchTweets("@lequipe bonjour",
                                                  AdditionalParameters.builder().recursiveCall(false).maxResults(10).build());
    assertEquals(10, result.getData().size());
    Tweet tweet = result.getData().get(0);
    assertNotNull(tweet.getId());
    assertNotNull(tweet.getText());
    assertNotNull(tweet.getCreatedAt());
    assertNotNull(tweet.getAuthorId());
    assertTrue(tweet.getRetweetCount() >= 0);
    assertTrue(tweet.getReplyCount() >= 0);
    assertTrue(tweet.getLikeCount() >= 0);
    assertNotNull(tweet.getLang());
  }

  public void testAllTweetsSearch() {
    TweetList result = twitterClient.searchAllTweets("@lequipe bonjour -RT", AdditionalParameters.builder()
                                                                                                 .recursiveCall(false).build());
    assertTrue(result.getData().size() > 0);
    Tweet tweet = result.getData().get(0);
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
  public void testAllTweetsSearchWithParams() {
    TweetList result = twitterClient.searchAllTweets("@lequipe bonjour -RT", AdditionalParameters.builder()
                                                                                                 .startTime(ConverterHelper.dayBeforeNow(50))
                                                                                                 .endTime(ConverterHelper.dayBeforeNow(45))
                                                                                                 .recursiveCall(false)
                                                                                                 .build());
    assertTrue(result.getData().size() > 0);
    assertNotNull(result.getData().get(0).getId());
    result = twitterClient.searchAllTweets("@lequipe bonjour -RT", AdditionalParameters.builder()
                                                                                       .sinceId(result.getData().get(6).getId())
                                                                                       .untilId(result.getData().get(0).getId())
                                                                                       .recursiveCall(false)
                                                                                       .build());
    assertTrue(result.getData().size() > 0);
    assertNotNull(result.getData().get(0).getId());
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
  public void testGetUserTimelineRecursively() {
    TweetList result = twitterClient.getUserTimeline("1120050519182016513", AdditionalParameters.builder().build());
    assertTrue(result.getData().size() > 200);
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());
    assertNotNull(result.getData().get(0).getCreatedAt());
    assertNotNull(result.getData().get(0).getAuthorId());
    assertNotNull(result.getData().get(0).getConversationId());
    assertNotNull(result.getData().get(0).getLang());
    assertNotNull(result.getMeta().getNewestId());
    assertNotNull(result.getMeta().getOldestId());
    assertEquals(result.getData().size(), result.getMeta().getResultCount());
  }

  @Test
  public void testGetUserTimelineWithDatesThenWithIds() {
    TweetList result = twitterClient.getUserTimeline(this.userId, AdditionalParameters.builder()
                                                                                      .startTime(ConverterHelper.dayBeforeNow(30))
                                                                                      .endTime(ConverterHelper.dayBeforeNow(1))
                                                                                      .recursiveCall(false)
                                                                                      .maxResults(10).build());
    assertEquals(10, result.getData().size());
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());

    result = twitterClient.getUserTimeline(this.userId, AdditionalParameters.builder()
                                                                            .maxResults(5)
                                                                            .sinceId(result.getData().get(6).getId())
                                                                            .untilId(result.getData().get(0).getId())
                                                                            .recursiveCall(false)
                                                                            .build());
    assertEquals(5, result.getData().size());
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());
  }

  @Test
  public void testGetUserMentionsRecursively() {
    TweetList result = twitterClient.getUserMentions("1307302673318895621", AdditionalParameters.builder().build());
    assertTrue(result.getData().size() > 200);
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());
    assertNotNull(result.getData().get(0).getCreatedAt());
    assertNotNull(result.getData().get(0).getAuthorId());
    assertNotNull(result.getData().get(0).getConversationId());
    assertNotNull(result.getData().get(0).getLang());
    assertNotNull(result.getMeta().getNewestId());
    assertNotNull(result.getMeta().getOldestId());
    assertEquals(result.getData().size(), result.getMeta().getResultCount());
  }

  @Test
  public void testGetUserMentionsWithDatesThenWithIds() {
    TweetList result = twitterClient.getUserMentions("1307302673318895621",
                                                     AdditionalParameters.builder()
                                                                         .maxResults(10)
                                                                         .startTime(ConverterHelper.dayBeforeNow(30))
                                                                         .endTime(ConverterHelper.dayBeforeNow(1))
                                                                         .recursiveCall(false)
                                                                         .build());
    assertEquals(10, result.getData().size());
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());

    result = twitterClient.getUserMentions("1307302673318895621",
                                           AdditionalParameters.builder()
                                                               .maxResults(5)
                                                               .sinceId(result.getData().get(6).getId())
                                                               .untilId(result.getData().get(0).getId())
                                                               .recursiveCall(false)
                                                               .build());
    assertEquals(5, result.getData().size());
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());
  }

  @Test
  public void testGetTweetByIdWithExpansions() {
    String  tweetId = "1361010662714007557";
    TweetV2 tweet   = (TweetV2) twitterClient.getTweet(tweetId);
    assertNotNull(tweet);
    assertEquals(3, tweet.getIncludes().getUsers().length);
    Assertions.assertEquals("RedouaneBali", tweet.getIncludes().getUsers()[0].getName());
    Assertions.assertEquals("TwitterDev", tweet.getIncludes().getUsers()[1].getName());
    Assertions.assertEquals("jessicagarson", tweet.getIncludes().getUsers()[2].getName());
    assertEquals(1, tweet.getIncludes().getTweets().length);
    assertEquals("2244994945", tweet.getIncludes().getTweets()[0].getAuthorId());
    assertEquals("1341761599976181763", tweet.getIncludes().getTweets()[0].getId());
    assertNotNull(tweet.getIncludes().getTweets()[0].getEntities());
  }

  @Test
  public void testGetLikingUsers() {
    UserList result = twitterClient.getLikingUsers("1395447825366847488");
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getName());
    assertNotNull(result.getData().get(0).getDisplayedName());
    assertNotNull(result.getData().get(0).getProfileImageUrl());
    assertNotNull(result.getData().get(0).getCreatedAt());
    assertTrue(result.getData().size() > 40);
  }

  @Test
  public void testGetLikedTweets() {
    TweetList result = twitterClient.getLikedTweets(userId);
    assertTrue(result.getData().size() > 0);
    assertTrue(result.getMeta().getResultCount() > 0);
    assertNull(result.getMeta().getNextToken());
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());
    assertNotNull(result.getData().get(0).getCreatedAt());
  }

  @Test
  public void testGetLikedTweetsWithParameters() {
    TweetList result = twitterClient.getLikedTweets(userId, AdditionalParameters.builder()
                                                                                .recursiveCall(false).maxResults(20).build());
    assertTrue(result.getData().size() > 0);
    assertTrue(result.getMeta().getResultCount() > 0);
    assertNotNull(result.getMeta().getNextToken());
    assertNotNull(result.getData().get(0).getId());
    assertNotNull(result.getData().get(0).getText());
    assertNotNull(result.getData().get(0).getCreatedAt());
    assertNotNull(result.getMeta().getNextToken());
  }

  @Test
  public void testGetTweetCount() {
    TweetCountsList result = twitterClient.getTweetCounts("@Twitter");
    assertTrue(result.getData().size() > 0);
    assertTrue(result.getData().get(0).getTweetCount() > 0);
  }

  @Test
  public void testGetTweetCountsWithParams() {
    TweetCountsList
        result =
        twitterClient.getTweetCounts("@Twitter", AdditionalParameters.builder()
                                                                     .startTime(ConverterHelper.dayBeforeNow(5))
                                                                     .endTime(ConverterHelper.dayBeforeNow(4))
                                                                     .build());
    assertTrue(result.getData().size() > 0);
    assertTrue(result.getData().get(0).getTweetCount() > 0);
  }

  @Test
  public void testGetAllTweetCountWithParams() {
    TweetCountsList
        result =
        twitterClient.getAllTweetCounts("@Twitter", AdditionalParameters.builder()
                                                                        .startTime(ConverterHelper.dayBeforeNow(1000))
                                                                        .endTime(ConverterHelper.dayBeforeNow(30))
                                                                        .build());
    assertTrue(result.getData().size() > 0);
    assertTrue(result.getData().get(0).getTweetCount() > 0);
    assertNotNull(result.getMeta().getNextToken());
    assertTrue(result.getMeta().getTotalTweetCount() > 0);
  }

}