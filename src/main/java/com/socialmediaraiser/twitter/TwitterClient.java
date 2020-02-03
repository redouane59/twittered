package com.socialmediaraiser.twitter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.RelationType;
import com.socialmediaraiser.twitter.dto.getrelationship.IdListDTO;
import com.socialmediaraiser.twitter.dto.getrelationship.RelationshipObjectResponseDTO;
import com.socialmediaraiser.twitter.dto.getrelationship.UserListDTO;
import com.socialmediaraiser.twitter.dto.others.RateLimitStatusDTO;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.twitter.dto.tweet.TweetDataDTO;
import com.socialmediaraiser.twitter.dto.tweet.TweetSearchV1DTO;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import com.socialmediaraiser.twitter.dto.user.UserDTOv2;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.helpers.RequestHelper;
import com.socialmediaraiser.twitter.helpers.URLHelper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.*;
import java.util.logging.Logger;

@Data
public class TwitterClient implements ITwitterClient {

    private static final Logger LOGGER = Logger.getLogger(TwitterClient.class.getName());
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private URLHelper urlHelper = new URLHelper();
    private RequestHelper requestHelper = new RequestHelper();
    private static final String IDS = "ids";
    private static final String USERS = "users";
    private static final String CURSOR = "cursor";
    private static final String NEXT = "next";
    private static final String RETWEET_COUNT = "retweet_count";
    private static final String RELATIONSHIP = "relationship";
    private static final String FOLLOWING = "following";
    private static final String FOLLOWED_BY = "followed_by";
    private static final String SOURCE = "source";
    private final String nullOrIdNotFoundError = "response null or ids not found !";
    private static final String NEXT_CURSOR = "next_cursor";

