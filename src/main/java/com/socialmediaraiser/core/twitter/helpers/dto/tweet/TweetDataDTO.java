package com.socialmediaraiser.core.twitter.helpers.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class TweetDataDTO {
    private TweetDataContentDTO tweet;

    @Data
    public static class TweetDataContentDTO{
        private String id;
        @JsonProperty("full_text")
        private String fullText;
        @JsonProperty("in_reply_to_user_id_str")
        private String inReplyToUserId;
        @JsonProperty("in_reply_to_screen_name")
        private String inReplyToScreenName;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("retweet_count")
        private int retweetCount;
        @JsonProperty("favorite_count")
        private int favorite_count;
        private String lang;
    }
}
