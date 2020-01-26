package com.socialmediaraiser.core.twitter.signature;

import lombok.Data;

@Data
public class SignatureConstants
{
    private SignatureConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CONSUMER_KEY = "consumerKey";
    public static final String CONSUMER_SECRET = "consumerSecret";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String SECRET_TOKEN = "secretToken";
}
