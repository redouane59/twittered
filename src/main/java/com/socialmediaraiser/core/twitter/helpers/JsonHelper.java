package com.socialmediaraiser.core.twitter.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.core.twitter.IUser;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.TweetDTOv1;

import com.socialmediaraiser.core.twitter.helpers.dto.user.*;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class JsonHelper {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Logger LOGGER = Logger.getLogger(JsonHelper.class.getName());

    @Deprecated
    private static final String STATUSES_COUNT = "statuses_count";
    private static final String CREATED_AT = "created_at";
    private static final String SCREEN_NAME = "screen_name";
    private static final String USER = "user";
    private static final String FOLLOWER_COUNT = "followers_count";
    @Deprecated
    private static final String FRIENDS_COUNT = "friends_count";
    private static final String FAVORITE_COUNT = "favorite_count";
    private static final String RETWEET_COUNT = "retweet_count";
    private static final String DESCRIPTION = "description";
    private static final String TEXT = "text";
    private static final String STATUS = "status";
    private static final String LOCATION = "location";
    private static final String ID = "id";
    private static final String IDS = "ids";
    private static final String LANG = "lang";
    private static final String NEXT_CURSOR = "next_cursor";
    public static final String FOLLOWING = "following";

    public List<String> jsonLongArrayToList(JsonNode jsonObject){
        ArrayList<String> listdata = new ArrayList<>();
        for(JsonNode n : jsonObject.get(IDS)){
            listdata.add(n.asText());
        }
        return listdata;
    }

    public List<IUser> jsonUserArrayToList(JsonNode jsonObject){
        ArrayList<IUser> users = new ArrayList<>();
        for(JsonNode node : jsonObject){
            IUser user = this.jsonResponseToUser(node);
            if(user!=null) users.add(user);
        }
        return users;
    }

    @Deprecated
    public UserDTOv1 jsonResponseToUser(JsonNode jsonObject){
        if(jsonObject==null) return null;

        String id = jsonObject.get(ID).asText();
        String screenName = Option.of(jsonObject.get(SCREEN_NAME)).map(s -> jsonObject.get(SCREEN_NAME).asText()).getOrNull();
        int followersCount = Option.of(jsonObject.get(FOLLOWER_COUNT)).map(s -> jsonObject.get(FOLLOWER_COUNT).asInt()).getOrElse(0);
        int friendsCount = Option.of(jsonObject.get(FRIENDS_COUNT)).map(s -> jsonObject.get(FRIENDS_COUNT).asInt()).getOrElse(0);
        int statusesCount = Option.of(jsonObject.get(STATUSES_COUNT)).map(s -> jsonObject.get(STATUSES_COUNT).asInt()).getOrElse(0);
        String createdAt = Option.of(jsonObject.get(CREATED_AT)).map(s -> jsonObject.get(CREATED_AT).asText()).getOrNull();
        String description = Option.of(jsonObject.get(DESCRIPTION)).map(s -> jsonObject.get(DESCRIPTION).asText()).getOrNull();
        String location = Option.of(jsonObject.get(LOCATION)).map(s -> jsonObject.get(LOCATION).asText()).getOrElse("");
        String lastUpdate = Option.of(jsonObject.get(STATUS)).map(s -> (jsonObject.get(STATUS)).get(CREATED_AT).asText()).getOrNull();
        return UserDTOv1.builder()
                .id(id)
                .name(screenName)
                .followersCount(followersCount)
                .followingCount(friendsCount)
                .tweetCount(statusesCount)
                .dateOfCreation(createdAt)
                .description(description)
                .dateOfFollow(null)
                .lastUpdate(lastUpdate)
                .location(location)
                .build();
    }

    // @todo use custom serializer ?
    @Deprecated
    public UserDTOv2 jsonResponseToUserV2(String response) throws IOException {
        UserObjectResponseDTO obj = JsonHelper.OBJECT_MAPPER.readValue(response, UserObjectResponseDTO.class);
        if(obj.getData()==null){
            LOGGER.severe(response);
            return null;
        }
        return obj.getData().get(0);
         /*
        UserDTOv2 data = obj.getData().get(0);
        IncludesDTO includes = obj.getIncludes();
        List<TweetDTO> mostRecentTweet = null;
        String lang = null;
        String lastUpdate = null;
        if(!data.isProtectedAccount() && includes!=null){
            mostRecentTweet = includes.getTweets();
            lang = includes.getTweets().get(0).getLang();
            lastUpdate = includes.getTweets().get(0).getCreatedAt();
        }

       return UserDTOv2.builder()
                .id(data.getId())
                .name(data.getName())
                .followersCount(data.getStats().getFollowersCount())
                .followingCount(data.getStats().getFollowingCount())
                .tweetCount(data.getStats().getTweetCount())
                .dateOfCreation(data.getCreatedAt())
                .description(data.getDescription())
                .dateOfFollow(null)
                .location(data.getLocation())
                .mostRecentTweet(mostRecentTweet)
                .lang(lang)
                .lastUpdate(lastUpdate) // @todo manage null value in scoring ?
                .protectedAccount(data.isProtectedAccount())
                .build(); */
    }

    @Deprecated
    public Long getLongFromCursorObject(JsonNode response){
        if(response==null){
            LOGGER.severe(()->"result null");
            return null;
        }
        return response.get(NEXT_CURSOR).asLong();
    }

    // @todo in converterHelper ?
    public static Date getDateFromTwitterString(String date)
    {
        if(date==null) return null;
        final String TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        return Try.of(() -> sf.parse(date)).getOrNull();
    }

    public static Date getDateFromTwitterDateV2(String date)
    {
        return Option.of(date).map(d -> Date.from(Instant.parse(date))).getOrNull();
    }

    @Deprecated
    public List<TweetDTOv1> jsonResponseToTweetListV2(JsonNode jsonArray){
        List<TweetDTOv1> tweets = new ArrayList<>();
        if(jsonArray!=null){
            for(JsonNode node : jsonArray){
                try {
                    TweetDTOv1 tweet = new ObjectMapper().treeToValue(node, TweetDTOv1.class);
                    UserDTOv1 user = jsonResponseToUser(node.get(USER));
                    user.setLastUpdate(tweet.getCreatedAt().toString());
                    tweet.setUser(user);
                    tweets.add(tweet);
                } catch (JsonProcessingException e) {
                    LOGGER.severe(e.getMessage());
                }
            }
        }
        return tweets;
    }

    @Deprecated
    public List<TweetDTOv1> jsonResponseToTweetList(JsonNode jsonArray) {
        List<TweetDTOv1> tweets = new ArrayList<>();
        if(jsonArray!=null){
            for(JsonNode node : jsonArray){
                String id = node.get(ID).asText();
                int retweetsCount = node.get(RETWEET_COUNT).asInt();
                int favourites_count = node.get(FAVORITE_COUNT).asInt();
                String text = node.get(TEXT).asText();
                String lang = node.get(LANG).asText();
                String createdAtDate = node.get(CREATED_AT).asText();
                UserDTOv1 user = null;
                try{ // @todo to test
                    user = jsonResponseToUser(node.get(USER));
                    user.setLastUpdate(createdAtDate);
                } catch (Exception e){
                    LOGGER.severe(e.getMessage());
                }
                TweetDTOv1 tweet = TweetDTOv1.builder()
                        .id(id)
                        .retweetCount(retweetsCount)
                        .likeCount(favourites_count)
                        .text(text)
                        .lang(lang)
                        .createdAt(node.get(CREATED_AT).asText())
                        .user(user)
                        .build();
                tweets.add(tweet);
            }
        }
        return tweets;
    }

}
