package io.github.redouane59.twitter.helpers;

import io.github.redouane59.twitter.dto.tweet.MediaCategory;
import lombok.Getter;

public class URLHelper {

  public static final  int    MAX_LOOKUP                  = 100;
  public static final  String GET_BEARER_TOKEN_URL        = "https://api.twitter.com/oauth2/token";
  public static final  String ACCESS_TOKEN_URL            = "https://api.twitter.com/2/oauth2/token";
  public static final  String GET_OAUTH1_TOKEN_URL        = "https://api.twitter.com/oauth/request_token";
  public static final  String GET_OAUTH1_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
  private static final String ROOT_URL_V1                 = "https://api.twitter.com/1.1";
  public static final  String RATE_LIMIT_URL              = ROOT_URL_V1 + "/application/rate_limit_status.json";
  // v1 legacy
  private static final String IDS_JSON                    = "/ids.json?";
  private static final String ID                          = "id";
  private static final String COUNT                       = "count";
  private static final String LIST_JSON                   = "/list.json";
  private static final String SHOW_JSON                   = "/show.json?";
  private static final String FOLLOWERS                   = "/followers";
  private static final String FOLLOWING                   = "/friends";
  private static final String FRIENDSHIPS                 = "/friendships";
  private static final String TWEETS                      = "/tweets";
  private static final String SEARCH                      = "/search";
  private static final String THIRTY_DAYS                 = "/30day";
  private static final String FULL_ARCHIVE                = "/fullarchive";
  private static final String USER_ID                     = "user_id";
  private static final String JSON                        = ".json";
  public static final  String SEARCH_TWEET_STANDARD_URL   = ROOT_URL_V1 + SEARCH + TWEETS + JSON;
  private static final String COLLECTIONS                 = "/collections";
  private static final String DIRECT_MESSAGE_EVENTS       = "/direct_messages/events";

  // v2
  private final String idVariable             = ":id";
  @Getter
  private final String searchRecentTweetsUrl  = "https://api.twitter.com/2/tweets/search/recent";
  @Getter
  private final String searchAllTweetsUrl     = "https://api.twitter.com/2/tweets/search/all";
  @Getter
  private final String filteredStreamRulesUrl = "https://api.twitter.com/2/tweets/search/stream/rules";
  @Getter
  private final String filteredStreamUrl      = "https://api.twitter.com/2/tweets/search/stream";
  @Getter
  private final String sampledStreamUrl       = "https://api.twitter.com/2/tweets/sample/stream";
  @Getter
  private final String tweetsCountUrl         = "https://api.twitter.com/2/tweets/counts/recent";
  @Getter
  private final String tweetsCountAllUrl      = "https://api.twitter.com/2/tweets/counts/all";
  @Getter
  private final String tweetsUrl              = "https://api.twitter.com/2/tweets";
  @Getter
  private final String usersByUrl             = "https://api.twitter.com/2/users/by";
  @Getter
  private final String usersUrl               = "https://api.twitter.com/2/users";
  @Getter
  private final String spacesUrl              = "https://api.twitter.com/2/spaces";
  @Getter
  private final String spaceByCreatorUrl      = "https://api.twitter.com/2/spaces/by/creator_ids";
  @Getter
  private final String searchSpacesUrl        = "https://api.twitter.com/2/spaces/search";
  @Getter
  private final String listUrlV2              = "https://api.twitter.com/2/lists";
  @Getter
  private final String postTweetUrl           = "https://api.twitter.com/2/tweets";

