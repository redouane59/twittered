package com.socialmediaraiser.twitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialmediaraiser.RelationType;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import com.socialmediaraiser.twitter.helpers.JsonHelper;
import com.socialmediaraiser.twitter.helpers.RequestHelper;
import com.socialmediaraiser.twitter.helpers.URLHelper;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.dto.getrelationship.RelationshipDTO;
import com.socialmediaraiser.twitter.dto.getrelationship.RelationshipObjectResponseDTO;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDataDTO;
import com.socialmediaraiser.twitter.dto.user.UserDTOv2;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@Data
public class TwitterClient implements ITwitterClient {

    private static final Logger LOGGER = Logger.getLogger(TwitterClient.class.getName());
    private URLHelper urlHelper = new URLHelper();
    private RequestHelper requestHelper = new RequestHelper();
    private JsonHelper jsonHelper = new JsonHelper();
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

    // can manage up to 5000 results / call . Max 15 calls / 15min ==> 75.000 results max. / 15min
    private List<String> getUserIdsByRelation(String url){
        Long cursor = -1L;
        List<String> result = new ArrayList<>();
        do {
            String urlWithCursor = url + "&"+CURSOR+"=" + cursor;
            JsonNode response = this.getRequestHelper().executeGetRequest(urlWithCursor);
            if(response!=null && response.has(IDS)){
                List<String> ids = null;
                try {
                    ids = Arrays.asList(JsonHelper.OBJECT_MAPPER.treeToValue(response.get("ids"), String[].class));
                } catch (JsonProcessingException e) {
                    LOGGER.severe(e.getMessage());
                }
                if(ids!=null){
                    result.addAll(ids);
                }
            } else{
                LOGGER.severe(()->nullOrIdNotFoundError);
                return result;
            }

            cursor = this.getJsonHelper().getLongFromCursorObject(response);
        }
        while (cursor != null && cursor != 0);
        return result;
    }

    private Set<String> getUserIdsByRelationSet(String url){
        Long cursor = -1L;
        Set<String> result = new HashSet<>();
        do {
            String urlWithCursor = url + "&"+CURSOR+"=" + cursor;
            JsonNode response = this.getRequestHelper().executeGetRequest(urlWithCursor);
            if(response!=null && response.has(IDS)){
                List<String> ids = null;
                try {
                    ids = Arrays.asList(JsonHelper.OBJECT_MAPPER.treeToValue(response.get("ids"), String[].class));
                } catch (JsonProcessingException e) {
                    LOGGER.severe(e.getMessage());
                }
                if(ids!=null){
                    result.addAll(ids);
                }
            } else{
                LOGGER.severe(()->nullOrIdNotFoundError);
                return result;
            }

            cursor = this.getJsonHelper().getLongFromCursorObject(response);
        }
        while (cursor != null && cursor != 0);
        return result;
    }

    // can manage up to 200 results/call . Max 15 calls/15min ==> 3.000 results max./15min
    private List<IUser> getUsersInfoByRelation(String url) {
        Long cursor = -1L;
        List<IUser> result = new ArrayList<>();
        int nbCalls = 1;
        LOGGER.fine(() -> "users : ");
        do {
            String urlWithCursor = url + "&"+CURSOR+"=" + cursor;
            JsonNode response = this.getRequestHelper().executeGetRequest(urlWithCursor);
            if(response==null){
                break;
            }
            List<IUser> users = null;
            try {
                users = Arrays.asList(JsonHelper.OBJECT_MAPPER.treeToValue(response.get(USERS), UserDTOv1[].class));
            } catch (JsonProcessingException e) {
                LOGGER.severe(e.getMessage());
            }
            result.addAll(users);
            cursor = this.getJsonHelper().getLongFromCursorObject(response);
            nbCalls++;
            LOGGER.info(result.size() + " | ");
        } while (cursor != 0 && cursor!=null);
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
        String response = this.getRequestHelper().executeGetRequestV2(url);
        if(response!=null) {
            try {
                RelationshipDTO relationshipDTO = JsonHelper.OBJECT_MAPPER.readValue(this.getRequestHelper().executeGetRequestV2(url), RelationshipObjectResponseDTO.class).getRelationship();
                Boolean followedBy = relationshipDTO.getSource().isFollowedBy();
                Boolean following = relationshipDTO.getSource().isFollowing();
                if (followedBy && following){
                    return RelationType.FRIENDS;
                } else if (!followedBy && !following){
                    return RelationType.NONE;
                } else if(followedBy){
                    return RelationType.FOLLOWER;
                } else{
                    return RelationType.FOLLOWING;
                }

            } catch (IOException e) {
                this.logError(e, response);
            }
        }
        LOGGER.severe(() -> "areFriends was null for " + userId2 + "! -> false ");
        return null;
    }

    @Override
    public List<String> getRetweetersId(String tweetId) {
        String url = this.urlHelper.getRetweetersUrl(tweetId);
        return this.getUserIdsByRelation(url);
    }

    @Override
    public boolean follow(String userId) {
        String url = this.urlHelper.getFollowUrl(userId);
        JsonNode jsonResponse = this.requestHelper.executePostRequest(url, new HashMap<>());
        if(jsonResponse!=null) {
            if (jsonResponse.has(JsonHelper.FOLLOWING)) {
                return true;
            } else{
                LOGGER.severe(()->"following property not found :(  " + userId + " not followed !");
            }
        }
        LOGGER.severe(()->"jsonResponse was null for user  " + userId);
        return false;
    }

