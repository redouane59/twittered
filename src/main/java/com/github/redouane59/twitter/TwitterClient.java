package com.github.redouane59.twitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.redouane59.RelationType;
import com.github.redouane59.twitter.dto.getrelationship.IdListDTO;
import com.github.redouane59.twitter.dto.stream.StreamRulesDTO;
import com.github.redouane59.twitter.dto.stream.StreamRulesDTO.StreamMeta;
import com.github.redouane59.twitter.dto.stream.StreamRulesDTO.StreamRule;
import com.github.redouane59.twitter.dto.tweet.HiddenResponseDTO.HiddenDataDTO;
import com.github.redouane59.twitter.dto.tweet.TweetDTOv2.TweetData;
import com.github.redouane59.twitter.dto.user.IUser;
import com.github.redouane59.twitter.dto.user.UserDTOListv2;
import com.github.redouane59.twitter.dto.user.UserDTOv2.UserData;
import com.github.redouane59.twitter.helpers.RequestHelper;
import com.github.redouane59.twitter.helpers.RequestHelperV2;
import com.github.redouane59.twitter.helpers.URLHelper;
import com.github.redouane59.twitter.dto.getrelationship.RelationshipObjectResponseDTO;
import com.github.redouane59.twitter.dto.getrelationship.UserListDTO;
import com.github.redouane59.twitter.dto.others.BearerTokenDTO;
import com.github.redouane59.twitter.dto.others.RateLimitStatusDTO;
import com.github.redouane59.twitter.dto.others.RequestTokenDTO;
import com.github.redouane59.twitter.dto.tweet.*;
import com.github.redouane59.twitter.dto.user.UserDTOv1;
import com.github.redouane59.twitter.dto.user.UserDTOv2;
import com.github.redouane59.twitter.helpers.*;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Setter
@Slf4j
public class TwitterClient implements ITwitterClient {

    public static TwitterCredentials TWITTER_CREDENTIALS;
    public static final  ObjectMapper    OBJECT_MAPPER   = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private              URLHelper       urlHelper       = new URLHelper();
    private              RequestHelper   requestHelper;
    private              RequestHelperV2 requestHelperV2;
    private static final String          IDS             = "ids";
    private static final String USERS = "users";
    private static final String CURSOR = "cursor";
    private static final String NEXT = "next";
    private static final String RETWEET_COUNT = "retweet_count";
    private static final String RELATIONSHIP = "relationship";
    private static final String FOLLOWING = "following";
    private static final String FOLLOWED_BY = "followed_by";
    private static final String SOURCE = "source";
    private final static String NULL_OR_ID_NOT_FOUND_ERROR = "response null or ids not found !";
    private static final String NEXT_CURSOR = "next_cursor";

    public TwitterClient(){
        TWITTER_CREDENTIALS = getAuthentication();
        requestHelper   = new RequestHelper();
        requestHelperV2 = new RequestHelperV2(this.getBearerToken());
    }

    public TwitterClient(TwitterCredentials credentials){
        TWITTER_CREDENTIALS = credentials;
        requestHelper   = new RequestHelper();
        requestHelperV2 = new RequestHelperV2(this.getBearerToken());
    }

    // can manage up to 5000 results / call . Max 15 calls / 15min ==> 75.000 results max. / 15min
    private List<String> getUserIdsByRelation(String url){
        String cursor = "-1";
        List<String> result = new ArrayList<>();
        do {
            String              urlWithCursor  = url + "&"+CURSOR+"=" + cursor;
            Optional<IdListDTO> idListResponse = this.requestHelperV2.executeGetRequest(urlWithCursor, IdListDTO.class);
            if(idListResponse.isEmpty()) break;
            result.addAll(idListResponse.get().getIds());
            cursor = idListResponse.get().getNextCursor();
        }
        while (!cursor.equals("0"));
        return result;
    }

    private Set<String> getUserIdsByRelationSet(String url){
        String cursor = "-1";
        Set<String> result = new HashSet<>();
        do {
            String urlWithCursor = url + "&"+CURSOR+"=" + cursor;
            Optional<IdListDTO> idListResponse = this.requestHelperV2.executeGetRequest(urlWithCursor, IdListDTO.class);
            if(idListResponse.isEmpty()) break;
            result.addAll(idListResponse.get().getIds());
            cursor = idListResponse.get().getNextCursor();
        }
        while (!cursor.equals("0"));
        return result;
    }

