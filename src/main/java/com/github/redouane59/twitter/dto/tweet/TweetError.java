package com.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetError {
    
    @JsonProperty("title")
    private String title;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("connection_issue")
    private String connectionIssue;

    @JsonProperty("type")
    private String type;
}
