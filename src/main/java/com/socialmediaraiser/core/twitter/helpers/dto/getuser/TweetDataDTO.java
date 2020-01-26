package com.socialmediaraiser.core.twitter.helpers.dto.getuser;

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
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("retweet_count")
        private int retweetCount;
    }
}