    // can manage up to 200 results/call . Max 15 calls/15min ==> 3.000 results max./15min
    private List<IUser> getUsersInfoByRelation(String url) {
        String cursor = "-1";
        List<IUser> result = new ArrayList<>();
        do {
            String urlWithCursor = url + "&"+CURSOR+"=" + cursor;
            Optional<UserListDTO> userListDTO = this.requestHelperV2.executeGetRequest(urlWithCursor, UserListDTO.class);
            if(userListDTO.isEmpty()) break;
            result.addAll(userListDTO.get().getUsers());
            cursor = userListDTO.get().getNextCursor();
        }
        while (!cursor.equals("0"));
        return result;
    }

    private List<String> getUserIdsByRelation(String userId, RelationType relationType){
        String url = null;
        if(relationType == RelationType.FOLLOWER){
            url = this.urlHelper.getFollowerIdsUrl(userId);
        } else if (relationType == RelationType.FOLLOWING){
            url = this.urlHelper.getFollowingIdsUrl(userId);
        }
        return this.getUserIdsByRelation(url);
    }

    private List<IUser> getUsersInfoByRelation(String userId, RelationType relationType) {
        String url = null;
        if(relationType == RelationType.FOLLOWER){
            url = this.urlHelper.getFollowerUsersUrl(userId);
        } else if (relationType == RelationType.FOLLOWING){
            url = this.urlHelper.getFollowingUsersUrl(userId);
        }
        return this.getUsersInfoByRelation(url);
    }

    public Set<String> getUserFollowersIds(String userId){
        return this.getUserIdsByRelationSet(this.urlHelper.getFollowerIdsUrl(userId));
    }

    @Override
    public List<String> getFollowerIds(String userId)  {
        return this.getUserIdsByRelation(userId, RelationType.FOLLOWER);
    }

    @Override
    public List<IUser> getFollowerUsers(String userId) {
        return this.getUsersInfoByRelation(userId, RelationType.FOLLOWER);
    }

    @Override
    public List<String> getFollowingIds(String userId) {
        return this.getUserIdsByRelation(userId, RelationType.FOLLOWING);
    }

    @Override
    public List<IUser> getFollowingUsers(String userId) {
        return this.getUsersInfoByRelation(userId, RelationType.FOLLOWING);
    }

    @Override
    public RelationType getRelationType(String userId1, String userId2){
        String url = this.urlHelper.getFriendshipUrl(userId1, userId2);
        RelationshipObjectResponseDTO relationshipDTO = this.requestHelperV2
            .executeGetRequest(url, RelationshipObjectResponseDTO.class).orElseThrow(NoSuchElementException::new);
        Boolean followedBy = relationshipDTO.getRelationship().getSource().isFollowedBy();
        Boolean following = relationshipDTO.getRelationship().getSource().isFollowing();
        if (followedBy && following){
            return RelationType.FRIENDS;
        } else if (!followedBy && !following){
            return RelationType.NONE;
        } else if(followedBy){
            return RelationType.FOLLOWER;
        } else{
            return RelationType.FOLLOWING;
        }
    }

    @Override
    public List<String> getRetweetersId(String tweetId) {
        String url = this.urlHelper.getRetweetersUrl(tweetId);
        return this.getUserIdsByRelation(url);
    }

