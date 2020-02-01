package com.socialmediaraiser.twitter.signature;

import lombok.Data;

@Data
public class TwitterCredentials {
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String secretToken;
}
