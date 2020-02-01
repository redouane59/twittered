package com.socialmediaraiser.core.twitter.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.core.twitter.IUser;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.ITweet;
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

    private static final String USER = "user";
    private static final String IDS = "ids";
    private static final String NEXT_CURSOR = "next_cursor";
    public static final String FOLLOWING = "following";

    public List<String> jsonLongArrayToList(JsonNode jsonObject){
        ArrayList<String> listdata = new ArrayList<>();
        for(JsonNode n : jsonObject.get(IDS)){
            listdata.add(n.asText());
        }
        return listdata;
    }

    public List<IUser> jsonUserArrayToList(JsonNode jsonObject) {
        ArrayList<IUser> users = new ArrayList<>();
        for(JsonNode node : jsonObject){
            IUser user = null;
            try {
                user = OBJECT_MAPPER.treeToValue(node, UserDTOv1.class);
            } catch (JsonProcessingException e) {
                LOGGER.severe(e.getMessage());
            }
            if(user!=null) users.add(user);
        }
        return users;
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

    // @todo in converterHelper ?
    public static Date getDateFromTwitterDateV2(String date)
    {
        return Option.of(date).map(d -> Date.from(Instant.parse(date))).getOrNull();
    }

    @Deprecated
    public List<ITweet> jsonResponseToTweetListV2(JsonNode jsonArray){
        List<ITweet> tweets = new ArrayList<>();
        if(jsonArray!=null){
            for(JsonNode node : jsonArray){
                try {
                    TweetDTOv1 tweet = OBJECT_MAPPER.treeToValue(node, TweetDTOv1.class);
                    tweet.getUser().setLastUpdate(tweet.getCreatedAt().toString());
                    tweets.add(tweet);
                } catch (JsonProcessingException e) {
                    LOGGER.severe(e.getMessage());
                }
            }
        }
        return tweets;
    }

    @Deprecated
    public List<ITweet> jsonResponseToTweetList(JsonNode jsonArray) {
        List<ITweet> tweets = new ArrayList<>();
        if(jsonArray!=null){
            for(JsonNode node : jsonArray){
                try{
                    tweets.add(OBJECT_MAPPER.treeToValue(node, TweetDTOv1.class));
                } catch (Exception e){
                    LOGGER.severe(e.getMessage());
                }
            }
        }
        return tweets;
    }

}
