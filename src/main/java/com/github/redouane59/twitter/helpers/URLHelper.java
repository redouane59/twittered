package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.dto.tweet.MediaCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class URLHelper {

  private static final String ROOT_URL_V1                   = "https://api.twitter.com/1.1";
  private static final String ROOT_URL_V2                   = "https://api.twitter.com/2";
  private static final String ROOT_URL_LABS_V2              = "https://api.twitter.com/labs/2";
  private static final String IDS_JSON                      = "/ids.json?";
  private static final String SCREEN_NAME                   = "screen_name";
  private static final String ID                            = "id";
  private static final String COUNT                         = "count";
  private static final String LIST_JSON                     = "/list.json?";
  private static final String SHOW_JSON                     = "/show.json?";
  private static final String CREATE_JSON                   = "/create.json?";
  private static final String DESTROY_JSON                  = "/destroy.json?";
  private static final String RETWEETERS                    = "/retweeters";
  private static final String FOLLOWERS                     = "/followers";
  private static final String FOLLOWING                     = "/following";
  private static final String STATUSES                      = "/statuses";
  private static final String FRIENDSHIPS                   = "/friendships";
  private static final String FAVORITES                     = "/favorites";
  private static final String USERS                         = "/users";
  private static final String TWEETS                        = "/tweets";
  private static final String MENTIONS                      = "/mentions";
  private static final String SEARCH                        = "/search";
  private static final String SAMPLE                        = "/sample";
  private static final String STREAM                        = "/stream";
  private static final String THIRTY_DAYS                   = "/30day";
  private static final String FULL_ARCHIVE                  = "/fullarchive";
  private static final String ACCOUNT_ACTIVITY              = "/account_activity/all";
  private static final String WEBHOOKS                      = "/webhooks";
  private static final String USER_ID                       = "user_id";
  private static final String LOOKUP_JSON                   = "/lookup.json?";
  private static final String USER_TIMELINE                 = "/user_timeline.json?";
  private static final String JSON                          = ".json";
  private static final String TRIM_USER                     = "trim_user=true";
  private static final String EXCLUDE_RTS                   = "include_rts=false";
  private static final String USER_FORMAT_DETAILED          = "user.format=detailed";
  private static final String TWEET_FORMAT_DETAILED         = "tweet.format=detailed";
  private static final String MAX_ID                        = "max_id";
  private static final String COLLECTIONS                   = "/collections";
  private static final int    MAX_COUNT                     = 200;
  private static final int    RETWEET_MAX_COUNT             = 100;
  public static final  int    MAX_LOOKUP                    = 100;
  public static final  String USER_FIELDS                   = "user.fields=";
  public static final  String ALL_USER_FIELDS               =
      "id,created_at,entities,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected";
  public static final  String TWEET_FIELDS                  = "tweet.fields=";
  public static final  String
                              ALL_TWEET_FIELDS              =
      "attachments,author_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,source,text,withheld,context_annotations,conversation_id,reply_settings";
  public static final  String EXPANSION                     = "expansions=";
  public static final  String
                              ALL_EXPANSIONS                =
      "author_id,entities.mentions.username,in_reply_to_user_id,referenced_tweets.id,referenced_tweets.id.author_id";
  public static final  String LAST_TWEET_LIST_URL           = ROOT_URL_V1 + STATUSES + USER_TIMELINE;
  public static final  String RATE_LIMIT_URL                = ROOT_URL_V1 + "/application/rate_limit_status.json";
  public static final  String SEARCH_TWEET_STANDARD_URL     = ROOT_URL_V1 + SEARCH + TWEETS + JSON;
  public static final  String SEARCH_TWEET_7_DAYS_URL       = ROOT_URL_V2 + TWEETS + SEARCH + "/recent";
  public static final  String SEARCH_TWEET_FULL_ARCHIVE_URL = ROOT_URL_V2 + TWEETS + SEARCH + "/all" + "?" + EXPANSION + ALL_EXPANSIONS;
  public static final  String GET_BEARER_TOKEN_URL          = "https://api.twitter.com/oauth2/token";
  public static final  String GET_OAUTH1_TOKEN_URL          = "https://api.twitter.com/oauth/request_token";
  public static final  String GET_OAUTH1_ACCESS_TOKEN_URL   = "https://api.twitter.com/oauth/access_token";
  private static final String MAX_RESULTS                   = "max_results";
  private static final String BLOCKING                      = "/blocking";
  private static final String LIKES                         = "/likes";

  public String getSearchTweet30DaysUrl(String envName) {
    return ROOT_URL_V1 + TWEETS + SEARCH + THIRTY_DAYS + "/" + envName + JSON;
  }

  public String getSearchTweetFullArchiveUrl(String envName) {
    return ROOT_URL_V1 + TWEETS + SEARCH + FULL_ARCHIVE + "/" + envName + JSON;
  }

  public String getLiveEventUrl(String envName) {
    return ROOT_URL_V1 + ACCOUNT_ACTIVITY + "/" + envName + WEBHOOKS + JSON;
  }

  public String getFollowUrl(String userId) {
    return ROOT_URL_V2 +
           USERS +
           "/" + userId +
           FOLLOWING;
  }

  public String getUnfollowUrl(String sourceUserId, String targetUserId) {
    return ROOT_URL_V2 +
           USERS +
           "/" + sourceUserId +
           FOLLOWING
           + "/" + targetUserId;
  }

  public String getFriendshipUrl(String sourceId, String targetId) {
    return ROOT_URL_V1 +
           FRIENDSHIPS +
           SHOW_JSON +
           "source_" + ID + "=" +
           sourceId +
           "&target_" + ID + "=" +
           targetId;
  }

  public String getRetweetersUrl(String tweetId) {
    return ROOT_URL_V1 +
           STATUSES +
           RETWEETERS +
           IDS_JSON +
           ID + "=" +
           tweetId +
           "&" + COUNT + "=" +
           RETWEET_MAX_COUNT;
  }

  public String getFollowersUrl(String userId) {
    return ROOT_URL_V2 +
           USERS +
           "/" +
           userId +
           FOLLOWERS +
           "?" +
           MAX_RESULTS +
           "=1000" +
           "&" +
           USER_FIELDS +
           ALL_USER_FIELDS;
  }

  public String getFollowersIdsUrl(String userId) {
    return ROOT_URL_V1 + FOLLOWERS + IDS_JSON
           + USER_ID + "=" + userId +
           "&count=5000";
  }

  public String getFollowingUrl(String userId) {
    return ROOT_URL_V2 +
           USERS +
           "/" +
           userId +
           FOLLOWING +
           "?" +
           MAX_RESULTS +
           "=1000" +
           "&" +
           USER_FIELDS +
           ALL_USER_FIELDS;
  }

  public String getUserUrl(String userId) {
    return ROOT_URL_V2 +
           USERS +
           "/" +
           userId +
           "?" +
           "expansions=pinned_tweet_id" +
           "&" +
           USER_FIELDS +
           ALL_USER_FIELDS;
  }

  public String getUserUrlFromName(String username) {
    return ROOT_URL_V2 +
           USERS +
           "/by/username/" +
           username +
           "?" +
           "expansions=pinned_tweet_id" +
           "&" +
           USER_FIELDS +
           ALL_USER_FIELDS;
  }

  public String getTweetUrl(String tweetId) {
    return ROOT_URL_V2 +
           "/tweets/" +
           tweetId +
           "?" +
           EXPANSION +
           ALL_EXPANSIONS +
           "&" +
           TWEET_FIELDS +
           ALL_TWEET_FIELDS +
           "&" +
           USER_FIELDS +
           ALL_USER_FIELDS;
  }

  public String getTweetListUrl(List<String> ids) {
    StringBuilder result = new StringBuilder(ROOT_URL_V2 +
                                             "/tweets?ids=");
    int i = 0;
    while (i < ids.size() && i < MAX_LOOKUP) {
      String id = ids.get(i);
      result.append(id);
      result.append(",");
      i++;
    }
    result.delete(result.length() - 1, result.length());
    result.append("&");
    result.append(TWEET_FIELDS);
    result.append(ALL_TWEET_FIELDS);
    result.append("&");
    result.append(USER_FIELDS);
    result.append(ALL_USER_FIELDS);
    return result.toString();
  }

  public String getUsersUrlbyNames(List<String> names) {
    StringBuilder result = new StringBuilder(ROOT_URL_V2)
        .append(USERS)
        .append("/by?usernames=");
    int i = 0;
    while (i < names.size() && i < MAX_LOOKUP) {
      String name = names.get(i);
      result.append(name);
      result.append(",");
      i++;
    }
    result.delete(result.length() - 1, result.length());
    return result.toString();
  }

  public String getUsersUrlbyIds(List<String> ids) {
    StringBuilder result = new StringBuilder(ROOT_URL_V2)
        .append(USERS)
        .append("?ids=");
    int i = 0;
    while (i < ids.size() && i < MAX_LOOKUP) {
      String id = ids.get(i);
      result.append(id);
      result.append(",");
      i++;
    }
    result.delete(result.length() - 1, result.length());
    return result.toString();
  }

  public String getUserTweetsUrl(String userId, int count) {
    return ROOT_URL_V1 +
           STATUSES +
           USER_TIMELINE +
           USER_ID + "=" +
           userId +
           "&" + COUNT + "=" +
           count +
           "&" + TRIM_USER +
           "&" + EXCLUDE_RTS;
  }

  public String getLikeUrl(String userId) {
    return ROOT_URL_V2 + USERS + "/" + userId + LIKES;
  }

  public String getUnlikeUrl(String userId, String tweetId) {
    return ROOT_URL_V2 + USERS + "/" + userId + LIKES + "/" + tweetId;
  }

  public String getRetweetTweetUrl(final String tweetId) {
    return ROOT_URL_V1 + STATUSES + "/retweet/" + tweetId + JSON;
  }

  public String getPostTweetUrl() {
    return ROOT_URL_V1 + STATUSES + "/update.json";
  }

  public String getDeleteTweetUrl(String tweetId) {
    return ROOT_URL_V1 + STATUSES + "/destroy/" + tweetId + JSON;
  }

  public String getFavoriteTweetsUrl(String userId, String maxId) {
    String result;
    if (maxId == null || maxId.length() == 0) {
      result = "https://api.twitter.com/1.1/favorites/list.json?count=200&user_id=" + userId;
    } else {
      result = "https://api.twitter.com/1.1/favorites/list.json?count=200&user_id=" + userId + "&" + MAX_ID + "=" + maxId;
    }
    return result.concat("&tweet_mode=extended");
  }

  public String getHideReplyUrl(final String tweetId) {
    return ROOT_URL_V2 + TWEETS + "/" + tweetId + "/hidden";
  }

  public String getFilteredStreamRulesUrl() {
    return ROOT_URL_V2 + TWEETS + SEARCH + STREAM + "/rules";
  }

  public String getFilteredStreamUrl() {
    return ROOT_URL_V2
           + TWEETS
           + SEARCH
           + STREAM
           + "?"
           + EXPANSION
           + ALL_EXPANSIONS
           + "&"
           + TWEET_FIELDS
           + ALL_TWEET_FIELDS
           + "&"
           + USER_FIELDS
           + ALL_USER_FIELDS;
  }

  public String getSampledStreamUrl() {
    return ROOT_URL_V2
           + TWEETS
           + SAMPLE
           + STREAM
           + "?"
           + EXPANSION
           + ALL_EXPANSIONS
           + "&"
           + TWEET_FIELDS
           + ALL_TWEET_FIELDS
           + "&"
           + USER_FIELDS
           + ALL_USER_FIELDS;
  }

  public String getUserTimelineUrl(String userId, int maxResult, LocalDateTime startTime, LocalDateTime endTime, String sinceId, String untilId) {
    String result = ROOT_URL_V2 + USERS + "/" + userId + TWEETS + "?" + MAX_RESULTS + "=" + maxResult;
    if (startTime != null) {
      result += "&start_time=" + ConverterHelper.getStringFromDateV2(startTime);
    }
    if (endTime != null) {
      result += "&end_time=" + ConverterHelper.getStringFromDateV2(endTime);
    }
    if (sinceId != null) {
      result += "&since_id=" + sinceId;
    }
    if (untilId != null) {
      result += "&until_id=" + untilId;
    }
    result += "&" + TWEET_FIELDS + ALL_TWEET_FIELDS;
    return result;
  }

  public String getUserMentionsUrl(String userId, int maxResult, LocalDateTime startTime, LocalDateTime endTime, String sinceId, String untilId) {
    String result = ROOT_URL_V2 + USERS + "/" + userId + MENTIONS + "?" + MAX_RESULTS + "=" + maxResult;
    if (startTime != null) {
      result += "&start_time=" + ConverterHelper.getStringFromDateV2(startTime);
    }
    if (endTime != null) {
      result += "&end_time=" + ConverterHelper.getStringFromDateV2(endTime);
    }
    if (sinceId != null) {
      result += "&since_id=" + sinceId;
    }
    if (untilId != null) {
      result += "&until_id=" + untilId;
    }
    result += "&" + TWEET_FIELDS + ALL_TWEET_FIELDS;
    return result;
  }

  public String getUploadMediaUrl(MediaCategory mediaCategory) {
    return "https://upload.twitter.com/1.1/media/upload.json?media_category=" + mediaCategory.label;
  }

  public String getCollectionsCreateUrl() {
    return ROOT_URL_V1
           + COLLECTIONS
           + "/create.json";
  }

  public String getCollectionsCurateUrl() {
    return ROOT_URL_V1
           + COLLECTIONS
           + "/entries/curate.json";
  }

  public String getCollectionsDestroyUrl(String collectionId) {
    return ROOT_URL_V1 +
           COLLECTIONS +
           "/destroy.json" +
           "?" +
           ID +
           "=" +
           collectionId;
  }

  public String getCollectionsEntriesUrl(String collectionId) {
    return ROOT_URL_V1 +
           COLLECTIONS +
           "/entries.json" +
           "?" +
           ID +
           "=" +
           collectionId;
  }

  public String getBlockUserUrl(String userId) {
    return ROOT_URL_V2 + USERS + "/" + userId + BLOCKING;
  }

  public String getUnblockUserUrl(String sourceUserId, String targetUserId) {
    return ROOT_URL_V2 + USERS + "/" + sourceUserId + BLOCKING + "/" + targetUserId;
  }

  public String getBlockingUsersUrl(String userId) {
    return ROOT_URL_V2 + USERS + "/" + userId + BLOCKING + "?" + USER_FIELDS + ALL_USER_FIELDS;
  }

  public String getLikingUsersUrl(final String tweetId) {
    return ROOT_URL_V2 + TWEETS + "/" + tweetId + "/liking_users"
           + "?" + USER_FIELDS + ALL_USER_FIELDS;
  }

  public String getLikedTweetsUrl(final String userId) {
    return ROOT_URL_V2 + USERS + "/" + userId + "/liked_tweets"
           + "?" + TWEET_FIELDS + ALL_TWEET_FIELDS;
  }
}
