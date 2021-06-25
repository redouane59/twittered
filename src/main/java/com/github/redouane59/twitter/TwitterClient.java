package com.github.redouane59.twitter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.redouane59.RelationType;
import com.github.redouane59.twitter.dto.collections.CollectionsResponse;
import com.github.redouane59.twitter.dto.collections.TimeLineOrder;
import com.github.redouane59.twitter.dto.dm.DirectMessage;
import com.github.redouane59.twitter.dto.dm.DmEvent;
import com.github.redouane59.twitter.dto.dm.DmListAnswer;
import com.github.redouane59.twitter.dto.endpoints.AdditionnalParameters;
import com.github.redouane59.twitter.dto.getrelationship.IdList;
import com.github.redouane59.twitter.dto.getrelationship.RelationshipObjectResponse;
import com.github.redouane59.twitter.dto.others.BlockResponse;
import com.github.redouane59.twitter.dto.others.RateLimitStatus;
import com.github.redouane59.twitter.dto.others.RequestToken;
import com.github.redouane59.twitter.dto.stream.StreamRules;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import com.github.redouane59.twitter.dto.tweet.HiddenResponse;
import com.github.redouane59.twitter.dto.tweet.HiddenResponse.HiddenData;
import com.github.redouane59.twitter.dto.tweet.LikeResponse;
import com.github.redouane59.twitter.dto.tweet.MediaCategory;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetCountsList;
import com.github.redouane59.twitter.dto.tweet.TweetListV2;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponse;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponseV1;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponseV2;
import com.github.redouane59.twitter.dto.tweet.TweetV1;
import com.github.redouane59.twitter.dto.tweet.TweetV1Deserializer;
import com.github.redouane59.twitter.dto.tweet.TweetV2;
import com.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import com.github.redouane59.twitter.dto.tweet.UploadMediaResponse;
import com.github.redouane59.twitter.dto.user.FollowBody;
import com.github.redouane59.twitter.dto.user.FollowResponse;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserListV2;
import com.github.redouane59.twitter.dto.user.UserV2;
import com.github.redouane59.twitter.dto.user.UserV2.UserData;
import com.github.redouane59.twitter.helpers.AbstractRequestHelper;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitter.helpers.RequestHelper;
import com.github.redouane59.twitter.helpers.RequestHelperV2;
import com.github.redouane59.twitter.helpers.URLHelper;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
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
  private static final String             IDS                                  = "ids";
  private static final String             QUERY                                = "query";
  private static final String             GRANULARITY                          = "granularity";
  private static final String             SINCE_ID                             = "since_id";
  private static final String             UNTIL_ID                             = "until_id";
  private static final String             START_TIME                           = "start_time";
  private static final String             END_TIME                             = "end_time";
  private static final String             MAX_RESULTS                          = "max_results";
  private static final String             USERS                                = "users";
  private static final String             CURSOR                               = "cursor";
  private static final String             NEXT                                 = "next";
  private static final String             PAGINATION_TOKEN                     = "pagination_token";
  private static final String             NEXT_TOKEN                           = "next_token";
  private static final String             RETWEET_COUNT                        = "retweet_count";
  private static final String             RELATIONSHIP                         = "relationship";
  private static final String             FOLLOWING                            = "following";
  private static final String             FOLLOWED_BY                          = "followed_by";
  private static final String             SOURCE                               = "source";
  private static final String             NULL_OR_ID_NOT_FOUND_ERROR           = "response null or ids not found !";
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
    String     token  = null;
    List<User> result = new ArrayList<>();
    do {
      String urlWithCursor = url;
      if (token != null) {
        urlWithCursor = urlWithCursor + "&" + PAGINATION_TOKEN + "=" + token;
      }
      Optional<UserListV2> userListDTO = this.getRequestHelper().getRequest(urlWithCursor, UserListV2.class);
      if (!userListDTO.isPresent()) {
        break;
      }
      result.addAll(userListDTO.get().getData());
      token = userListDTO.get().getMeta().getNextToken();
    } while (token != null);
    return result;
  }

  private List<User> getUsersInfoByRelation(String userId, RelationType relationType) {
    String url = null;
    if (relationType == RelationType.FOLLOWER) {
      url = this.urlHelper.getFollowersUrl(userId);
    } else if (relationType == RelationType.FOLLOWING) {
      url = this.urlHelper.getFollowingUrl(userId);
    }
    return this.getUsersInfoByRelation(url);
  }

  @Override
  public List<User> getFollowers(String userId) {
    return this.getUsersInfoByRelation(userId, RelationType.FOLLOWER);
  }

  @Override
  public List<User> getFollowing(String userId) {
    return this.getUsersInfoByRelation(userId, RelationType.FOLLOWING);
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
  public UserListV2 getBlockedUsers() {
    String url = this.urlHelper.getBlockingUsersUrl(this.getUserIdFromAccessToken());
    return this.getRequestHelper().getRequest(url, UserListV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public User getUserFromUserId(String userId) {
    String url = this.getUrlHelper().getUserUrl(userId);
    return this.getRequestHelper().getRequest(url, UserV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserV2 getUserFromUserName(String userName) {
    String url = this.getUrlHelper().getUserUrlFromName(userName);
    return this.getRequestHelper().getRequest(url, UserV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<User> getUsersFromUserNames(List<String> userNames) {
    String url = this.getUrlHelper().getUsersUrlbyNames(userNames);
    List<UserData> result = this.getRequestHelper().getRequest(url, UserListV2.class)
                                .orElseThrow(NoSuchElementException::new).getData();
    return result.stream().map(userData -> UserV2.builder().data(userData).build()).collect(Collectors.toList());
  }

  @Override
  public List<User> getUsersFromUserIds(List<String> userIds) {
    String url = this.getUrlHelper().getUsersUrlbyIds(userIds);
    List<UserData> result = this.getRequestHelper().getRequest(url, UserListV2.class)
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
  public UserListV2 getLikingUsers(final String tweetId) {
    String url = this.getUrlHelper().getLikingUsersUrl(tweetId);
    return getRequestHelper().getRequest(url, UserListV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public TweetListV2 getLikedTweets(final String userId) {
    String url = this.getUrlHelper().getLikedTweetsUrl(userId);
    return getRequestHelper().getRequest(url, TweetListV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public TweetCountsList getTweetCounts(final String query) {
    return this.getTweetCounts(query, AdditionnalParameters.builder().build());
  }

  @Override
  public TweetCountsList getTweetCounts(final String query, AdditionnalParameters additionnalParameters) {
    String url = this.getUrlHelper().getTweetsCountsUrl();
    return this.getTweetCounts(url, query, additionnalParameters);
  }

  @Override
  public TweetCountsList getTweetCountsFullArchive(final String query) {
    return this.getTweetCountsFullArchive(query, AdditionnalParameters.builder().build());
  }

  @Override
  public TweetCountsList getTweetCountsFullArchive(final String query, AdditionnalParameters additionnalParameters) {
    String url = this.getUrlHelper().getTweetsCountsFullArchiveUrl();
    return this.getTweetCounts(url, query, additionnalParameters);
  }

  private TweetCountsList getTweetCounts(String url, final String query, AdditionnalParameters additionnalParameters) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put(QUERY, query);
    if (additionnalParameters.getGranularity() != null) {
      parameters.put(GRANULARITY, additionnalParameters.getGranularity());
    }
    if (additionnalParameters.getStartTime() != null) {
      parameters.put(START_TIME, ConverterHelper.getStringFromDateV2(additionnalParameters.getStartTime()));
    }
    if (additionnalParameters.getEndTime() != null) {
      parameters.put(END_TIME, ConverterHelper.getStringFromDateV2(additionnalParameters.getEndTime()));
    }
    if (additionnalParameters.getSinceId() != null) {
      parameters.put(SINCE_ID, additionnalParameters.getSinceId());
    }
    if (additionnalParameters.getUntilId() != null) {
      parameters.put(UNTIL_ID, additionnalParameters.getUntilId());
    }
    if (additionnalParameters.getNextToken() != null) {
      parameters.put(NEXT_TOKEN, additionnalParameters.getNextToken());
    }
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
    String url = this.getUrlHelper().getTweetUrl(tweetId);
    return this.getRequestHelper().getRequest(url, TweetV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<Tweet> getTweets(List<String> tweetIds) {
    String url = this.getUrlHelper().getTweetListUrl(tweetIds);
    List<TweetData> result = this.getRequestHelper().getRequest(url, TweetSearchResponseV2.class)
                                 .orElseThrow(NoSuchElementException::new).getData();
    return result.stream().map(tweetData -> TweetV2.builder().data(tweetData).build()).collect(Collectors.toList());
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
  public List<Tweet> searchForTweetsWithin7days(String query, LocalDateTime fromDate, LocalDateTime toDate) {
    int                 count      = 100;
    Map<String, String> parameters = new HashMap<>();
    parameters.put(QUERY, query);
    parameters.put(MAX_RESULTS, String.valueOf(count));
    if (fromDate != null) {
      parameters.put("start_time", ConverterHelper.getStringFromDateV2(fromDate));
    }
    if (toDate != null) {
      parameters.put("end_time", ConverterHelper.getStringFromDateV2(toDate));
    }
    parameters.put("tweet.fields", URLHelper.ALL_TWEET_FIELDS);
    String      next;
    List<Tweet> result = new ArrayList<>();
    do {
      Optional<TweetSearchResponseV2> tweetSearchV2DTO = this.getRequestHelper()
                                                             .getRequestWithParameters(URLHelper.SEARCH_TWEET_7_DAYS_URL,
                                                                                       parameters,
                                                                                       TweetSearchResponseV2.class);
      if (!tweetSearchV2DTO.isPresent() || tweetSearchV2DTO.get().getData() == null) {
        break;
      }
      result.addAll(tweetSearchV2DTO.get().getData());
      next = tweetSearchV2DTO.get().getMeta().getNextToken();
      parameters.put(NEXT_TOKEN, next);
    } while (next != null);
    return result;
  }

  @Override
  public List<Tweet> searchForTweetsWithin7days(String query) {
    return this.searchForTweetsWithin7days(query, null, null);
  }

  @Override
  public TweetSearchResponse searchForTweetsWithin7days(String query, int maxResult, String nextToken) {
    return this.searchForTweetsWithin7days(query, null, null, maxResult, nextToken);
  }

  private TweetSearchResponse searchForTweets(String query, LocalDateTime fromDate, LocalDateTime toDate, int maxResult,
                                              String nextToken, String searchUrl) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put(QUERY, query);
    parameters.put(MAX_RESULTS, String.valueOf(maxResult));
    if (fromDate != null) {
      parameters.put("start_time", ConverterHelper.getStringFromDateV2(fromDate));
    }
    if (toDate != null) {
      parameters.put("end_time", ConverterHelper.getStringFromDateV2(toDate));
    }
    if (nextToken != null) {
      parameters.put(NEXT_TOKEN, nextToken);
    }
    parameters.put("tweet.fields", URLHelper.ALL_TWEET_FIELDS);
    Optional<TweetSearchResponseV2> tweetSearchV2DTO = this.requestHelperV2.getRequestWithParameters(searchUrl,
                                                                                                     parameters, TweetSearchResponseV2.class);
    if (!tweetSearchV2DTO.isPresent() || tweetSearchV2DTO.get().getData() == null) {
      return new TweetSearchResponse(new ArrayList<>(), null);
    }
    List<Tweet> result = new ArrayList<>(tweetSearchV2DTO.get().getData());
    return new TweetSearchResponse(result, tweetSearchV2DTO.get().getMeta().getNextToken());
  }

  @Override
  public TweetSearchResponse searchForTweetsWithin7days(String query, LocalDateTime fromDate, LocalDateTime toDate,
                                                        int maxResult, String nextToken) {
    return this.searchForTweets(query, fromDate, toDate, maxResult, nextToken, URLHelper.SEARCH_TWEET_7_DAYS_URL);
  }

  @Override
  public TweetSearchResponse searchForTweetsFullArchive(final String query, final LocalDateTime fromDate,
                                                        final LocalDateTime toDate, final int maxResult, final String nextToken) {
    return this.searchForTweets(query, fromDate, toDate, maxResult, nextToken, URLHelper.SEARCH_TWEET_FULL_ARCHIVE_URL);
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
    parameters.put(MAX_RESULTS, String.valueOf(count));
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
  public List<Tweet> getUserTimeline(final String userId, int nbTweets) {
    return this.getUserTimeline(userId, nbTweets, null, null, null, null);
  }

  @Override
  public List<Tweet> getUserTimeline(String userId, int nbTweets, LocalDateTime startTime, LocalDateTime endTime, String sinceId, String untilId) {
    String      token          = null;
    List<Tweet> result         = new ArrayList<>();
    int         apiResultLimit = 100;
    int         missingTweets  = nbTweets;
    do {
      String url = this.urlHelper.getUserTimelineUrl(userId, Math.min(apiResultLimit, missingTweets), startTime, endTime, sinceId, untilId);
      if (token != null) {
        url = url + "&" + PAGINATION_TOKEN + "=" + token;
      }
      Optional<TweetSearchResponseV2> tweetListDTO = this.getRequestHelperV2().getRequest(url, TweetSearchResponseV2.class);
      if (!tweetListDTO.isPresent() || tweetListDTO.get().getData() == null) {
        break;
      }
      result.addAll(tweetListDTO.get().getData());
      token = tweetListDTO.get().getMeta().getNextToken();
      missingTweets -= apiResultLimit;
    }
    while (token != null && missingTweets > 0);
    return result;
  }

  @Override
  public List<Tweet> getUserMentions(final String userId, final int nbTweets) {
    return this.getUserMentions(userId, nbTweets, null, null, null, null);
  }

  @Override
  public List<Tweet> getUserMentions(final String userId,
                                     final int nbTweets,
                                     final LocalDateTime startTime,
                                     final LocalDateTime endTime,
                                     final String sinceId,
                                     final String untilId) {
    String      token          = null;
    List<Tweet> result         = new ArrayList<>();
    int         apiResultLimit = 100;
    int         missingTweets  = nbTweets;
    do {
      String url = this.urlHelper.getUserMentionsUrl(userId, Math.min(apiResultLimit, missingTweets), startTime, endTime, sinceId, untilId);
      if (token != null) {
        url = url + "&" + PAGINATION_TOKEN + "=" + token;
      }
      Optional<TweetSearchResponseV2> tweetListDTO = this.getRequestHelperV2().getRequest(url, TweetSearchResponseV2.class);
      if (!tweetListDTO.isPresent() || tweetListDTO.get().getData() == null) {
        break;
      }
      result.addAll(tweetListDTO.get().getData());
      token = tweetListDTO.get().getMeta().getNextToken();
      missingTweets -= apiResultLimit;
    }
    while (token != null && missingTweets > 0);
    return result;
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