package io.github.redouane59.twitter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import io.github.redouane59.RelationType;
import io.github.redouane59.twitter.dto.collections.CollectionsResponse;
import io.github.redouane59.twitter.dto.collections.TimeLineOrder;
import io.github.redouane59.twitter.dto.dm.DirectMessage;
import io.github.redouane59.twitter.dto.dm.DmEvent;
import io.github.redouane59.twitter.dto.dm.DmListAnswer;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.getrelationship.IdList;
import io.github.redouane59.twitter.dto.getrelationship.RelationshipObjectResponse;
import io.github.redouane59.twitter.dto.others.BlockResponse;
import io.github.redouane59.twitter.dto.others.RateLimitStatus;
import io.github.redouane59.twitter.dto.others.RequestToken;
import io.github.redouane59.twitter.dto.stream.StreamRules;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import io.github.redouane59.twitter.dto.tweet.HiddenResponse;
import io.github.redouane59.twitter.dto.tweet.HiddenResponse.HiddenData;
import io.github.redouane59.twitter.dto.tweet.LikeResponse;
import io.github.redouane59.twitter.dto.tweet.MediaCategory;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetCountsList;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.tweet.TweetSearchResponseV1;
import io.github.redouane59.twitter.dto.tweet.TweetV1;
import io.github.redouane59.twitter.dto.tweet.TweetV1Deserializer;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import io.github.redouane59.twitter.dto.tweet.UploadMediaResponse;
import io.github.redouane59.twitter.dto.user.FollowBody;
import io.github.redouane59.twitter.dto.user.FollowResponse;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserList;
import io.github.redouane59.twitter.dto.user.UserV2;
import io.github.redouane59.twitter.dto.user.UserV2.UserData;
import io.github.redouane59.twitter.helpers.AbstractRequestHelper;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import io.github.redouane59.twitter.helpers.RequestHelper;
import io.github.redouane59.twitter.helpers.RequestHelperV2;
import io.github.redouane59.twitter.helpers.URLHelper;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class TwitterClient implements ITwitterClientV1, ITwitterClientV2, ITwitterClientArchive {

  public static final  ObjectMapper       OBJECT_MAPPER                        = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL);
  private              URLHelper          urlHelper                            = new URLHelper();
  private              RequestHelper      requestHelperV1;
  private              RequestHelperV2    requestHelperV2;
  private              TwitterCredentials twitterCredentials;
  public static final  String             TWEET_FIELDS                         = "tweet.fields";
  public static final  String
                                          ALL_TWEET_FIELDS                     =
      "attachments,author_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,source,text,withheld,context_annotations,conversation_id,reply_settings";
  public static final  String             EXPANSION                            = "expansions";
  public static final  String
                                          ALL_EXPANSIONS                       =
      "author_id,entities.mentions.username,in_reply_to_user_id,referenced_tweets.id,referenced_tweets.id.author_id";
  public static final  String             USER_FIELDS                          = "user.fields";
  public static final  String             ALL_USER_FIELDS                      =
      "id,created_at,entities,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected";
  private static final String             QUERY                                = "query";
  private static final String             CURSOR                               = "cursor";
  private static final String             NEXT                                 = "next";
  private static final String             PAGINATION_TOKEN                     = "pagination_token";
  private static final String             PINNED_TWEET_ID                      = "pinned_tweet_id";
  private static final String[]           DEFAULT_VALID_CREDENTIALS_FILE_NAMES = {"test-twitter-credentials.json",
                                                                                  "twitter-credentials.json"};

  public TwitterClient() {
    this(getAuthentication());
  }

  public TwitterClient(TwitterCredentials credentials) {
    this(credentials, new ServiceBuilder(credentials.getApiKey()).apiSecret(credentials.getApiSecretKey()));
  }

  public TwitterClient(TwitterCredentials credentials, HttpClient httpClient) {
    this(credentials,
         new ServiceBuilder(credentials.getApiKey()).apiSecret(credentials.getApiSecretKey()).httpClient(httpClient));
  }

  public TwitterClient(TwitterCredentials credentials, HttpClient httpClient, HttpClientConfig config) {
    this(credentials, new ServiceBuilder(credentials.getApiKey()).apiSecret(credentials.getApiSecretKey())
                                                                 .httpClient(httpClient).httpClientConfig(config));
  }

  public TwitterClient(TwitterCredentials credentials, ServiceBuilder serviceBuilder) {
    this(credentials, serviceBuilder.apiKey(credentials.getApiKey()).apiSecret(credentials.getApiSecretKey())
                                    .build(TwitterApi.instance()));
  }

  public TwitterClient(TwitterCredentials credentials, OAuth10aService service) {
    twitterCredentials = credentials;
    requestHelperV1    = new RequestHelper(credentials, service);
    requestHelperV2    = new RequestHelperV2(credentials, service);
  }

  // can manage up to 5000 results / call . Max 15 calls / 15min ==> 75.000
  // results max. / 15min
  private List<String> getUserIdsByRelation(String url) {
    String       cursor = "-1";
    List<String> result = new ArrayList<>();
    do {
      String           urlWithCursor  = url + "&" + CURSOR + "=" + cursor;
      Optional<IdList> idListResponse = this.getRequestHelper().getRequest(urlWithCursor, IdList.class);
      if (!idListResponse.isPresent()) {
        break;
      }
      result.addAll(idListResponse.get().getIds());
      cursor = idListResponse.get().getNextCursor();
    } while (!cursor.equals("0"));
    return result;
  }

  // can manage up to 200 results/call . Max 15 calls/15min ==> 3.000 results
  // max./15min
  private List<User> getUsersInfoByRelation(String url) {
    String              token      = null;
    List<User>          result     = new ArrayList<>();
    Map<String, String> parameters = new HashMap<>();
    parameters.put(AdditionalParameters.MAX_RESULTS, String.valueOf(1000));
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(EXPANSION, PINNED_TWEET_ID);
    do {
      String urlWithCursor = url;
      if (token != null) {
        urlWithCursor = urlWithCursor + "?" + PAGINATION_TOKEN + "=" + token;
      }
      Optional<UserList> userListDTO = this.getRequestHelper().getRequestWithParameters(urlWithCursor, parameters, UserList.class);
      if (!userListDTO.isPresent()) {
        break;
      }
      result.addAll(userListDTO.get().getData());
      token = userListDTO.get().getMeta().getNextToken();
    } while (token != null);
    return result;
  }

  /**
   * Get the users corresponding to a specific relation in oneshot
   *
   * @param userId the id of the user
   * @param relationType the relations you want to retrieve
   * @return a list of users corresponding to the specified relation
   */
  public List<User> getUsersByRelation(String userId, RelationType relationType) {
    String url = null;
    if (relationType == RelationType.FOLLOWER) {
      url = this.urlHelper.getFollowersUrl(userId);
    } else if (relationType == RelationType.FOLLOWING) {
      url = this.urlHelper.getFollowingUrl(userId);
    }
    return this.getUsersInfoByRelation(url);
  }

  @Override
  public UserList getFollowers(String userId) {
    return this.getFollowers(userId, AdditionalParameters.builder().maxResults(1000).build());
  }

  @Override
  public UserList getFollowers(final String userId, final AdditionalParameters additionalParameters) {
    String              url        = this.urlHelper.getFollowersUrl(userId);
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return this.getRequestHelper().getRequestWithParameters(url, parameters, UserList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserList getFollowing(String userId) {
    return this.getFollowing(userId, AdditionalParameters.builder().maxResults(1000).build());
  }

  @Override
  public UserList getFollowing(final String userId, final AdditionalParameters additionalParameters) {
    String              url        = this.urlHelper.getFollowingUrl(userId);
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return this.getRequestHelper().getRequestWithParameters(url, parameters, UserList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public RelationType getRelationType(String userId1, String userId2) {
    String url = this.urlHelper.getFriendshipUrl(userId1, userId2);
    RelationshipObjectResponse relationshipDTO = this.getRequestHelper().getRequest(url, RelationshipObjectResponse.class)
                                                     .orElseThrow(NoSuchElementException::new);
    Boolean followedBy = relationshipDTO.getRelationship().getSource().isFollowedBy();
    Boolean following  = relationshipDTO.getRelationship().getSource().isFollowing();
    if (followedBy && following) {
      return RelationType.FRIENDS;
    } else if (!followedBy && !following) {
      return RelationType.NONE;
    } else if (followedBy) {
      return RelationType.FOLLOWER;
    } else {
      return RelationType.FOLLOWING;
    }
  }

  @Override
  public List<String> getRetweetersId(String tweetId) {
    String url = this.urlHelper.getRetweetersUrl(tweetId);
    return this.getUserIdsByRelation(url);
  }

  @Override
  public List<String> getFollowersIds(String userId) {
    String url = this.urlHelper.getFollowersIdsUrl(userId);
    return this.getUserIdsByRelation(url);
  }

  @SneakyThrows
  @Override
  public FollowResponse follow(String targetUserId) {
    String url  = this.urlHelper.getFollowUrl(this.getUserIdFromAccessToken());
    String body = OBJECT_MAPPER.writeValueAsString(new FollowBody(targetUserId));
    return this.requestHelperV1.postRequestWithBodyJson(url, new HashMap<>(), body, FollowResponse.class)
                               .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public FollowResponse unfollow(String targetUserId) {
    String url = this.urlHelper.getUnfollowUrl(this.getUserIdFromAccessToken(), targetUserId);
    return this.getRequestHelper().makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, FollowResponse.class)
               .orElseThrow(NoSuchElementException::new);

  }

  @SneakyThrows
  @Override
  public BlockResponse blockUser(final String targetUserId) {
    String url = this.urlHelper.getBlockUserUrl(this.getUserIdFromAccessToken());
    return this.getRequestHelper()
               .makeRequest(Verb.POST,
                            url,
                            new HashMap<>(),
                            OBJECT_MAPPER.writeValueAsString(new FollowBody(targetUserId)),
                            true,
                            BlockResponse.class)
               .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public BlockResponse unblockUser(final String targetUserId) {
    String url = this.urlHelper.getUnblockUserUrl(this.getUserIdFromAccessToken(), targetUserId);
    return this.getRequestHelper().makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, BlockResponse.class)
               .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserList getBlockedUsers() {
    String              url        = this.urlHelper.getBlockingUsersUrl(this.getUserIdFromAccessToken());
    Map<String, String> parameters = new HashMap<>();
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return this.getRequestHelper().getRequestWithParameters(url, parameters, UserList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public User getUserFromUserId(String userId) {
    String              url        = this.getUrlHelper().getUserUrl(userId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(EXPANSION, PINNED_TWEET_ID);
    return this.getRequestHelper().getRequestWithParameters(url, parameters, UserV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserV2 getUserFromUserName(String userName) {
    String              url        = this.getUrlHelper().getUserUrlFromName(userName);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(EXPANSION, PINNED_TWEET_ID);
    return this.getRequestHelper().getRequestWithParameters(url, parameters, UserV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<User> getUsersFromUserNames(List<String> userNames) {
    String url = this.getUrlHelper().getUsersUrlbyNames(userNames);
    List<UserData> result = this.getRequestHelper().getRequest(url, UserList.class)
                                .orElseThrow(NoSuchElementException::new).getData();
    return result.stream().map(userData -> UserV2.builder().data(userData).build()).collect(Collectors.toList());
  }

  @Override
  public List<User> getUsersFromUserIds(List<String> userIds) {
    String url = this.getUrlHelper().getUsersUrlbyIds(userIds);
    List<UserData> result = this.getRequestHelper().getRequest(url, UserList.class)
                                .orElseThrow(NoSuchElementException::new).getData();
    return result.stream().map(userData -> UserV2.builder().data(userData).build()).collect(Collectors.toList());
  }

  @Override
  public RateLimitStatus getRateLimitStatus() {
    String url = URLHelper.RATE_LIMIT_URL;
    return this.getRequestHelper().getRequest(url, RateLimitStatus.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public LikeResponse likeTweet(String tweetId) {
    String url = this.getUrlHelper().getLikeUrl(this.getUserIdFromAccessToken());
    return this.getRequestHelperV1().postRequestWithBodyJson(url, new HashMap<>(), "{\"tweet_id\":\"" + tweetId + "\"}", LikeResponse.class)
               .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public LikeResponse unlikeTweet(String tweetId) {
    String url = this.getUrlHelper().getUnlikeUrl(this.getUserIdFromAccessToken(), tweetId);
    return getRequestHelper().makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, LikeResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserList getLikingUsers(final String tweetId) {
    String              url        = this.getUrlHelper().getLikingUsersUrl(tweetId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return getRequestHelper().getRequestWithParameters(url, parameters, UserList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public TweetList getLikedTweets(final String userId) {
    String              url        = this.getUrlHelper().getLikedTweetsUrl(userId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    return getRequestHelper().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public TweetCountsList getTweetCounts(final String query) {
    return this.getTweetCounts(query, AdditionalParameters.builder().build());
  }

  @Override
  public TweetCountsList getTweetCounts(final String query, AdditionalParameters additionalParameters) {
    String url = this.getUrlHelper().getTweetsCountUrl();
    return this.getTweetCounts(url, query, additionalParameters);
  }

  @Override
  public TweetCountsList getAllTweetCounts(final String query) {
    return this.getAllTweetCounts(query, AdditionalParameters.builder().build());
  }

  @Override
  public TweetCountsList getAllTweetCounts(final String query, AdditionalParameters additionalParameters) {
    String url = this.getUrlHelper().getTweetsCountAllUrl();
    return this.getTweetCounts(url, query, additionalParameters);
  }

  private TweetCountsList getTweetCounts(String url, final String query, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(QUERY, query);
    return getRequestHelperV2().getRequestWithParameters(url, parameters, TweetCountsList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet retweetTweet(String tweetId) {
    String url = this.getUrlHelper().getRetweetTweetUrl(tweetId);
    return this.requestHelperV1.postRequest(url, new HashMap<>(), TweetV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet postTweet(String text) {
    return this.postTweet(text, null);
  }

  @Override
  public Tweet postTweet(String text, String inReplyToStatusId) {
    return this.postTweet(text, inReplyToStatusId, null);
  }

  @Override
  public Tweet postTweet(String text, String inReplyToStatusId, String mediaIds) {
    return this.postTweet(text, inReplyToStatusId, mediaIds, null);
  }

  @Override
  public Tweet postTweet(final String text, final String inReplyToStatusId, final String mediaIds, final String attachmentUrl) {
    String              url        = this.getUrlHelper().getPostTweetUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put("status", text);
    if (inReplyToStatusId != null) {
      parameters.put("in_reply_to_status_id", inReplyToStatusId);
      parameters.put("auto_populate_reply_metadata", "true");
    }
    if (mediaIds != null) {
      parameters.put("media_ids", mediaIds);
    }
    if (attachmentUrl != null) {
      parameters.put("attachment_url", attachmentUrl);
    }
    return this.requestHelperV1.postRequest(url, parameters, TweetV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet deleteTweet(String tweetId) {
    String              url        = this.getUrlHelper().getDeleteTweetUrl(tweetId);
    Map<String, String> parameters = new HashMap<>();
    return this.requestHelperV1.postRequest(url, parameters, TweetV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet getTweet(String tweetId) {
    String              url        = this.getUrlHelper().getTweetUrl(tweetId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return this.getRequestHelper().getRequestWithParameters(url, parameters, TweetV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public TweetList getTweets(List<String> tweetIds) {
    String              url        = this.getUrlHelper().getTweetListUrl(tweetIds);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return this.getRequestHelper().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public boolean hideReply(final String tweetId, final boolean hide) {
    String url = this.getUrlHelper().getHideReplyUrl(tweetId);
    try {
      String body = TwitterClient.OBJECT_MAPPER.writeValueAsString(new HiddenData(hide));
      HiddenResponse response = this.requestHelperV1.putRequest(url, body, HiddenResponse.class)
                                                    .orElseThrow(NoSuchElementException::new);
      return response.getData().isHidden();
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException();
    }
  }

  @Override
  public List<Tweet> getFavorites(String userId, int count) {
    List<Tweet>   favoriteTweets = new ArrayList<>();
    List<TweetV1> result;
    String        maxId          = null;
    do {
      result = Arrays.asList(
          this.getRequestHelper().getRequest(this.getUrlHelper().getFavoriteTweetsUrl(userId, maxId), TweetV1[].class)
              .orElseThrow(NoSuchElementException::new));
      if (result.isEmpty()) {
        break;
      }
      maxId = result.get(result.size() - 1).getId();
      favoriteTweets.addAll(result.subList(0, result.size() - 1)); // to avoid having duplicates
    } while (favoriteTweets.size() < count && result.size() > 1);
    return favoriteTweets;
  }

  @Override
  public TweetList searchTweets(String query) {
    return this.searchTweets(query, AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public TweetList searchTweets(String query, AdditionalParameters additionalParameters) {
    return this.searchTweets(query, additionalParameters, false);
  }

  @Override
  public TweetList searchTweets(String query, AdditionalParameters additionalParameters, boolean recursively) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(QUERY, query);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    if (!recursively) {
      return this.getRequestHelper().getRequestWithParameters(this.urlHelper.getSearchRecentTweetsUrl(),
                                                              parameters,
                                                              TweetList.class).orElseThrow(NoSuchElementException::new);
    } else {
      if (additionalParameters.getMaxResults() <= 0) {
        parameters.put(additionalParameters.MAX_RESULTS, String.valueOf(100));
      }
      return TweetList.builder().data(this.getTweetsRecursively(this.urlHelper.getSearchRecentTweetsUrl(), parameters)).build();
    }
  }

  public List<TweetData> getTweetsRecursively(String url, Map<String, String> parameters) {
    String          next;
    List<TweetData> result = new ArrayList<>();
    do {
      Optional<TweetList> tweetSearchV2DTO = this.getRequestHelper().getRequestWithParameters(url, parameters, TweetList.class);
      if (!tweetSearchV2DTO.isPresent() || tweetSearchV2DTO.get().getData() == null) {
        break;
      }
      result.addAll(tweetSearchV2DTO.get().getData());
      next = tweetSearchV2DTO.get().getMeta().getNextToken();
      parameters.put(AdditionalParameters.NEXT_TOKEN, next);
    } while (next != null);
    return result;
  }

  @Override
  public TweetList searchAllTweets(final String query) {
    return this.searchAllTweets(query, AdditionalParameters.builder().maxResults(500).build());
  }

  @Override
  public TweetList searchAllTweets(final String query, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(QUERY, query);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    return this.getRequestHelperV2().getRequestWithParameters(this.urlHelper.getSearchAllTweetsUrl(),
                                                              parameters,
                                                              TweetList.class).orElseThrow(NoSuchElementException::new);

  }


  @Override
  public List<Tweet> searchForTweetsWithin30days(String query, LocalDateTime fromDate, LocalDateTime toDate,
                                                 String envName) {
    int                 count      = 100;
    Map<String, String> parameters = new HashMap<>();
    parameters.put(QUERY, query);
    parameters.put("maxResults", String.valueOf(count));
    parameters.put("fromDate", ConverterHelper.getStringFromDate(fromDate));
    parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
    String      next;
    List<Tweet> result = new ArrayList<>();
    do {
      Optional<TweetSearchResponseV1> tweetSearchV1DTO = this.getRequestHelper().getRequestWithParameters(
          urlHelper.getSearchTweet30DaysUrl(envName), parameters, TweetSearchResponseV1.class);
      if (!tweetSearchV1DTO.isPresent() || tweetSearchV1DTO.get().getResults() == null) {
        break;
      }
      result.addAll(tweetSearchV1DTO.get().getResults());
      next = tweetSearchV1DTO.get().getNext();
      parameters.put(NEXT, next);
    } while (next != null);
    return result;
  }

  @Override
  @Deprecated
  public List<Tweet> searchForTweetsArchive(String query, LocalDateTime fromDate, LocalDateTime toDate,
                                            String envName) {
    int                 count      = 100;
    Map<String, String> parameters = new HashMap<>();
    parameters.put(QUERY, query);
    parameters.put(AdditionalParameters.MAX_RESULTS, String.valueOf(count));
    parameters.put("fromDate", ConverterHelper.getStringFromDate(fromDate));
    parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
    String      next;
    List<Tweet> result = new ArrayList<>();
    do {
      Optional<TweetSearchResponseV1> tweetSearchV1DTO = this.getRequestHelper().getRequestWithParameters(
          urlHelper.getSearchTweetFullArchiveUrl(envName), parameters, TweetSearchResponseV1.class);
      if (!tweetSearchV1DTO.isPresent()) {
        LOGGER.error("empty response on searchForTweetsArchive");
        break;
      }
      result.addAll(tweetSearchV1DTO.get().getResults());
      next = tweetSearchV1DTO.get().getNext();
      parameters.put(NEXT, next);
    } while (next != null);
    return result;
  }

  @Override
  public Future<Response> startFilteredStream(Consumer<Tweet> consumer) {
    String url = this.urlHelper.getFilteredStreamUrl();
    return this.requestHelperV2.getAsyncRequest(url, consumer);
  }

  @Override
  public Future<Response> startFilteredStream(IAPIEventListener listener) {
    String url = this.urlHelper.getFilteredStreamUrl();
    return this.requestHelperV2.getAsyncRequest(url, listener);
  }

  @Override
  public boolean stopFilteredStream(Future<Response> response) {
    try {
      if (response.get() == null) {
        return false;
      }
      response.get().getStream().close();
      return true;
    } catch (IOException | InterruptedException | ExecutionException e) {
      LOGGER.error("Couldn't stopFilteredstream ", e);
      Thread.currentThread().interrupt();
    }
    return false;
  }

  @Override
  public List<StreamRule> retrieveFilteredStreamRules() {
    String      url    = this.urlHelper.getFilteredStreamRulesUrl();
    StreamRules result = this.requestHelperV2.getRequest(url, StreamRules.class).orElseThrow(NoSuchElementException::new);
    return result.getData();
  }

  @Override
  public StreamRule addFilteredStreamRule(String value, String tag) {
    String     url  = this.urlHelper.getFilteredStreamRulesUrl();
    StreamRule rule = StreamRule.builder().value(value).tag(tag).build();
    try {
      String      body   = "{\"add\": [" + TwitterClient.OBJECT_MAPPER.writeValueAsString(rule) + "]}";
      StreamRules result = this.requestHelperV2.postRequest(url, body, StreamRules.class).orElseThrow(NoSuchElementException::new);
      if (result.getData() == null || result.getData().isEmpty()) {
        throw new IllegalArgumentException();
      }
      return result.getData().get(0);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException();
    }
  }

  @Override
  public StreamMeta deleteFilteredStreamRule(String ruleValue) {
    String      url    = this.urlHelper.getFilteredStreamRulesUrl();
    String      body   = "{\"delete\": {\"values\": [\"" + ruleValue + "\"]}}";
    StreamRules result = this.requestHelperV2.postRequest(url, body, StreamRules.class).orElseThrow(NoSuchElementException::new);
    return result.getMeta();
  }

  @Override
  public StreamMeta deleteFilteredStreamRuletag(String ruleTag) {
    String      url    = this.urlHelper.getFilteredStreamRulesUrl();
    String      body   = "{\"delete\": {\"ids\": [\"" + ruleTag + "\"]}}";
    StreamRules result = this.requestHelperV2.postRequest(url, body, StreamRules.class).orElseThrow(NoSuchElementException::new);
    return result.getMeta();
  }

  @Override
  public Future<Response> startSampledStream(Consumer<Tweet> consumer) {
    String url = this.urlHelper.getSampledStreamUrl();
    return this.requestHelperV2.getAsyncRequest(url, consumer);
  }

  @Override
  public Future<Response> startSampledStream(IAPIEventListener listener) {
    String url = this.urlHelper.getSampledStreamUrl();
    return this.requestHelperV2.getAsyncRequest(url, listener);
  }

  @Override
  public TweetList getUserTimeline(final String userId) {
    return this.getUserTimeline(userId, AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public TweetList getUserTimeline(String userId, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    String url = this.urlHelper.getUserTimelineUrl(userId);
    return this.getRequestHelperV2().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public TweetList getUserMentions(final String userId) {
    return this.getUserMentions(userId, AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public TweetList getUserMentions(final String userId, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    String url = this.urlHelper.getUserMentionsUrl(userId);
    return this.getRequestHelperV2().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<TweetV1> readTwitterDataFile(File file) throws IOException {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(TweetV1.class, new TweetV1Deserializer());
    ObjectMapper customObjectMapper = new ObjectMapper();
    customObjectMapper.registerModule(module);

    List<TweetV1> result = new ArrayList<>();
    if (!file.exists()) {
      LOGGER.error("file not found at : " + file.toURI().toString());
    } else {
      result = Arrays.asList(customObjectMapper.readValue(file, TweetV1[].class));
    }
    return result;
  }

  @Override
  public String getBearerToken() {
    return requestHelperV2.getBearerToken();
  }

  @Override
  public RequestToken getOauth1Token() {
    return this.getOauth1Token(null);
  }

  @Override
  public RequestToken getOauth1Token(String oauthCallback) {
    String              url        = URLHelper.GET_OAUTH1_TOKEN_URL;
    Map<String, String> parameters = new HashMap<>();
    if (oauthCallback != null) {
      parameters.put("oauth_callback", oauthCallback);
    }
    String       stringResponse = this.requestHelperV1.postRequest(url, parameters, String.class).orElseThrow(NoSuchElementException::new);
    RequestToken requestToken   = new RequestToken(stringResponse);
    LOGGER.info("Open the following URL to grant access to your account:");
    LOGGER.info("https://twitter.com/oauth/authenticate?oauth_token=" + requestToken.getOauthToken());
    return requestToken;
  }

  @Override
  public RequestToken getOAuth1AccessToken(RequestToken requestToken, String pinCode) {
    String              url        = URLHelper.GET_OAUTH1_ACCESS_TOKEN_URL;
    Map<String, String> parameters = new HashMap<>();
    parameters.put("oauth_verifier", pinCode);
    parameters.put("oauth_token", requestToken.getOauthToken());
    String stringResponse = this.requestHelperV1.postRequest(url, parameters, String.class).orElseThrow(NoSuchElementException::new);
    return new RequestToken(stringResponse);
  }

  @Override
  public UploadMediaResponse uploadMedia(String mediaName, byte[] data, MediaCategory mediaCategory) {
    String url = urlHelper.getUploadMediaUrl(mediaCategory);
    return this.requestHelperV1.uploadMedia(url, mediaName, data, UploadMediaResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UploadMediaResponse uploadMedia(File imageFile, MediaCategory mediaCategory) {
    String url = urlHelper.getUploadMediaUrl(mediaCategory);
    return this.requestHelperV1.uploadMedia(url, imageFile, UploadMediaResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public CollectionsResponse collectionsCreate(String name, String description, String collectionUrl, TimeLineOrder timeLineOrder) {
    String              url        = this.getUrlHelper().getCollectionsCreateUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put("name", name);
    parameters.put("description", description);
    parameters.put("url", collectionUrl);
    if (timeLineOrder != null) {
      parameters.put("timeline_order", timeLineOrder.value());
    }
    return this.requestHelperV1.postRequest(url, parameters, CollectionsResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public CollectionsResponse collectionsCurate(String collectionId, List<String> tweetIds) {
    String url = this.getUrlHelper().getCollectionsCurateUrl();

    // Can only curate 100 tweets at a time - so chunk if tweetIds is larger
    AtomicInteger index = new AtomicInteger(0);
    Stream<List<String>> chunked = tweetIds.stream()
                                           .collect(Collectors.groupingBy(x -> index.getAndIncrement() / URLHelper.MAX_LOOKUP))
                                           .entrySet().stream()
                                           .sorted(Map.Entry.comparingByKey())
                                           .map(Map.Entry::getValue);
    return chunked
        .map(
            chunk -> {
              String json = String.format("{\"id\": \"%s\",\"changes\": [", collectionId);
              json += chunk
                  .stream()
                  .map(tweetId -> String.format("{ \"op\": \"add\", \"tweet_id\": \"%s\"}", tweetId))
                  .collect(Collectors.joining(", "));
              json += "]}";
              return this.requestHelperV1.postRequestWithBodyJson(url, Collections.emptyMap(), json, CollectionsResponse.class)
                                         .orElseThrow(NoSuchElementException::new);
            })
        .filter(CollectionsResponse::hasErrors) // any errors? If so return first chunk of errors
        .findFirst()
        .orElse(new CollectionsResponse()); // success - no errors
  }

  @Override
  public CollectionsResponse collectionsDestroy(String collectionId) {
    String url = this.getUrlHelper().getCollectionsDestroyUrl(collectionId);
    return this.requestHelperV1.postRequest(url, Collections.emptyMap(), CollectionsResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<DirectMessage> getDmList() {
    return this.getDmList(Integer.MAX_VALUE);
  }

  @Override
  public List<DirectMessage> getDmList(int count) {
    List<DirectMessage> result   = new ArrayList<>();
    int                 maxCount = 50;
    String              url      = this.getUrlHelper().getDMListUrl(maxCount);
    DmListAnswer        dmListAnswer;
    do {
      dmListAnswer = this.requestHelperV1.getRequest(url, DmListAnswer.class).orElseThrow(NoSuchElementException::new);
      result.addAll(dmListAnswer.getDirectMessages());
      url = this.getUrlHelper().getDMListUrl(maxCount) + "&" + CURSOR + "=" + dmListAnswer.getNextCursor();
    }
    while (dmListAnswer.getNextCursor() != null && result.size() < count);
    if (count == Integer.MAX_VALUE) {
      return result;
    }
    return result.subList(0, count); // to fix the API bug which is not giving the right count
  }

  @Override
  public DirectMessage getDm(String dmId) {
    String  url    = urlHelper.getDmUrl(dmId);
    DmEvent result = this.getRequestHelper().getRequest(url, DmEvent.class).orElseThrow(NoSuchElementException::new);
    return result.getEvent();
  }

  @Override
  public DmEvent postDm(final String text, final String userId) {
    String url = urlHelper.getPostDmUrl();
    try {
      String body = TwitterClient.OBJECT_MAPPER.writeValueAsString(
          DmEvent.builder().event(new DirectMessage(text, userId)).build());
      return this.getRequestHelperV1().postRequestWithBodyJson(url, null, body, DmEvent.class).orElseThrow(NoSuchElementException::new);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e.getStackTrace());
    }
    return null;
  }

  @Override
  public CollectionsResponse collectionsEntries(final String collectionId, int count, String maxPosition, String minPosition) {
    String              url        = this.getUrlHelper().getCollectionsEntriesUrl(collectionId);
    Map<String, String> parameters = new HashMap<>();
    if (count > 0) {
      parameters.put("count", Integer.toString(count));
    }
    if (maxPosition != null) {
      parameters.put("max_position", maxPosition);
    }
    if (minPosition != null) {
      parameters.put("min_position", minPosition);
    }
    return getRequestHelper().getRequestWithParameters(url, parameters, CollectionsResponse.class).orElseThrow(NoSuchElementException::new);
  }

  public static TwitterCredentials getAuthentication() {
    String credentialPath = System.getProperty("twitter.credentials.file.path");
    if (credentialPath != null) {
      return getAuthentication(new File(credentialPath));
    } else {
      return getAuthentication(Paths.get(""));
    }
  }

  public static TwitterCredentials getAuthentication(final Path pathToScan, final String... validNames) {
    if (pathToScan.toFile().isFile()) {
      return getAuthentication(pathToScan.toFile());
    } else {
      String[] namesToCheck = validNames != null && validNames.length > 0 ? validNames : DEFAULT_VALID_CREDENTIALS_FILE_NAMES;
      for (Path currentPath = pathToScan; currentPath != null; currentPath = currentPath.getParent()) {
        for (String name : namesToCheck) {
          Path file = currentPath.resolve(name);
          if (Files.isRegularFile(file)) {
            return getAuthentication(file.toFile());
          }
        }
      }
    }
    return null;
  }

  public static TwitterCredentials getAuthentication(File twitterCredentialsFile) {
    try {
      TwitterCredentials twitterCredentials = TwitterClient.OBJECT_MAPPER.readValue(twitterCredentialsFile, TwitterCredentials.class);
      if (twitterCredentials.getAccessToken() == null) {
        LOGGER.error("Access token is null in twitter-credentials.json");
      }
      if (twitterCredentials.getAccessTokenSecret() == null) {
        LOGGER.error("Secret token is null in twitter-credentials.json");
      }
      if (twitterCredentials.getApiKey() == null) {
        LOGGER.error("Consumer key is null in twitter-credentials.json");
      }
      if (twitterCredentials.getApiSecretKey() == null) {
        LOGGER.error("Consumer secret is null in twitter-credentials.json");
      }
      return twitterCredentials;
    } catch (Exception e) {
      LOGGER.error("twitter credentials json file error in path " + twitterCredentialsFile.getAbsolutePath()
                   + ". Use program argument -Dtwitter.credentials.file.path=/my/path/to/json . ", e);
      return null;
    }
  }

  private AbstractRequestHelper getRequestHelper() {
    if (this.requestHelperV1.getTwitterCredentials().getAccessToken() != null
        && this.requestHelperV1.getTwitterCredentials().getAccessTokenSecret() != null) {
      return this.requestHelperV1;
    } else {
      return this.requestHelperV2;
    }
  }

  public String getUserIdFromAccessToken() {
    String accessToken = this.twitterCredentials.getAccessToken();
    if (accessToken == null
        || accessToken.isEmpty()
        || !accessToken.contains("-")) {
      LOGGER.error("access token null, empty or incorrect");
      throw new IllegalArgumentException();
    }
    return accessToken.substring(0, accessToken.indexOf("-"));
  }
}