package com.socialmediaraiser.core.twitter.properties;

import lombok.Data;

@Data
public class TwitterCredentials {
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String secretToken;
}