  private final String followUrl           = "https://api.twitter.com/2/users/:id/following";
  private final String unfollowUrl         = "https://api.twitter.com/2/users/:sourceId/following/:targetId";
  private final String followersUrl        = "https://api.twitter.com/2/users/:id/followers";
  private final String followingUrl        = "https://api.twitter.com/2/users/:id/following";
  private final String userUrl             = "https://api.twitter.com/2/users/:id";
  private final String userUrlFromName     = "https://api.twitter.com/2/users/by/username/:username";
  private final String tweetUrl            = "https://api.twitter.com/2/tweets/:id";
  private final String likeUrl             = "https://api.twitter.com/2/users/:id/likes";
  private final String unlikeUrl           = "https://api.twitter.com/2/users/:userId/likes/:tweetId";
  private final String hideUrl             = "https://api.twitter.com/2/tweets/:id/hidden";
  private final String userTimelineUrl     = "https://api.twitter.com/2/users/:id/tweets";
  private final String userMentionsUrl     = "https://api.twitter.com/2/users/:id/mentions";
  private final String blockUserUrl        = "https://api.twitter.com/2/users/:id/blocking";
  private final String unblockUserUrl      = "https://api.twitter.com/2/users/:sourceId/blocking/:targetId";
  private final String blockingUsersUrl    = "https://api.twitter.com/2/users/:id/blocking";
  private final String likingUsersUrl      = "https://api.twitter.com/2/tweets/:id/liking_users";
  private final String likedTweetsUrl      = "https://api.twitter.com/2/users/:id/liked_tweets";
  private final String muteUserUrl         = "https://api.twitter.com/2/users/:id/muting";
  private final String unmuteUserUrl       = "https://api.twitter.com/2/users/:source_user_id/muting/:target_user_id";
  private final String mutedUsersUrl       = "https://api.twitter.com/2/users/:id/muting";
  private final String retweetingUsersUrl  = "https://api.twitter.com/2/tweets/:id/retweeted_by";
  private final String retweetTweetUrl     = "https://api.twitter.com/2/users/:id/retweets";
  private final String unretweetTweetUrl   = "https://api.twitter.com/2/users/:id/retweets/:source_tweet_id";
  private final String spaceUrl            = "https://api.twitter.com/2/spaces/:id";
  private final String spaceBuyersUrl      = "https://api.twitter.com/2/spaces/:id/buyers";
  private final String addListMemberUrl    = "https://api.twitter.com/2/lists/:id/members";
  private final String removeListMemberUrl = "https://api.twitter.com/2/lists/:id/members/:user_id";
  private final String pinListUrl          = "https://api.twitter.com/2/users/:id/pinned_lists";
  private final String unpinListUrl        = "https://api.twitter.com/2/users/:id/pinned_lists/:list_id";
  private final String followListUrl       = "https://api.twitter.com/2/users/:id/followed_lists";
  private final String unfollowListUrl     = "https://api.twitter.com/2/users/:id/followed_lists/:list_id";
  private final String ownedListUrl        = "https://api.twitter.com/2/users/:id/owned_lists";

  public String getSearchTweet30DaysUrl(String envName) {
    return ROOT_URL_V1 + TWEETS + SEARCH + THIRTY_DAYS + "/" + envName + JSON;
  }

  public String getSearchTweetFullArchiveUrl(String envName) {
    return ROOT_URL_V1 + TWEETS + SEARCH + FULL_ARCHIVE + "/" + envName + JSON;
  }

  public String getFollowUrl(String userId) {
    return followUrl.replace(idVariable, userId);
  }

  public String getUnfollowUrl(String sourceUserId, String targetUserId) {
    return unfollowUrl.replace(":sourceId", sourceUserId).replace(":targetId", targetUserId);
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
    return retweetingUsersUrl.replace(idVariable, tweetId);
  }

  public String getFollowersUrl(String userId) {
    return followersUrl.replace(idVariable, userId);
  }

  public String getFollowersIdsUrl(String userId) {
    return ROOT_URL_V1 + FOLLOWERS + IDS_JSON
           + USER_ID + "=" + userId +
           "&count=5000";
  }

  public String getFollowingIdsUrl(String userId) {
    return ROOT_URL_V1 + FOLLOWING + IDS_JSON
           + USER_ID + "=" + userId +
           "&count=5000";
  }

  public String getFollowingUrl(String userId) {
    return followingUrl.replace(idVariable, userId);
  }