    @Override
    public IUser follow(String userId) {
        String url = this.urlHelper.getFollowUrl(userId);
        return this.requestHelper
            .executePostRequest(url, new HashMap<>(), UserDTOv1.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public IUser unfollow(String userId) {
        String url = this.urlHelper.getUnfollowUrl(userId);
        return this.requestHelper
            .executePostRequest(url, new HashMap<>(), UserDTOv1.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public IUser unfollowByName(String userName) {
        String url = this.urlHelper.getUnfollowByUsernameUrl(userName);
        return this.requestHelper
            .executePostRequest(url, new HashMap<>(), UserDTOv1.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public IUser getUserFromUserId(String userId)  {
        String url = this.getUrlHelper().getUserUrl(userId);
        return this.requestHelperV2.executeGetRequest(url, UserDTOv2.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public UserDTOv2 getUserFromUserName(String userName) {
        String url = this.getUrlHelper().getUserUrlFromName(userName);
        return this.requestHelperV2.executeGetRequest(url, UserDTOv2.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<IUser> getUsersFromUserNames(List<String> userNames)  {
        String         url      = this.getUrlHelper().getUsersUrlbyNames(userNames);
        List<UserData> result = this.requestHelperV2.executeGetRequest(url, UserDTOListv2.class).orElseThrow(NoSuchElementException::new).getData();
        return result.stream().map(userData -> UserDTOv2.builder().data(userData).build()).collect(Collectors.toList());
    }


    @Override
    public List<IUser> getUsersFromUserIds(List<String> userIds)  {
        String url = this.getUrlHelper().getUsersUrlbyIds(userIds);
        List<UserData> result = this.requestHelperV2.executeGetRequest(url, UserDTOListv2.class).orElseThrow(NoSuchElementException::new).getData();
        return result.stream().map(userData -> UserDTOv2.builder().data(userData).build()).collect(Collectors.toList());
    }

    @Override
    public List<ITweet> getUserLastTweets(String userId, int count){
        String url = this.getUrlHelper().getUserTweetsUrl(userId, count);
        TweetDTOv1[] response = this.requestHelperV2.executeGetRequest(url, TweetDTOv1[].class).orElseThrow(NoSuchElementException::new);
        return List.of(response);
    }

    @Override
    public RateLimitStatusDTO getRateLimitStatus(){
        String url = URLHelper.RATE_LIMIT_URL;
        return this.requestHelperV2.executeGetRequest(url, RateLimitStatusDTO.class).orElseThrow(NoSuchElementException::new);
    }


    @Override
    public ITweet likeTweet(String tweetId) {
        String url = this.getUrlHelper().getLikeUrl(tweetId);
        return this.requestHelper.executePostRequest(url, new HashMap<>(), TweetDTOv1.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public ITweet unlikeTweet(String tweetId) {
        String url = this.getUrlHelper().getUnlikeUrl(tweetId);
        return this.requestHelper.executePostRequest(url, new HashMap<>(), TweetDTOv1.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public ITweet retweetTweet(String tweetId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ITweet postTweet(String text){
        String url = this.getUrlHelper().getPostTweetUrl();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("status", text);
        return this.getRequestHelper().executePostRequest(url, parameters, TweetDTOv1.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public ITweet getTweet(String tweetId){
        String url = this.getUrlHelper().getTweetUrl(tweetId);
        return this.requestHelperV2.executeGetRequest(url, TweetDTOv2.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<ITweet> getTweets(List<String> tweetIds){
        String          url    = this.getUrlHelper().getTweetListUrl(tweetIds);
        List<TweetData> result = this.requestHelperV2.executeGetRequest(url, TweetDTOListv2.class).orElseThrow(NoSuchElementException::new).getData();
        return result.stream().map(tweetData -> TweetDTOv2.builder().data(tweetData).build()).collect(Collectors.toList());
    }

    @Override
    public boolean hideReply(final String tweetId, final boolean hide) {
        String url  = this.getUrlHelper().getHideReplyUrl(tweetId);
        try {
            String body = TwitterClient.OBJECT_MAPPER.writeValueAsString(new HiddenDataDTO(hide));
            HiddenResponseDTO response = this.requestHelper.executePutRequest(url, body, HiddenResponseDTO.class)
                                                           .orElseThrow(NoSuchElementException::new);
            return response.getData().isHidden();
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<ITweet> getFavorites(String userId, int count) {
        List<ITweet> favoriteTweets = new ArrayList<>();
        List<TweetDTOv1> result;
        String maxId = null;
        do{
            result = List.of(this.requestHelperV2.executeGetRequest(this.getUrlHelper().getFavoriteTweetsUrl(userId, maxId), TweetDTOv1[].class)
                                                 .orElseThrow(NoSuchElementException::new));
            if(result.size()==0) break;
            maxId = result.get(result.size()-1).getId();
            favoriteTweets.addAll(result.subList(0, result.size() - 1)); // to avoid having duplicates
        } while (favoriteTweets.size() < count && result.size()>1);
        return favoriteTweets;
    }

    @Override
    public List<ITweet> searchForTweetsWithin7days(String query, LocalDateTime fromDate, LocalDateTime toDate) {
        int                 count      = 100;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query", query);
        parameters.put("max_results", String.valueOf(count));
        if (fromDate != null) {
            parameters.put("start_time", ConverterHelper.getStringFromDateV2(fromDate));
        }
        if (toDate != null){
            parameters.put("end_time", ConverterHelper.getStringFromDateV2(toDate));
        }
        parameters.put("tweet.fields", "attachments,author_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,source,text,withheld,context_annotations,conversation_id");
        String next;
        List<ITweet> result = new ArrayList<>();
        do {
            Optional<TweetSearchV2DTO> tweetSearchV2DTO = this.requestHelperV2.executeGetRequestWithParameters(
                URLHelper.SEARCH_TWEET_7_DAYS_URL,parameters, TweetSearchV2DTO.class);
            if(tweetSearchV2DTO.isEmpty() || tweetSearchV2DTO.get().getData()==null){
                LOGGER.error("empty response on searchForTweetsWithin7days");
                break;
            }
            result.addAll(tweetSearchV2DTO.get().getData());
            next = tweetSearchV2DTO.get().getMeta().getNextToken();
            parameters.put("next_token", next);
        }
        while (next!= null);
        return result;
    }

    @Override
    public List<ITweet> searchForTweetsWithin7days(String query){
        return this.searchForTweetsWithin7days(query, null, null);
    }

    @Override
    public List<ITweet> searchForTweetsWithin30days(String query, LocalDateTime fromDate, LocalDateTime toDate){
        int count = 100;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query",query);
        parameters.put("maxResults",String.valueOf(count));
        parameters.put("fromDate",ConverterHelper.getStringFromDate(fromDate));
        parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
        String next;
        List<ITweet> result = new ArrayList<>();
        do {
            Optional<TweetSearchV1DTO> tweetSearchV1DTO = this.requestHelperV2.executeGetRequestWithParameters(
                URLHelper.SEARCH_TWEET_30_DAYS_URL,parameters, TweetSearchV1DTO.class);
            if(tweetSearchV1DTO.isEmpty()){
                LOGGER.error("empty response on searchForTweetsWithin30days");
                break;
            }
            result.addAll(tweetSearchV1DTO.get().getResults());
            next = tweetSearchV1DTO.get().getNext();
            parameters.put(NEXT, next);
        }
        while (next!= null);
        return result;
    }

    @Override
    public List<ITweet> searchForTweetsArchive(String query, LocalDateTime fromDate, LocalDateTime toDate) {
        int count = 100;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query",query);
        parameters.put("maxResults",String.valueOf(count));
        parameters.put("fromDate",ConverterHelper.getStringFromDate(fromDate));
        parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
        String next;
        List<ITweet> result = new ArrayList<>();
        do {
            Optional<TweetSearchV1DTO> tweetSearchV1DTO = this.requestHelperV2.executeGetRequestWithParameters(
                URLHelper.SEARCH_TWEET_FULL_ARCHIVE_URL,parameters, TweetSearchV1DTO.class);
            if(tweetSearchV1DTO.isEmpty()){
                LOGGER.error("empty response on searchForTweetsArchive");
                break;
            }
            result.addAll(tweetSearchV1DTO.get().getResults());
            next = tweetSearchV1DTO.get().getNext();
            parameters.put(NEXT, next);
        }
        while (next!= null);
        return result;
    }

    @Override
    public List<StreamRule> retrieveFilteredStreamRules() {
        String       url    = this.urlHelper.getFilteredStreamRulesUrl();
        StreamRulesDTO result = this.requestHelperV2.executeGetRequest(url, StreamRulesDTO.class).orElseThrow(NoSuchElementException::new);
        return result.getData();
    }

    @Override
    public StreamRule addFilteredStreamRule(String value, String tag){
        String       url    = this.urlHelper.getFilteredStreamRulesUrl();
        try {
            StreamRule rule = StreamRule.builder().value(value).tag(tag).build();
            String body = "{\"add\": ["+TwitterClient.OBJECT_MAPPER.writeValueAsString(rule)+"]}";
            StreamRulesDTO result = this.requestHelperV2.executePostRequest(url, body, StreamRulesDTO.class).orElseThrow(NoSuchElementException::new);
            if(result.getData()==null || result.getData().size()==0) throw new IllegalArgumentException();
            return result.getData().get(0);
        }
        catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public StreamMeta deleteFilteredStreamRule(String ruleValue){
        String       url    = this.urlHelper.getFilteredStreamRulesUrl();
        String body = "{\"delete\": {\"values\": [\""+ruleValue+"\"]}}";
        StreamRulesDTO result = this.requestHelperV2.executePostRequest(url, body, StreamRulesDTO.class).orElseThrow(NoSuchElementException::new);
        return result.getMeta();
    }

    @Override
    public List<TweetDTOv1> readTwitterDataFile(File file) throws IOException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TweetDTOv1.class, new TweetDTOv1Deserializer());
        ObjectMapper customObjectMapper = new ObjectMapper();
        customObjectMapper.registerModule(module);

        List<TweetDTOv1> result = new ArrayList<>();
        if(!file.exists()) {
            LOGGER.error("file not found at : " + file.toURI().toString());
        } else{
            result = List.of(customObjectMapper.readValue(file, TweetDTOv1[].class));
        }
        return result;
    }

    @Override
    public String getBearerToken() {
        String url = URLHelper.GET_BEARER_TOKEN_URL;
        String valueToCrypt = TWITTER_CREDENTIALS.getApiKey()
                              +":"+TWITTER_CREDENTIALS.getApiSecretKey();
        String cryptedValue = Base64.getEncoder().encodeToString(valueToCrypt.getBytes());
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", "Basic " + cryptedValue);
        params.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        String body = "grant_type=client_credentials";
        BearerTokenDTO result = this.requestHelperV2
            .executePostRequestWithHeader(url, params, body, BearerTokenDTO.class).orElseThrow(NoSuchElementException::new);
        return result.getAccessToken();
    }

    @Override
    public RequestTokenDTO getOauth1Token(){
        String url = URLHelper.GET_OAUTH1_TOKEN_URL;
        String stringResponse = this.requestHelper.executePostRequest(url, new HashMap<>(),String.class)
                                                  .orElseThrow(NoSuchElementException::new);
        List<NameValuePair> params = null;
        try {
            params = URLEncodedUtils.parse(new URI("twitter.com?"+stringResponse), StandardCharsets.UTF_8.name());
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
        RequestTokenDTO requestTokenDTO = new RequestTokenDTO();
        for (NameValuePair param : params) {
            if(param.getName().equals("oauth_token")){
                requestTokenDTO.setOauthToken(param.getValue());
            } else if (param.getName().equals("oauth_token_secret")){
                requestTokenDTO.setOauthTokenSecret(param.getValue());
            }
        }
        return requestTokenDTO;
    }

    public static TwitterCredentials getAuthentication(){
        String credentialPath = System.getProperty("twitter.credentials.file.path");
        try {
            URL                twitterCredentialsFile = new File(credentialPath).toURI().toURL();
            TwitterCredentials twitterCredentials     = TwitterClient.OBJECT_MAPPER.readValue(twitterCredentialsFile, TwitterCredentials.class);
            if(twitterCredentials.getAccessToken()==null) LOGGER.error("Access token is null in twitter-credentials.json");
            if(twitterCredentials.getAccessTokenSecret()==null) LOGGER.error("Secret token is null in twitter-credentials.json");
            if(twitterCredentials.getApiKey()==null) LOGGER.error("Consumer key is null in twitter-credentials.json");
            if(twitterCredentials.getApiSecretKey()==null) LOGGER.error("Consumer secret is null in twitter-credentials.json");
            return twitterCredentials;
        } catch (Exception e) {
            LOGGER.error("twitter credentials json file error in path " + credentialPath
                         + ". Use program argument -Dtwitter.credentials.file.path=/my/path/to/json . ", e);
            return null;
        }
    }

}