    @Override
    public boolean unfollow(String userId) {
        String url = this.urlHelper.getUnfollowUrl(userId);
        JsonNode jsonResponse = this.requestHelper.executePostRequest(url, new HashMap<>());
        if(jsonResponse!=null){
            LOGGER.info(()->userId + " unfollowed");
            return true;
        }
        LOGGER.severe(()->userId + " not unfollowed");
        return false;
    }

    @Override
    public boolean unfollowByName(String userName) {
        String url = this.urlHelper.getUnfollowByUsernameUrl(userName);
        JsonNode jsonResponse = this.requestHelper.executePostRequest(url, new HashMap<>());
        if(jsonResponse!=null){
            LOGGER.info(()->userName + " unfollowed");
            return true;
        }
        LOGGER.severe(()->userName + " not unfollowed");
        return false;
    }

    // UserV2
    @Override
    public IUser getUserFromUserId(String userId)  {
        String url = this.getUrlHelper().getUserUrl(userId);
        String response = this.getRequestHelper().executeGetRequestV2(url);
        if(response!=null){
            try{
                return JsonHelper.OBJECT_MAPPER.readValue(response, UserDTOv2.class);
            } catch(Exception e){
                this.logError(e, response);
            }
        }
        LOGGER.severe(()->"getUserFromUserId return null for " + userId);
        return null;
    }

    @Override
    public UserDTOv2 getUserFromUserName(String userName) {
        String url = this.getUrlHelper().getUserUrlFromName(userName);
        String response = this.getRequestHelper().executeGetRequestV2(url);
        if (response != null) {
            try {
                return JsonHelper.OBJECT_MAPPER.readValue(response, UserDTOv2.class);
            } catch (IOException e) {
                this.logError(e, response);
            }
        }
        return null;
    }

    public List<IUser> getUsersFromUserNames(List<String> userNames)  {
        String url = this.getUrlHelper().getUsersUrlbyNames(userNames);
        JsonNode response = this.getRequestHelper().executeGetRequestReturningArray(url);
        if(response!=null){
            try {
                return Arrays.asList(JsonHelper.OBJECT_MAPPER.treeToValue(response.get(USERS), UserDTOv1[].class));
            } catch (JsonProcessingException e) {
                LOGGER.severe(e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    public List<IUser> getUsersFromUserIds(List<String> userIds)  {
        String url = this.getUrlHelper().getUsersUrlbyIds(userIds);
        JsonNode response = this.getRequestHelper().executeGetRequestReturningArray(url);
        if(response!=null) {
            try {
                return Arrays.asList(JsonHelper.OBJECT_MAPPER.treeToValue(response, UserDTOv1[].class));
            } catch (JsonProcessingException e) {
                LOGGER.severe(e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String getRateLimitStatus(){
        String url = this.getUrlHelper().getRateLimitUrl();
        return this.getRequestHelper().executeGetRequestV2(url);
    }

    @Override
    public List<ITweet> getUserLastTweets(String userId, int count){
        String url = this.getUrlHelper().getUserTweetsUrl(userId, count);
        JsonNode response = this.getRequestHelper().executeGetRequestReturningArray(url);
        if(response!=null && response.size()>0){
            try {
                return Arrays.asList(JsonHelper.OBJECT_MAPPER.treeToValue(response, TweetDTOv1[].class));
            } catch (JsonProcessingException e) {
                LOGGER.severe(e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void likeTweet(String tweetId) {
        String url = this.getUrlHelper().getLikeUrl(tweetId);
        this.getRequestHelper().executePostRequest(url, null);
    }

    @Override
    public void retweetTweet(String tweetId) {
        throw new UnsupportedOperationException();
    }

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
            JsonNode response = this.getRequestHelper().executeGetRequestWithParameters(this.getUrlHelper().getSearchTweets30daysUrl(),parameters);
            JsonNode responseArray = null;
            try {
                responseArray = JsonHelper.OBJECT_MAPPER.readTree(response.get("results").toString());
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
            }

            if(response.size() > 0){
                try {
                    result.addAll(Arrays.asList(JsonHelper.OBJECT_MAPPER.treeToValue(responseArray, TweetDTOv1[].class)));
                } catch (JsonProcessingException e) {
                    LOGGER.severe(e.getMessage());
                }
            } else{
                LOGGER.severe(()->nullOrIdNotFoundError);
            }

            if(!response.has(NEXT)){
                break;
            }
            next = response.get(NEXT).toString();
            parameters.put(NEXT, next);
        }
        while (next!= null && result.size()<count);
        return result;
    }

    private void logError(Exception e, String response){
        LOGGER.severe(() -> e.getMessage() + " response = " + response);
    }

    // @TODO TweetDTO instead of TweetData ?
    @Override
    public List<TweetDataDTO> readTwitterDataFile(File file) throws IOException {
        if(!file.exists()) {
            LOGGER.severe("file not found at : " + file.toURI().toString());
            return null;
        }
        return  Arrays.asList(JsonHelper.OBJECT_MAPPER.readValue(file, TweetDataDTO[].class));
    }
}
