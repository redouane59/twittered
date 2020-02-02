package com.socialmediaraiser.twitter.signature;

import lombok.Data;

@Data
public class TwitterCredentials {
    private String apiKey;
    private String apiSecretKey;
    private String accessToken;
    private String accessTokenSecret;
}