  public String getUserUrl(String userId) {
    return userUrl.replace(idVariable, userId);
  }

  public String getUserUrlFromName(String username) {
    return userUrlFromName.replace(":username", username);
  }

  public String getTweetUrl(String tweetId) {
    return tweetUrl.replace(idVariable, tweetId);
  }

  public String getLikeUrl(String userId) {
    return likeUrl.replace(idVariable, userId);
  }

  public String getUnlikeUrl(String userId, String tweetId) {
    return unlikeUrl.replace(":userId", userId).replace(":tweetId", tweetId);
  }

  public String getRetweetTweetUrl(final String tweetId) {
    return retweetTweetUrl.replace(idVariable, tweetId);
  }

  public String getUnretweetTweetUrl(final String userId, final String tweetId) {
    return unretweetTweetUrl.replace(idVariable, userId).replace(":source_tweet_id", tweetId);
  }

  public String getHideReplyUrl(final String tweetId) {
    return hideUrl.replace(idVariable, tweetId);
  }

  public String getUserTimelineUrl(String userId) {
    return userTimelineUrl.replace(idVariable, userId);
  }

  public String getUserMentionsUrl(String userId) {
    return userMentionsUrl.replace(idVariable, userId);
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
    return blockUserUrl.replace(idVariable, userId);
  }

  public String getUnblockUserUrl(String sourceUserId, String targetUserId) {
    return unblockUserUrl.replace(":sourceId", sourceUserId).replace(":targetId", targetUserId);
  }

  public String getBlockingUsersUrl(String userId) {
    return blockingUsersUrl.replace(idVariable, userId);
  }

  public String getLikingUsersUrl(final String tweetId) {
    return likingUsersUrl.replace(idVariable, tweetId);
  }

  public String getLikedTweetsUrl(final String userId) {
    return likedTweetsUrl.replace(idVariable, userId);
  }

  public String getDMListUrl(int count) {
    return ROOT_URL_V1 + DIRECT_MESSAGE_EVENTS + LIST_JSON + "?" + COUNT + "=" + count;
  }

  public String getDmUrl(String id) {
    return ROOT_URL_V1 + DIRECT_MESSAGE_EVENTS + SHOW_JSON + ID + "=" + id;
  }

  public String getPostDmUrl() {
    return ROOT_URL_V1 + DIRECT_MESSAGE_EVENTS + "/new.json";
  }

  public String getMuteUserUrl(String sourceUserId) {
    return muteUserUrl.replace(idVariable, sourceUserId);
  }

  public String getUnmuteUserUrl(String sourceUserId, String targetUserId) {
    return unmuteUserUrl.replace(":source_user_id", sourceUserId).replace(":target_user_id", targetUserId);
  }

  public String getSpaceUrl(String id) {
    return spaceUrl.replace(idVariable, id);
  }

  public String getSpaceBuyersUrl(String spaceId) {
    return spaceBuyersUrl.replace(idVariable, spaceId);
  }

  public String getMutedUsersUrl(final String userId) {
    return mutedUsersUrl.replace(idVariable, userId);
  }

  public String getAddListMemberUrl(final String listId) {
    return addListMemberUrl.replace(idVariable, listId);
  }

  public String getRemoveListMemberUrl(final String listId, final String userId) {
    return removeListMemberUrl.replace(idVariable, listId).replace(":user_id", userId);
  }

  public String getPinListUrl(final String userId) {
    return pinListUrl.replace(idVariable, userId);
  }

  public String getUnpinListUrl(final String userId, final String listId) {
    return unpinListUrl.replace(idVariable, userId).replace(":list_id", listId);
  }

  public String getFollowListUrl(final String userId) {
    return followListUrl.replace(idVariable, userId);
  }

  public String getUnfollowListUrl(final String userId, final String listId) {
    return unfollowListUrl.replace(idVariable, userId).replace(":list_id", listId);
  }

  public String getOwnedListUrl(String userId) {
    return ownedListUrl.replace(idVariable, userId);
  }
}
