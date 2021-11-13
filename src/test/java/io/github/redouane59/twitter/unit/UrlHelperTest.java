package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.redouane59.twitter.dto.tweet.MediaCategory;
import io.github.redouane59.twitter.helpers.URLHelper;
import org.junit.jupiter.api.Test;

public class UrlHelperTest {

  private URLHelper urlHelper = new URLHelper();

  @Test
  public void testUrlRetweetrs() {
    assertEquals("https://api.twitter.com/2/tweets/12345/retweeted_by", urlHelper.getRetweetersUrl("12345"));
  }

  @Test
  public void testUrlFollowersById() {
    assertEquals(
        "https://api.twitter.com/2/users/12345/followers",
        urlHelper.getFollowersUrl("12345"));
  }

  @Test
  public void testUrlFollowingsById() {
    assertEquals(
        "https://api.twitter.com/2/users/12345/following", urlHelper.getFollowingUrl("12345"));
  }

  @Test
  public void testUrlFollowersIs() {
    assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=12345&count=5000",
                 urlHelper.getFollowersIdsUrl("12345"));
  }

  @Test
  public void testUrlFollowingIs() {
    assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=12345&count=5000",
                 urlHelper.getFollowingIdsUrl("12345"));
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
    assertEquals("https://api.twitter.com/2/users/12345", urlHelper.getUserUrl("12345"));
  }

  @Test
  public void testUrlGetUsersBy() {
    assertEquals("https://api.twitter.com/2/users/by", urlHelper.getUsersByUrl());
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
    assertEquals("https://api.twitter.com/2/users/12345/retweets", urlHelper.getRetweetTweetUrl("12345"));
  }

  @Test
  public void testTweetUrl() {
    assertEquals("https://api.twitter.com/2/tweets/12345", urlHelper.getTweetUrl("12345"));
  }

  @Test
  public void testUrlGetTweetsV2() {
    assertEquals(
        "https://api.twitter.com/2/tweets",
        urlHelper.getTweetsUrl());
  }

  @Test
  public void testGetUserUrlFromName() {
    assertEquals("https://api.twitter.com/2/users/by/username/RedTheOne", urlHelper.getUserUrlFromName("RedTheOne"));
  }

  @Test
  public void testSearch7DaysUrl() {
    assertEquals("https://api.twitter.com/2/tweets/search/recent",
                 urlHelper.getSearchRecentTweetsUrl());
  }

  @Test
  public void testSearchAllTweetsUrl() {
    assertEquals(
        "https://api.twitter.com/2/tweets/search/all",
        urlHelper.getSearchAllTweetsUrl());
  }

  @Test
  public void testGetBearerTokenUrl() {
    assertEquals(
        "https://api.twitter.com/oauth2/token",
        URLHelper.GET_BEARER_TOKEN_URL);
  }

  @Test
  public void testUnretweetTweetUrl() {
    assertEquals("https://api.twitter.com/2/users/12345/retweets/67890", urlHelper.getUnretweetTweetUrl("12345", "67890"));
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
    assertEquals("https://api.twitter.com/2/tweets/search/stream", urlHelper.getFilteredStreamUrl());
  }

  @Test
  public void testSampledStreamUrl() {
    assertEquals("https://api.twitter.com/2/tweets/sample/stream", urlHelper.getSampledStreamUrl());
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
  public void testGetUserTimelineUrl() {
    assertEquals("https://api.twitter.com/2/users/99999/tweets",
                 urlHelper.getUserTimelineUrl("99999"));
  }

  @Test
  public void testGetUserMentionsUrl() {
    assertEquals("https://api.twitter.com/2/users/99999/mentions",
                 urlHelper.getUserMentionsUrl("99999"));
  }

  @Test
  public void testUploadMediaUrl() {
    assertEquals("https://upload.twitter.com/1.1/media/upload.json?media_category=tweet_image",
                 urlHelper.getUploadMediaUrl(MediaCategory.TWEET_IMAGE));
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
    assertEquals("https://api.twitter.com/2/users/" + userId + "/blocking", urlHelper.getBlockingUsersUrl(userId));
  }

  @Test
  public void testLikingUsers() {
    assertEquals("https://api.twitter.com/2/tweets/1354143047324299264/liking_users",
                 urlHelper.getLikingUsersUrl("1354143047324299264"));
  }

  @Test
  public void testLikedTweetsUrl() {
    String userId = "12345";
    assertEquals("https://api.twitter.com/2/users/" + userId + "/liked_tweets", urlHelper.getLikedTweetsUrl(userId));
  }

  @Test
  public void testDMListUrl() {
    assertEquals("https://api.twitter.com/1.1/direct_messages/events/list.json?count=50", urlHelper.getDMListUrl(50));
  }

  @Test
  public void testDmUrl() {
    String dmId = "110";
    assertEquals("https://api.twitter.com/1.1/direct_messages/events/show.json?id=" + dmId, urlHelper.getDmUrl(dmId));
  }

  @Test
  public void testPostDmUrl() {
    assertEquals("https://api.twitter.com/1.1/direct_messages/events/new.json", urlHelper.getPostDmUrl());
  }

  @Test
  public void testGetTweetsCountsUrl() {
    assertEquals("https://api.twitter.com/2/tweets/counts/recent", urlHelper.getTweetsCountUrl());
  }

  @Test
  public void testGetTweetsCountsAllUrl() {
    assertEquals("https://api.twitter.com/2/tweets/counts/all", urlHelper.getTweetsCountAllUrl());
  }

  @Test
  public void testMuteUserUrl() {
    assertEquals("https://api.twitter.com/2/users/12345/muting", urlHelper.getMuteUserUrl("12345"));
  }

  @Test
  public void testUnuteUserUrl() {
    assertEquals("https://api.twitter.com/2/users/12345/muting/67890", urlHelper.getUnmuteUserUrl("12345", "67890"));
  }

  @Test
  public void testGetSpaceUrl() {
    assertEquals("https://api.twitter.com/2/spaces/12345", urlHelper.getSpaceUrl("12345"));
  }
}
