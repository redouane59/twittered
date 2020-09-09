package com.github.redouane59.twitter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.redouane59.RelationType;
import com.github.redouane59.twitter.dto.getrelationship.IdList;
import com.github.redouane59.twitter.dto.getrelationship.RelationshipObjectResponse;
import com.github.redouane59.twitter.dto.getrelationship.UserList;
import com.github.redouane59.twitter.dto.others.BearerToken;
import com.github.redouane59.twitter.dto.others.RateLimitStatus;
import com.github.redouane59.twitter.dto.others.RequestToken;
import com.github.redouane59.twitter.dto.stream.StreamRules;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import com.github.redouane59.twitter.dto.tweet.HiddenResponse;
import com.github.redouane59.twitter.dto.tweet.HiddenResponse.HiddenData;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetListV2;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponse;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponseV1;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponseV2;
import com.github.redouane59.twitter.dto.tweet.TweetV1;
import com.github.redouane59.twitter.dto.tweet.TweetV1Deserializer;
import com.github.redouane59.twitter.dto.tweet.TweetV2;
import com.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserListv2;
import com.github.redouane59.twitter.dto.user.UserV1;
import com.github.redouane59.twitter.dto.user.UserV2;
import com.github.redouane59.twitter.dto.user.UserV2.UserData;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitter.helpers.RequestHelper;
import com.github.redouane59.twitter.helpers.RequestHelperV2;
import com.github.redouane59.twitter.helpers.URLHelper;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

@Getter
@Setter
@Slf4j
public class TwitterClient implements ITwitterClientV1, ITwitterClientV2, ITwitterClientArchive {

