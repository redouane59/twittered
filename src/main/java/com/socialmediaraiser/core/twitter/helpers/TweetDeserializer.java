package com.socialmediaraiser.core.twitter.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialmediaraiser.core.twitter.Tweet;
import com.socialmediaraiser.core.twitter.User;

import java.io.IOException;

public class TweetDeserializer extends JsonDeserializer<Tweet>
{

    private static final String createdAt = "created_at";

    @Deprecated
    @Override
    public Tweet deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        JsonNode tweetNode = parser.readValueAsTree();
        JsonNode userNode = tweetNode.get("user");

        User user = User.builder()
                .id(userNode.get("id").asText())
                .userName(userNode.get("screen_name").asText())
                .followingCount(userNode.get("friends_count").asInt())
                .followersCout(userNode.get("followers_count").asInt())
                .statusesCount(userNode.get("statuses_count").asInt())
                .favouritesCount(userNode.get("favourites_count").asInt())
                .location(userNode.get("location").asText())
                .description(userNode.get("description").asText())
                .dateOfCreation(JsonHelper.getDateFromTwitterString(userNode.get(createdAt).asText()))
                .lastUpdate(JsonHelper.getDateFromTwitterString(tweetNode.get(createdAt).asText()))
                .lang(tweetNode.get("lang").asText())
                .build();

        return Tweet.builder()
                .id(tweetNode.get("id").asText())
                .text(tweetNode.get("text").asText())
                .lang(tweetNode.get("lang").asText())
                .favoriteCount(tweetNode.get("favorite_count").asInt())
                .retweetCount(tweetNode.get("retweet_count").asInt())
                .replyCount(tweetNode.get("reply_count").asInt())
                .inReplyToStatusId(tweetNode.get("in_reply_to_status_id_str").asText())
                .user(user)
                .createdAt(JsonHelper.getDateFromTwitterString(tweetNode.get(createdAt).asText()))
                .build();
    }
}
