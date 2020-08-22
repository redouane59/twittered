package com.github.redouane59.twitter.signature;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwitterCredentials {
    private String apiKey;
    private String apiSecretKey;
    private String accessToken;
    private String accessTokenSecret;
}
