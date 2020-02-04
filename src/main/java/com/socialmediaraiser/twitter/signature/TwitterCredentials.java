package com.socialmediaraiser.twitter.signature;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterCredentials {
    private String apiKey;
    private String apiSecretKey;
    private String accessToken;
    private String accessTokenSecret;
}