    // can manage up to 5000 results / call . Max 15 calls / 15min ==> 75.000 results max. / 15min
    private List<String> getUserIdsByRelation(String url){
        String cursor = "-1";
        List<String> result = new ArrayList<>();
        do {
            String urlWithCursor = url + "&"+CURSOR+"=" + cursor;
            Optional<IdListDTO> idListResponse = this.getRequestHelper().executeGetRequest(urlWithCursor, IdListDTO.class);
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
            Optional<IdListDTO> idListResponse = this.getRequestHelper().executeGetRequest(urlWithCursor, IdListDTO.class);
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
        LOGGER.fine(() -> "users : ");
        do {
            String urlWithCursor = url + "&"+CURSOR+"=" + cursor;
            Optional<UserListDTO> userListDTO = this.getRequestHelper().executeGetRequest(urlWithCursor, UserListDTO.class);
            if(userListDTO.isEmpty()) break;
            result.addAll(userListDTO.get().getUsers());
            cursor = userListDTO.get().getNextCursor();
        }
        while (!cursor.equals("0"));
        LOGGER.info("\n");
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
    public List<IUser> getFollowingsUsers(String userId) {
        return this.getUsersInfoByRelation(userId, RelationType.FOLLOWING);
    }

    @Override
    public RelationType getRelationType(String userId1, String userId2){
        String url = this.urlHelper.getFriendshipUrl(userId1, userId2);
        RelationshipObjectResponseDTO relationshipDTO = this.getRequestHelper()
                .executeGetRequestV2(url, RelationshipObjectResponseDTO.class).orElseThrow(NoSuchElementException::new);
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
    public boolean follow(String userId) {
        String url = this.urlHelper.getFollowUrl(userId);
        UserDTOv1 userResponse = this.requestHelper
                .executePostRequest(url, new HashMap<>(), UserDTOv1.class).orElseThrow(NoSuchElementException::new);
        return !userResponse.isFollowing();
    }

    @Override
    public boolean unfollow(String userId) {
        String url = this.urlHelper.getUnfollowUrl(userId);
        UserDTOv1 userResponse = this.requestHelper
                .executePostRequest(url, new HashMap<>(), UserDTOv1.class).orElseThrow(NoSuchElementException::new);
        return userResponse.isFollowing();
    }

    @Override
    public boolean unfollowByName(String userName) {
        String url = this.urlHelper.getUnfollowByUsernameUrl(userName);
        UserDTOv1 userResponse = this.requestHelper
                .executePostRequest(url, new HashMap<>(), UserDTOv1.class).orElseThrow(NoSuchElementException::new);
        return userResponse.isFollowing();
    }

    // UserV2
    @Override
    public IUser getUserFromUserId(String userId)  {
        String url = this.getUrlHelper().getUserUrl(userId);
        return this.getRequestHelper().executeGetRequestV2(url, UserDTOv2.class).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public UserDTOv2 getUserFromUserName(String userName) {
        String url = this.getUrlHelper().getUserUrlFromName(userName);
        return this.getRequestHelper().executeGetRequestV2(url, UserDTOv2.class).orElseThrow(NoSuchElementException::new);
    }

    public List<IUser> getUsersFromUserNames(List<String> userNames)  {
        String url = this.getUrlHelper().getUsersUrlbyNames(userNames);
        UserDTOv1[] response = this.getRequestHelper()
                .executeGetRequestReturningArray(url, UserDTOv1[].class).orElseThrow(NoSuchElementException::new);
        return Arrays.asList(response);
    }

    public List<IUser> getUsersFromUserIds(List<String> userIds)  {
        String url = this.getUrlHelper().getUsersUrlbyIds(userIds);
        UserDTOv1[] response = this.getRequestHelper()
                .executeGetRequestReturningArray(url, UserDTOv1[].class).orElseThrow(NoSuchElementException::new);
        return Arrays.asList(response);
    }

    @Override
    public List<ITweet> getUserLastTweets(String userId, int count){
        String url = this.getUrlHelper().getUserTweetsUrl(userId, count);
        TweetDTOv1[] response = this.getRequestHelper()
                .executeGetRequestReturningArray(url, TweetDTOv1[].class).orElseThrow(NoSuchElementException::new);
        return Arrays.asList(response);
    }

    @Override
    public RateLimitStatusDTO getRateLimitStatus(){
        String url = this.getUrlHelper().getRateLimitUrl();
        return this.getRequestHelper().executeGetRequestV2(url, RateLimitStatusDTO.class).orElseThrow(NoSuchElementException::new);
    }


    @Override
    public void likeTweet(String tweetId) {
        String url = this.getUrlHelper().getLikeUrl(tweetId);
        this.getRequestHelper().executePostRequest(url, null, TweetDTOv1.class);
    }

    @Override
    public void retweetTweet(String tweetId) {
        throw new UnsupportedOperationException();
    }

    // @todo to test
    @Override
    public List<ITweet> searchForTweetsWithin30days(String query, Date fromDate, Date toDate){
        int count = 100;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query",query);
        parameters.put("maxResults",String.valueOf(count));
        parameters.put("fromDate",ConverterHelper.getStringFromDate(fromDate));
        parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
        String next;
        List<ITweet> result = new ArrayList<>();
        do {
            Optional<TweetSearchV1DTO> tweetSearchV1DTO = this.getRequestHelper().executeGetRequestWithParameters(
                    this.getUrlHelper().getSearchTweets30daysUrl(),parameters, TweetSearchV1DTO.class);
            if(tweetSearchV1DTO.isEmpty()){
                LOGGER.severe(()->"empty response");
                break;
            }
            result.addAll(tweetSearchV1DTO.get().getResults());
            next = tweetSearchV1DTO.get().getNext();
            parameters.put(NEXT, next);
        }
        while (next!= null && result.size()<count);
        return result;
    }

    // @TODO TweetDTO instead of TweetData ?
    @Override
    public List<TweetDataDTO> readTwitterDataFile(File file) throws IOException {
        if(!file.exists()) {
            LOGGER.severe("file not found at : " + file.toURI().toString());
            return null;
        }
        return  Arrays.asList(OBJECT_MAPPER.readValue(file, TweetDataDTO[].class));
    }
}
