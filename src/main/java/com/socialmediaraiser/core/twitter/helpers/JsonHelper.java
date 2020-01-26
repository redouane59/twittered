package com.socialmediaraiser.core.twitter.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.core.twitter.Tweet;
import com.socialmediaraiser.core.twitter.User;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.*;
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

    public List<AbstractUser> jsonUserArrayToList(JsonNode jsonObject){
        ArrayList<AbstractUser> users = new ArrayList<>();
        for(JsonNode node : jsonObject){
            AbstractUser user = this.jsonResponseToUser(node);
            if(user!=null) users.add(user);
        }
        return users;
    }

    public User jsonResponseToUser(JsonNode jsonObject){
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
        return User.builder()
                .id(id)
                .userName(screenName)
                .followersCout(followersCount)
                .followingCount(friendsCount)
                .statusesCount(statusesCount)
                .dateOfCreation(getDateFromTwitterString(createdAt))
                .description(description)
                .dateOfFollow(null)
                .lastUpdate(getDateFromTwitterString(lastUpdate))
                .location(location)
                .build();
    }

    // @todo use custom serializer ?
    public AbstractUser jsonResponseToUserV2(String response) throws IOException {
        UserObjectResponseDTO obj = JsonHelper.OBJECT_MAPPER.readValue(response, UserObjectResponseDTO.class);
        UserDTO data = obj.getData().get(0);
        IncludesDTO includes = obj.getIncludes();
        List<TweetDTO> mostRecentTweet = null;
        String lang = null;
        Date lastUpdate = null;
        if(!data.isProtectedAccount() && includes!=null){
            mostRecentTweet = includes.getTweets();
            lang = includes.getTweets().get(0).getLang();
            lastUpdate = getDateFromTwitterDateV2(includes.getTweets().get(0).getCreatedAt());
        }
        return User.builder()
                .id(data.getId())
                .userName(data.getUsername())
                .followersCout(data.getStats().getFollowersCount())
                .followingCount(data.getStats().getFollowingCount())
                .statusesCount(data.getStats().getTweetCount())
                .dateOfCreation(getDateFromTwitterDateV2(data.getCreatedAt()))
                .description(data.getDescription())
                .dateOfFollow(null)
                .location(data.getLocation())
                .mostRecentTweet(mostRecentTweet)
                .lang(lang)
                .lastUpdate(lastUpdate) // @todo manage null value in scoring ?
                .protectedAccount(data.isProtectedAccount())
                .build();
    }

    @Deprecated
    public Long getLongFromCursorObject(JsonNode response){
        if(response==null){
            LOGGER.severe(()->"result null");
            return null;
        }
        return response.get(NEXT_CURSOR).asLong();
    }

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

    public List<Tweet> jsonResponseToTweetListV2(JsonNode jsonArray){
        List<Tweet> tweets = new ArrayList<>();
        if(jsonArray!=null){
            for(JsonNode node : jsonArray){
                try {
                    Tweet tweet = new ObjectMapper().treeToValue(node, Tweet.class);
                    User user = jsonResponseToUser(node.get(USER));
                    user.setLastUpdate(tweet.getCreatedAt());
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
    public List<Tweet> jsonResponseToTweetList(JsonNode jsonArray) {
        List<Tweet> tweets = new ArrayList<>();
        if(jsonArray!=null){
            for(JsonNode node : jsonArray){
                String id = node.get(ID).asText();
                int retweetsCount = node.get(RETWEET_COUNT).asInt();
                int favourites_count = node.get(FAVORITE_COUNT).asInt();
                String text = node.get(TEXT).asText();
                String lang = node.get(LANG).asText();
                Date createdAtDate = getDateFromTwitterString(node.get(CREATED_AT).asText());
                User user = null;
                try{ // @todo to test
                    user = jsonResponseToUser(node.get(USER));
                    user.setLastUpdate(createdAtDate);
                } catch (Exception e){
                    LOGGER.severe(e.getMessage());
                }
                Tweet tweet = Tweet.builder()
                        .id(id)
                        .retweetCount(retweetsCount)
                        .favoriteCount(favourites_count)
                        .text(text)
                        .lang(lang)
                        .createdAt(createdAtDate)
                        .user(user)
                        .build();
                tweets.add(tweet);
            }
        }
        return tweets;
    }

}
