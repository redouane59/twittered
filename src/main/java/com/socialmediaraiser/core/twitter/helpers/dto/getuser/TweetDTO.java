package com.socialmediaraiser.core.twitter.helpers.dto.getuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class TweetDTO {
    private String id;
    @JsonProperty("created_at")
    private String createdAt;
    private String text;
    @JsonProperty("author_id")
    private String authorId;
    @JsonProperty("in_reply_to_user_id")
    private String inReplyToUserId;
    @JsonProperty("referenced_tweets")
    private List<ReferencedTweetDTO> referencedTweets;
    private JsonNode entities;
    private TwitterStatsDTO stats;
    @JsonProperty("possibly_sensitive")
    private boolean possiblySensitive;
    private String lang;
    private String source;
    private String format;
    private JsonNode attachments;
    private JsonNode geo;
}
