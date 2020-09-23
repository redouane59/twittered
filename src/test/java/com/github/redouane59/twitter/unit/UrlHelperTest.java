package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.redouane59.twitter.helpers.URLHelper;
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
    assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106",
                 urlHelper.getFollowerIdsUrl("952253106"));
  }

  @Test
  public void testUrlFollowingsById() {
    assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106",
                 urlHelper.getFollowingIdsUrl("952253106"));
  }

  @Test
  public void testUrlFollowingsUsersById() {
    assertEquals("https://api.twitter.com/1.1/friends/list.json?user_id=952253106&count=200",
                 urlHelper.getFollowingUsersUrl("952253106"));
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
    assertEquals("https://api.twitter.com/1.1/friendships/create.json?user_id=12345",
                 urlHelper.getFollowUrl("12345"));
  }

  @Test
  public void testUrlGetUserByIdV2() {
    assertEquals(
        "https://api.twitter.com/2/users/12345?expansions=pinned_tweet_id&user.fields=id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected",
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
  public void testGetUserTweetUrlById() {
    assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=12345&count=1&trim_user=true&include_rts=false",
                 urlHelper.getUserTweetsUrl("12345", 1));
  }

  @Test
  public void testGetFollowersByIdUrl() {
    assertEquals("https://api.twitter.com/1.1/followers/list.json?user_id=12345&count=200",
                 urlHelper.getFollowerUsersUrl("12345"));
  }

  @Test
  public void testSearchTweetsUrlStandard() {
    https:
//api.twitter.com/1.1/search/tweets.json
    assertEquals("https://api.twitter.com/1.1/search/tweets.json",
                 URLHelper.SEARCH_TWEET_STANDARD_URL);
  }

  @Test
  public void testUnfollowByUsernameUrl() {
    //https://api.twitter.com/1.1/friendships/destroy.json?user_id=2244994945
    assertEquals("https://api.twitter.com/1.1/friendships/destroy.json?screen_name=Red",
                 urlHelper.getUnfollowByUsernameUrl("Red"));
  }

  @Test
  public void testUnfollowByUserId() {
    assertEquals("https://api.twitter.com/1.1/friendships/destroy.json?user_id=12345",
                 urlHelper.getUnfollowUrl("12345"));
  }

  @Test
  public void testLikeUrl() {
    String tweetId = "12345";
    assertEquals("https://api.twitter.com/1.1/favorites/create.json?id=" + tweetId,
                 urlHelper.getLikeUrl(tweetId));
  }

  @Test
  public void testUnlikeUrl() {
    String tweetId = "12345";
    assertEquals("https://api.twitter.com/1.1/favorites/destroy.json?id=" + tweetId,
                 urlHelper.getUnlikeUrl(tweetId));
  }

  @Test
  public void testRetweetTweetUrl() {
    String tweetId = "12345";
    assertEquals("https://api.twitter.com/1.1/statuses/retweet/12345.json", urlHelper.getRetweetTweetUrl(tweetId));
  }

  @Test
  public void testTweetUrl() {
    assertEquals(
        "https://api.twitter.com/2/tweets/12345?expansions=author_id&tweet.fields=attachments,author_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,source,text,withheld,context_annotations,conversation_id&user.fields=id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected",
        urlHelper.getTweetUrl("12345"));
  }

  @Test
  public void testUrlGetTweetsV2() {
    assertEquals(
        "https://api.twitter.com/2/tweets?ids=1294174710624849921,1294380029430960128,1294375095746666496&tweet.fields=attachments,author_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,source,text,withheld,context_annotations,conversation_id&user.fields=id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected",
        urlHelper.getTweetListUrl(Arrays.asList("1294174710624849921,1294380029430960128,1294375095746666496")));
  }

  @Test
  public void testGetUserUrlFromName() {
    assertEquals(
        "https://api.twitter.com/2/users/by/username/RedTheOne?expansions=pinned_tweet_id&user.fields=id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected"
        ,
        urlHelper.getUserUrlFromName("RedTheOne"));
  }

  @Test
  public void testSearch7DaysUrl() {
    assertEquals("https://api.twitter.com/2/tweets/search/recent",
                 URLHelper.SEARCH_TWEET_7_DAYS_URL);
  }

  @Test
  public void testGetBearerTokenUrl() {
    assertEquals("https://api.twitter.com/oauth2/token",
                 URLHelper.GET_BEARER_TOKEN_URL);
  }

  @Test
  public void testPostNewTweetUrl() {
    assertEquals("https://api.twitter.com/1.1/statuses/update.json", urlHelper.getPostTweetUrl());
  }

  @Test
  public void testDeleteTweetUrl() {
    assertEquals("https://api.twitter.com/1.1/statuses/destroy/240854986559455234.json", urlHelper.getdeleteTweetUrl("240854986559455234"));
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
        "https://api.twitter.com/2/tweets/search/stream?tweet.fields=attachments,author_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,source,text,withheld,context_annotations,conversation_id&user.fields=id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected",
        urlHelper.getFilteredStreamUrl());
  }

  @Test
  public void testSampledStreamUrl() {
    assertEquals("https://api.twitter.com/2/tweets/sample/stream", urlHelper.getSampledStreamUrl());
  }

  @Test
  public void testSearchFullArchiveUrl() {
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
  public void testGetMentionsTimelineUrl() {
    assertEquals("https://api.twitter.com/1.1/statuses/mentions_timeline.json?include_entities=true&count=200",
                 urlHelper.getMentionsTimelineUrl(200));
  }

  @Test
  public void testGetMentionsTimelineUrlWithMaxId() {
    assertEquals("https://api.twitter.com/1.1/statuses/mentions_timeline.json?include_entities=true&count=10&max_id=12345",
                 urlHelper.getMentionsTimelineUrl(10, "12345"));
  }

  @Test
  public void testGetUserTimelineUrl() {
    assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=99999&count=200",
                 urlHelper.getUserTimelineUrl("99999", 200));
  }

  @Test
  public void testGetUserTimelineUrlWithMaxId() {
    assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=99999&count=10&max_id=12345",
                 urlHelper.getUserTimelineUrl("99999", 10, "12345"));
  }

}
