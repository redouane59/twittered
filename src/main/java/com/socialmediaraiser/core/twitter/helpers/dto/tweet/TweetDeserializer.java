package com.socialmediaraiser.core.twitter.helpers.dto.tweet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.user.UserDTOv1;

import java.io.IOException;
import java.util.Optional;

public class TweetDeserializer extends JsonDeserializer<TweetDTOv1>
{

    private static final String CREATED_AT = "created_at";

    @Deprecated
    @Override
    public TweetDTOv1 deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        JsonNode tweetNode = parser.readValueAsTree();
        JsonNode userNode = tweetNode.get("user");

        UserDTOv1 user = null;
        if(userNode!=null) {
            user = UserDTOv1.builder()
                    .id(userNode.get("id").asText())
                    .name(userNode.get("screen_name").asText())
                    .followingCount(userNode.get("friends_count").asInt())
                    .followersCount(userNode.get("followers_count").asInt())
                    .tweetCount(userNode.get("statuses_count").asInt())
                    .location(userNode.get("location").asText())
                    .description(userNode.get("description").asText())
                    .dateOfCreation(userNode.get(CREATED_AT).asText())
                    .lastUpdate(tweetNode.get(CREATED_AT).asText())
                    .lang(tweetNode.get("lang").asText())
                    .build();
        }
        return TweetDTOv1.builder()
                .id(tweetNode.get("id").asText())
                .text(Optional.ofNullable(tweetNode.get("text")).orElse(tweetNode.get("full_text")).asText())
                .lang(tweetNode.get("lang").asText())
                .likeCount(tweetNode.get("favorite_count").asInt())
                .retweetCount(tweetNode.get("retweet_count").asInt())
                .replyCount(Optional.ofNullable(tweetNode.get("reply_count")).orElse(JsonHelper.OBJECT_MAPPER.createObjectNode()).asInt())
                .inReplyToStatusId(Optional.ofNullable(tweetNode.get("in_reply_to_status_id_str")).orElse(JsonHelper.OBJECT_MAPPER.createObjectNode()).asText())
                .inReplyToUserId(Optional.ofNullable(tweetNode.get("in_reply_to_user_id")).orElse(JsonHelper.OBJECT_MAPPER.createObjectNode()).asText())
                .user(user)
              //  .createdAt(JsonHelper.getDateFromTwitterString(tweetNode.get(CREATED_AT).asText()))
                .build();
    }
}