  public static        TwitterCredentials TWITTER_CREDENTIALS;
  public static final  ObjectMapper       OBJECT_MAPPER              = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL);
  private              URLHelper          urlHelper                  = new URLHelper();
  private              RequestHelper      requestHelper;
  private              RequestHelperV2    requestHelperV2;
  private static final String             IDS                        = "ids";
  private static final String             USERS                      = "users";
  private static final String             CURSOR                     = "cursor";
  private static final String             NEXT                       = "next";
  private static final String             RETWEET_COUNT              = "retweet_count";
  private static final String             RELATIONSHIP               = "relationship";
  private static final String             FOLLOWING                  = "following";
  private static final String             FOLLOWED_BY                = "followed_by";
  private static final String             SOURCE                     = "source";
  private final static String             NULL_OR_ID_NOT_FOUND_ERROR = "response null or ids not found !";
  private static final String             NEXT_CURSOR                = "next_cursor";
  private static final String
                                          ALL_TWEET_FIELDS           =
      "attachments,author_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,source,text,withheld,context_annotations,conversation_id";

  public TwitterClient() {
    TWITTER_CREDENTIALS = getAuthentication();
    requestHelper       = new RequestHelper();
    requestHelperV2     = new RequestHelperV2(this.getBearerToken());
  }

  public TwitterClient(TwitterCredentials credentials) {
    TWITTER_CREDENTIALS = credentials;
    requestHelper       = new RequestHelper();
    requestHelperV2     = new RequestHelperV2(this.getBearerToken());
  }

  // can manage up to 5000 results / call . Max 15 calls / 15min ==> 75.000 results max. / 15min
  private List<String> getUserIdsByRelation(String url) {
    String       cursor = "-1";
    List<String> result = new ArrayList<>();
    do {
      String           urlWithCursor  = url + "&" + CURSOR + "=" + cursor;
      Optional<IdList> idListResponse = this.requestHelperV2.getRequest(urlWithCursor, IdList.class);
      if (idListResponse.isEmpty()) {
        break;
      }
      result.addAll(idListResponse.get().getIds());
      cursor = idListResponse.get().getNextCursor();
    }
    while (!cursor.equals("0"));
    return result;
  }

  private Set<String> getUserIdsByRelationSet(String url) {
    String      cursor = "-1";
    Set<String> result = new HashSet<>();
    do {
      String           urlWithCursor  = url + "&" + CURSOR + "=" + cursor;
      Optional<IdList> idListResponse = this.requestHelperV2.getRequest(urlWithCursor, IdList.class);
      if (idListResponse.isEmpty()) {
        break;
      }
      result.addAll(idListResponse.get().getIds());
      cursor = idListResponse.get().getNextCursor();
    }
    while (!cursor.equals("0"));
    return result;
  }

  // can manage up to 200 results/call . Max 15 calls/15min ==> 3.000 results max./15min
  private List<User> getUsersInfoByRelation(String url) {
    String     cursor = "-1";
    List<User> result = new ArrayList<>();
    do {
      String             urlWithCursor = url + "&" + CURSOR + "=" + cursor;
      Optional<UserList> userListDTO   = this.requestHelperV2.getRequest(urlWithCursor, UserList.class);
      if (userListDTO.isEmpty()) {
        break;
      }
      result.addAll(userListDTO.get().getUsers());
      cursor = userListDTO.get().getNextCursor();
    }
    while (!cursor.equals("0"));
    return result;
  }

  private List<String> getUserIdsByRelation(String userId, RelationType relationType) {
    String url = null;
    if (relationType == RelationType.FOLLOWER) {
      url = this.urlHelper.getFollowerIdsUrl(userId);
    } else if (relationType == RelationType.FOLLOWING) {
      url = this.urlHelper.getFollowingIdsUrl(userId);
    }
    return this.getUserIdsByRelation(url);
  }

  private List<User> getUsersInfoByRelation(String userId, RelationType relationType) {
    String url = null;
    if (relationType == RelationType.FOLLOWER) {
      url = this.urlHelper.getFollowerUsersUrl(userId);
    } else if (relationType == RelationType.FOLLOWING) {
      url = this.urlHelper.getFollowingUsersUrl(userId);
    }
    return this.getUsersInfoByRelation(url);
  }

  public Set<String> getUserFollowersIds(String userId) {
    return this.getUserIdsByRelationSet(this.urlHelper.getFollowerIdsUrl(userId));
  }

  @Override
  public List<String> getFollowerIds(String userId) {
    return this.getUserIdsByRelation(userId, RelationType.FOLLOWER);
  }

  @Override
  public List<User> getFollowerUsers(String userId) {
    return this.getUsersInfoByRelation(userId, RelationType.FOLLOWER);
  }

  @Override
  public List<String> getFollowingIds(String userId) {
    return this.getUserIdsByRelation(userId, RelationType.FOLLOWING);
  }

  @Override
  public List<User> getFollowingUsers(String userId) {
    return this.getUsersInfoByRelation(userId, RelationType.FOLLOWING);
  }

  @Override
  public RelationType getRelationType(String userId1, String userId2) {
    String url = this.urlHelper.getFriendshipUrl(userId1, userId2);
    RelationshipObjectResponse relationshipDTO = this.requestHelperV2
        .getRequest(url, RelationshipObjectResponse.class).orElseThrow(NoSuchElementException::new);
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
  public User follow(String userId) {
    String url = this.urlHelper.getFollowUrl(userId);
    return this.requestHelper
        .postRequest(url, new HashMap<>(), UserV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public User unfollow(String userId) {
    String url = this.urlHelper.getUnfollowUrl(userId);
    return this.requestHelper
        .postRequest(url, new HashMap<>(), UserV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public User unfollowByName(String userName) {
    String url = this.urlHelper.getUnfollowByUsernameUrl(userName);
    return this.requestHelper
        .postRequest(url, new HashMap<>(), UserV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public User getUserFromUserId(String userId) {
    String url = this.getUrlHelper().getUserUrl(userId);
    return this.requestHelperV2.getRequest(url, UserV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserV2 getUserFromUserName(String userName) {
    String url = this.getUrlHelper().getUserUrlFromName(userName);
    return this.requestHelperV2.getRequest(url, UserV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<User> getUsersFromUserNames(List<String> userNames) {
    String         url    = this.getUrlHelper().getUsersUrlbyNames(userNames);
    List<UserData> result = this.requestHelperV2.getRequest(url, UserListv2.class).orElseThrow(NoSuchElementException::new).getData();
    return result.stream().map(userData -> UserV2.builder().data(userData).build()).collect(Collectors.toList());
  }


  @Override
  public List<User> getUsersFromUserIds(List<String> userIds) {
    String         url    = this.getUrlHelper().getUsersUrlbyIds(userIds);
    List<UserData> result = this.requestHelperV2.getRequest(url, UserListv2.class).orElseThrow(NoSuchElementException::new).getData();
    return result.stream().map(userData -> UserV2.builder().data(userData).build()).collect(Collectors.toList());
  }

  @Override
  public RateLimitStatus getRateLimitStatus() {
    String url = URLHelper.RATE_LIMIT_URL;
    return this.requestHelperV2.getRequest(url, RateLimitStatus.class).orElseThrow(NoSuchElementException::new);
  }


  @Override
  public Tweet likeTweet(String tweetId) {
    String url = this.getUrlHelper().getLikeUrl(tweetId);
    return this.requestHelper.postRequest(url, new HashMap<>(), TweetV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet unlikeTweet(String tweetId) {
    String url = this.getUrlHelper().getUnlikeUrl(tweetId);
    return this.requestHelper.postRequest(url, new HashMap<>(), TweetV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet retweetTweet(String tweetId) {
    String url = this.getUrlHelper().getRetweetTweetUrl(tweetId);
    return this.requestHelper.postRequest(url, new HashMap<>(), TweetV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet postTweet(String text) {
    String              url        = this.getUrlHelper().getPostTweetUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put("status", text);
    return this.getRequestHelper().postRequest(url, parameters, TweetV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet deleteTweet(String tweetId) {
    String              url        = this.getUrlHelper().getdeleteTweetUrl(tweetId);
    Map<String, String> parameters = new HashMap<>();
    return this.getRequestHelper().postRequest(url, parameters, TweetV1.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Tweet getTweet(String tweetId) {
    String url = this.getUrlHelper().getTweetUrl(tweetId);
    return this.requestHelperV2.getRequest(url, TweetV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<Tweet> getTweets(List<String> tweetIds) {
    String          url    = this.getUrlHelper().getTweetListUrl(tweetIds);
    List<TweetData> result = this.requestHelperV2.getRequest(url, TweetListV2.class).orElseThrow(NoSuchElementException::new).getData();
    return result.stream().map(tweetData -> TweetV2.builder().data(tweetData).build()).collect(Collectors.toList());
  }

  @Override
  public boolean hideReply(final String tweetId, final boolean hide) {
    String url = this.getUrlHelper().getHideReplyUrl(tweetId);
    try {
      String body = TwitterClient.OBJECT_MAPPER.writeValueAsString(new HiddenData(hide));
      HiddenResponse response = this.requestHelper.putRequest(url, body, HiddenResponse.class)
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
      result = List.of(this.requestHelperV2.getRequest(this.getUrlHelper().getFavoriteTweetsUrl(userId, maxId), TweetV1[].class)
                                           .orElseThrow(NoSuchElementException::new));
      if (result.size() == 0) {
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
    parameters.put("query", query);
    parameters.put("max_results", String.valueOf(count));
    if (fromDate != null) {
      parameters.put("start_time", ConverterHelper.getStringFromDateV2(fromDate));
    }
    if (toDate != null) {
      parameters.put("end_time", ConverterHelper.getStringFromDateV2(toDate));
    }
    parameters.put("tweet.fields", ALL_TWEET_FIELDS);
    String      next;
    List<Tweet> result = new ArrayList<>();
    do {
      Optional<TweetSearchResponseV2> tweetSearchV2DTO = this.requestHelperV2.getRequestWithParameters(
          URLHelper.SEARCH_TWEET_7_DAYS_URL, parameters, TweetSearchResponseV2.class);
      if (tweetSearchV2DTO.isEmpty() || tweetSearchV2DTO.get().getData() == null) {
        LOGGER.warn("empty response on searchForTweetsWithin7days");
        break;
      }
      result.addAll(tweetSearchV2DTO.get().getData());
      next = tweetSearchV2DTO.get().getMeta().getNextToken();
      parameters.put("next_token", next);
    }
    while (next != null);
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

  @Override
  public TweetSearchResponse searchForTweetsWithin7days(String query, LocalDateTime fromDate, LocalDateTime toDate, int maxResult, String nextToken) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("query", query);
    parameters.put("max_results", String.valueOf(maxResult));
    if (fromDate != null) {
      parameters.put("start_time", ConverterHelper.getStringFromDateV2(fromDate));
    }
    if (toDate != null) {
      parameters.put("end_time", ConverterHelper.getStringFromDateV2(toDate));
    }
    if (nextToken != null) {
      parameters.put("next_token", nextToken);
    }
    parameters.put("tweet.fields", ALL_TWEET_FIELDS);
    Optional<TweetSearchResponseV2> tweetSearchV2DTO = this.requestHelperV2.getRequestWithParameters(
        URLHelper.SEARCH_TWEET_7_DAYS_URL, parameters, TweetSearchResponseV2.class);
    if (tweetSearchV2DTO.isEmpty() || tweetSearchV2DTO.get().getData() == null) {
      LOGGER.warn("empty response on searchForTweetsWithin7days");
      return new TweetSearchResponse(new ArrayList<>(), null);
    }
    List<Tweet> result = new ArrayList<>(tweetSearchV2DTO.get().getData());
    return new TweetSearchResponse(result, tweetSearchV2DTO.get().getMeta().getNextToken());
  }

  @Override
  public List<Tweet> searchForTweetsWithin30days(String query, LocalDateTime fromDate, LocalDateTime toDate, String envName) {
    int                 count      = 100;
    Map<String, String> parameters = new HashMap<>();
    parameters.put("query", query);
    parameters.put("maxResults", String.valueOf(count));
    parameters.put("fromDate", ConverterHelper.getStringFromDate(fromDate));
    parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
    String      next;
    List<Tweet> result = new ArrayList<>();
    do {
      Optional<TweetSearchResponseV1> tweetSearchV1DTO = this.requestHelperV2.getRequestWithParameters(
          urlHelper.getSearchTweet30DaysUrl(envName), parameters, TweetSearchResponseV1.class);
      if (tweetSearchV1DTO.isEmpty() || tweetSearchV1DTO.get().getResults() == null) {
        LOGGER.warn("empty response on searchForTweetsWithin30days");
        break;
      }
      result.addAll(tweetSearchV1DTO.get().getResults());
      next = tweetSearchV1DTO.get().getNext();
      parameters.put(NEXT, next);
    }
    while (next != null);
    return result;
  }

  @Override
  public List<Tweet> searchForTweetsArchive(String query, LocalDateTime fromDate, LocalDateTime toDate, String envName) {
    int                 count      = 100;
    Map<String, String> parameters = new HashMap<>();
    parameters.put("query", query);
    parameters.put("max_results", String.valueOf(count));
    parameters.put("fromDate", ConverterHelper.getStringFromDate(fromDate));
    parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
    String      next;
    List<Tweet> result = new ArrayList<>();
    do {
      Optional<TweetSearchResponseV1> tweetSearchV1DTO = this.requestHelperV2.getRequestWithParameters(
          urlHelper.getSearchTweetFullArchiveUrl(envName), parameters, TweetSearchResponseV1.class);
      if (tweetSearchV1DTO.isEmpty()) {
        LOGGER.error("empty response on searchForTweetsArchive");
        break;
      }
      result.addAll(tweetSearchV1DTO.get().getResults());
      next = tweetSearchV1DTO.get().getNext();
      parameters.put(NEXT, next);
    }
    while (next != null);
    return result;
  }

  @Override
  public void startFilteredStream(Consumer<Tweet> consumer) {
    String url = this.urlHelper.getFilteredStreamUrl();
    this.requestHelperV2.getAsyncRequest(url, consumer);
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
      if (result.getData() == null || result.getData().size() == 0) {
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
  public void startSampledStream(Consumer<Tweet> consumer) {
    String url = this.urlHelper.getSampledStreamUrl();
    this.requestHelperV2.getAsyncRequest(url, consumer);
  }

  @Override
  public List<Tweet> getMentionsTimeline() {
    int    maxCount = 200;
    String url      = this.urlHelper.getMentionsTimelineUrl(maxCount);
    return List.of(this.requestHelper.getRequest(url, TweetV1[].class).orElseThrow(NoSuchElementException::new));
  }

  @Override
  public List<Tweet> getMentionsTimeline(int count, String maxId) {
    String url = this.urlHelper.getMentionsTimelineUrl(count, maxId);
    return List.of(this.requestHelper.getRequest(url, TweetV1[].class).orElseThrow(NoSuchElementException::new));
  }

  @Override
  public List<Tweet> getUserTimeline(final String userId) {
    int    maxCount = 200;
    String url      = this.urlHelper.getUserTimelineUrl(userId, maxCount);
    return List.of(this.requestHelper.getRequest(url, TweetV1[].class).orElseThrow(NoSuchElementException::new));
  }

  @Override
  public List<Tweet> getUserTimeline(final String userId, final int count, final String maxId) {
    String url = this.urlHelper.getUserTimelineUrl(userId, count, maxId);
    return List.of(this.requestHelper.getRequest(url, TweetV1[].class).orElseThrow(NoSuchElementException::new));
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
      result = List.of(customObjectMapper.readValue(file, TweetV1[].class));
    }
    return result;
  }

  @Override
  public String getBearerToken() {
    String url = URLHelper.GET_BEARER_TOKEN_URL;
    String valueToCrypt = TWITTER_CREDENTIALS.getApiKey()
                          + ":" + TWITTER_CREDENTIALS.getApiSecretKey();
    String              cryptedValue = Base64.getEncoder().encodeToString(valueToCrypt.getBytes());
    Map<String, String> params       = new HashMap<>();
    params.put("Authorization", "Basic " + cryptedValue);
    params.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    String body = "grant_type=client_credentials";
    BearerToken result = requestHelperV2
        .postRequestWithHeader(url, params, body, BearerToken.class).orElseThrow(NoSuchElementException::new);
    return result.getAccessToken();
  }

  @Override
  public RequestToken getOauth1Token() {
    String url = URLHelper.GET_OAUTH1_TOKEN_URL;
    String stringResponse = this.requestHelper.postRequest(url, new HashMap<>(), String.class)
                                              .orElseThrow(NoSuchElementException::new);
    List<NameValuePair> params = null;
    try {
      params = URLEncodedUtils.parse(new URI("twitter.com?" + stringResponse), StandardCharsets.UTF_8.name());
    } catch (URISyntaxException e) {
      LOGGER.error(e.getMessage());
    }
    RequestToken requestToken = new RequestToken();
    for (NameValuePair param : params) {
      if (param.getName().equals("oauth_token")) {
        requestToken.setOauthToken(param.getValue());
      } else if (param.getName().equals("oauth_token_secret")) {
        requestToken.setOauthTokenSecret(param.getValue());
      }
    }
    return requestToken;
  }

  public static TwitterCredentials getAuthentication() {
    String credentialPath = System.getProperty("twitter.credentials.file.path");
    try {
      URL                twitterCredentialsFile = new File(credentialPath).toURI().toURL();
      TwitterCredentials twitterCredentials     = TwitterClient.OBJECT_MAPPER.readValue(twitterCredentialsFile, TwitterCredentials.class);
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
      LOGGER.error("twitter credentials json file error in path " + credentialPath
                   + ". Use program argument -Dtwitter.credentials.file.path=/my/path/to/json . ", e);
      return null;
    }
  }

}
