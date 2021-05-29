package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.redouane59.twitter.dto.tweet.MediaCategory;
import com.github.redouane59.twitter.helpers.URLHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UrlHelperTest {

  private URLHelper urlHelper = new URLHelper();

  @Test
  public void testUrlRetweetrs() {
    assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?id=12345&count=100", urlHelper.getRetweetersUrl("12345"));
  }

  @Test
  public void testUrlFollowersById() {
    assertEquals(
        "https://api.twitter.com/2/users/12345/followers?max_results=1000&user.fields=id,created_at,entities,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected",
        urlHelper.getFollowersUrl("12345"));
  }

  @Test
  public void testUrlFollowingsById() {
    assertEquals(
        "https://api.twitter.com/2/users/12345/following?max_results=1000&user.fields=id,created_at,entities,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected",
        urlHelper.getFollowingUrl("12345"));
  }

  @Test
  public void testUrlFollowersIs() {
    assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=12345&count=5000",
                 urlHelper.getFollowersIdsUrl("12345"));
  }

  @Test
  public void testUrlLastTweet() {
    assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?",
                 URLHelper.LAST_TWEET_LIST_URL);
  }

  @Test
  public void testUrlFriendshipById() {
    assertEquals("https://api.twitter.com/1.1/friendships/show.json?source_id=12345&target_id=67890",
                 urlHelper.getFriendshipUrl("12345", "67890"));
  }

  @Test
  public void testUrlFollowById() {
    assertEquals("https://api.twitter.com/2/users/12345/following",
                 urlHelper.getFollowUrl("12345"));
  }

  @Test
  public void testUrlGetUserByIdV2() {
    assertEquals(
        "https://api.twitter.com/2/users/12345?expansions=pinned_tweet_id&user.fields=id,created_at,entities,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected",
        urlHelper.getUserUrl("12345"));
  }

  @Test
  public void testUrlGetUsersByNames() {
    List<String> names = new ArrayList<>();
    names.add("RedTheOne");
    names.add("Ronaldo");
    names.add("Zidane");
    assertEquals("https://api.twitter.com/2/users/by?usernames=RedTheOne,Ronaldo,Zidane",
                 urlHelper.getUsersUrlbyNames(names));
  }

  @Test
  public void testUrlGetUsersByIds() {
    List<String> ids = new ArrayList<>();
    ids.add("12345");
    ids.add("23456");
    ids.add("34567");
    assertEquals("https://api.twitter.com/2/users?ids=12345,23456,34567",
                 urlHelper.getUsersUrlbyIds(ids));
  }

  @Test
  public void testUrlGetRateLimitStatus() {
    assertEquals("https://api.twitter.com/1.1/application/rate_limit_status.json",
                 URLHelper.RATE_LIMIT_URL);
  }

  @Test
  public void testSearchTweetsUrlStandard() {
    https:
//api.twitter.com/1.1/search/tweets.json
    assertEquals("https://api.twitter.com/1.1/search/tweets.json",
                 URLHelper.SEARCH_TWEET_STANDARD_URL);
  }

  @Test
  public void testUnfollowByUserId() {
    assertEquals("https://api.twitter.com/2/users/12345/following/67890",
                 urlHelper.getUnfollowUrl("12345", "67890"));
  }

  @Test
  public void testLikeUrl() {
    String userId = "12345";
    assertEquals("https://api.twitter.com/2/users/" + userId + "/likes",
                 urlHelper.getLikeUrl(userId));
  }

  @Test
  public void testUnlikeUrl() {
    String userId  = "12345";
    String tweetId = "00000";
    assertEquals("https://api.twitter.com/2/users/" + userId + "/likes/" + tweetId,
                 urlHelper.getUnlikeUrl(userId, tweetId));
  }

  @Test
  public void testRetweetTweetUrl() {
    String tweetId = "12345";
    assertEquals("https://api.twitter.com/1.1/statuses/retweet/12345.json", urlHelper.getRetweetTweetUrl(tweetId));
  }

  @Test
  public void testTweetUrl() {
    assertEquals(
        "https://api.twitter.com/2/tweets/12345?expansions=author_id,entities.mentions.username,in_reply_to_user_id,referenced_tweets.id,referenced_tweets.id.author_id&"
        + URLHelper.TWEET_FIELDS
        + URLHelper.ALL_TWEET_FIELDS
        + "&"
        + URLHelper.USER_FIELDS
        + URLHelper.ALL_USER_FIELDS,
        urlHelper.getTweetUrl("12345"));
  }

  @Test
  public void testUrlGetTweetsV2() {
    assertEquals(
        "https://api.twitter.com/2/tweets?ids=1294174710624849921,1294380029430960128,1294375095746666496&"
        + URLHelper.TWEET_FIELDS + URLHelper.ALL_TWEET_FIELDS
        + "&"
        + URLHelper.USER_FIELDS + URLHelper.ALL_USER_FIELDS,
        urlHelper.getTweetListUrl(Arrays.asList("1294174710624849921,1294380029430960128,1294375095746666496")));
  }

  @Test
  public void testGetUserUrlFromName() {
    assertEquals(
        "https://api.twitter.com/2/users/by/username/RedTheOne?expansions=pinned_tweet_id&user.fields=id,created_at,entities,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected"
        ,
        urlHelper.getUserUrlFromName("RedTheOne"));
  }

  @Test
  public void testSearch7DaysUrl() {
    assertEquals("https://api.twitter.com/2/tweets/search/recent",
                 URLHelper.SEARCH_TWEET_7_DAYS_URL);
  }

  @Test
  public void testSearchFullArchiveUrl() {
    assertEquals(
        "https://api.twitter.com/2/tweets/search/all?expansions=author_id,entities.mentions.username,in_reply_to_user_id,referenced_tweets.id,referenced_tweets.id.author_id",
        URLHelper.SEARCH_TWEET_FULL_ARCHIVE_URL);
  }

  @Test
  public void testGetBearerTokenUrl() {
    assertEquals(
        "https://api.twitter.com/oauth2/token",
        URLHelper.GET_BEARER_TOKEN_URL);
  }

  @Test
  public void testPostNewTweetUrl() {
    assertEquals("https://api.twitter.com/1.1/statuses/update.json", urlHelper.getPostTweetUrl());
  }

  @Test
  public void testDeleteTweetUrl() {
    assertEquals("https://api.twitter.com/1.1/statuses/destroy/240854986559455234.json", urlHelper.getDeleteTweetUrl("240854986559455234"));
  }

  @Test
  public void testHideReplyUrl() {
    assertEquals("https://api.twitter.com/2/tweets/1298226351653056514/hidden", urlHelper.getHideReplyUrl("1298226351653056514"));
  }

  @Test
  public void testRetrieveFilteredStreamRulesUrl() {
    assertEquals("https://api.twitter.com/2/tweets/search/stream/rules", urlHelper.getFilteredStreamRulesUrl());
  }

  @Test
  public void testFilteredStreamUrl() {
    assertEquals(
        "https://api.twitter.com/2/tweets/search/stream?"
        + URLHelper.EXPANSION
        + URLHelper.ALL_EXPANSIONS
        + "&"
        + URLHelper.TWEET_FIELDS
        + URLHelper.ALL_TWEET_FIELDS
        + "&"
        + URLHelper.USER_FIELDS
        + URLHelper.ALL_USER_FIELDS,
        urlHelper.getFilteredStreamUrl());
  }

  @Test
  public void testSampledStreamUrl() {
    assertEquals(
        "https://api.twitter.com/2/tweets/sample/stream?"
        + URLHelper.EXPANSION
        + URLHelper.ALL_EXPANSIONS
        + "&"
        + URLHelper.TWEET_FIELDS
        + URLHelper.ALL_TWEET_FIELDS
        + "&"
        + URLHelper.USER_FIELDS
        + URLHelper.ALL_USER_FIELDS,
        urlHelper.getSampledStreamUrl());
  }

  @Test
  public void testSearchFullArchiveUrlv1() {
    assertEquals("https://api.twitter.com/1.1/tweets/search/fullarchive/dev.json", urlHelper.getSearchTweetFullArchiveUrl("dev"));
  }

  @Test
  public void testSearch30daysUrl() {
    assertEquals("https://api.twitter.com/1.1/tweets/search/30day/dev.json", urlHelper.getSearchTweet30DaysUrl("dev"));
  }

  @Test
  public void testLiveEventUrl() {
    assertEquals("https://api.twitter.com/1.1/account_activity/all/dev/webhooks.json",
                 urlHelper.getLiveEventUrl("dev"));
  }

  @Test
  public void testGetUserTimelineUrl() {
    assertEquals("https://api.twitter.com/2/users/99999/tweets?max_results=200&" + URLHelper.TWEET_FIELDS + URLHelper.ALL_TWEET_FIELDS,
                 urlHelper.getUserTimelineUrl("99999", 200, null, null, null, null));
  }

  @Test
  public void testGetUserTimelineUrlWithDates() {
    assertEquals(
        "https://api.twitter.com/2/users/99999/tweets?max_results=100&start_time=2020-01-01T00:00:00.000Z&end_time=2020-02-01T00:00:00.000Z&"
        + URLHelper.TWEET_FIELDS + URLHelper.ALL_TWEET_FIELDS,
        urlHelper.getUserTimelineUrl("99999", 100, LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.of(2020, 2, 1, 0, 0), null, null));
  }

  @Test
  public void testGetUserMentionsUrl() {
    assertEquals("https://api.twitter.com/2/users/99999/mentions?max_results=200&" + URLHelper.TWEET_FIELDS + URLHelper.ALL_TWEET_FIELDS,
                 urlHelper.getUserMentionsUrl("99999", 200, null, null, null, null));
  }

  @Test
  public void testGetUserMentionsUrlWithDates() {
    assertEquals(
        "https://api.twitter.com/2/users/99999/mentions?max_results=100&start_time=2020-01-01T00:00:00.000Z&end_time=2020-02-01T00:00:00.000Z&"
        + URLHelper.TWEET_FIELDS + URLHelper.ALL_TWEET_FIELDS,
        urlHelper.getUserMentionsUrl("99999", 100, LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.of(2020, 2, 1, 0, 0), null, null));
  }

  @Test
  public void testUploadMediaUrl() {
    assertEquals("https://upload.twitter.com/1.1/media/upload.json?media_category=tweet_image",
                 urlHelper.getUploadMediaUrl(MediaCategory.TWEET_IMAGE));
  }

  @Test
  public void testGetFavouritesUrl() {
    assertEquals("https://api.twitter.com/1.1/favorites/list.json?count=200&user_id=12345&max_id=9999&tweet_mode=extended",
                 urlHelper.getFavoriteTweetsUrl("12345", "9999"));
  }

  @Test
  public void testGetFavouritesUrlWithoutMaxId() {
    assertEquals("https://api.twitter.com/1.1/favorites/list.json?count=200&user_id=12345&tweet_mode=extended",
                 urlHelper.getFavoriteTweetsUrl("12345", null));
  }

  @Test
  public void testGetBlockUrl() {
    String userId = "12345";
    assertEquals("https://api.twitter.com/2/users/" + userId + "/blocking",
                 urlHelper.getBlockUserUrl(userId));
  }

  @Test
  public void testGetUnblockUrl() {
    String sourceUserId = "12345";
    String targetUserId = "67890";
    assertEquals("https://api.twitter.com/2/users/" + sourceUserId + "/blocking/" + targetUserId,
                 urlHelper.getUnblockUserUrl(sourceUserId, targetUserId));
  }

  @Test
  public void testGetBlockingUsersUrl() {
    String userId = "12345";
    assertEquals("https://api.twitter.com/2/users/" + userId + "/blocking?" + URLHelper.USER_FIELDS + URLHelper.ALL_USER_FIELDS,
                 urlHelper.getBlockingUsersUrl(userId));
  }

  @Test
  public void testLikingUsers() {
    assertEquals(
        "https://api.twitter.com/2/tweets/1354143047324299264/liking_users?user.fields=id,created_at,entities,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected",
        urlHelper.getLikingUsersUrl("1354143047324299264"));
  }

  @Test
  public void testLikedTweetsUrl() {
    String userId = "12345";
    assertEquals("https://api.twitter.com/2/users/" + userId + "/liked_tweets?" + URLHelper.TWEET_FIELDS + URLHelper.ALL_TWEET_FIELDS,
                 urlHelper.getLikedTweetsUrl(userId));
  }
}
