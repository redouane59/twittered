package com.socialmediaraiser.core.twitter.helpers.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TwitterUrl {
    private int start;
    private int end;
    private String url;
    @JsonProperty("expanded_url")
    private String expandedUrl;
    @JsonProperty("display_url")
    private String displayUrl;
}
