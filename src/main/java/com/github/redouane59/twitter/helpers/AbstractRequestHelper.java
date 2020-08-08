package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.util.concurrent.TimeUnit;
import lombok.CustomLog;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import okhttp3.Response;

@CustomLog
@Getter
public abstract class AbstractRequestHelper {

    public static final TwitterCredentials TWITTER_CREDENTIALS = getAuthentication();

    public static TwitterCredentials getAuthentication(){
        URL twitterCredentialsFile = TwitterCredentials.class.getClassLoader().getResource("twitter-credentials.json");
        if(twitterCredentialsFile==null){
            LOGGER.severe("twitter-credentials.json file not found in src/main/resources");
            return null;
        }
        try {
            TwitterCredentials twitterCredentials = TwitterClient.OBJECT_MAPPER.readValue(twitterCredentialsFile, TwitterCredentials.class);
            if(twitterCredentials.getAccessToken()==null) LOGGER.severe("Access token is null in twitter-credentials.json");
            if(twitterCredentials.getAccessTokenSecret()==null) LOGGER.severe("Secret token is null in twitter-credentials.json");
            if(twitterCredentials.getApiKey()==null) LOGGER.severe("Consumer key is null in twitter-credentials.json");
            if(twitterCredentials.getApiSecretKey()==null) LOGGER.severe("Consumer secret is null in twitter-credentials.json");
            return twitterCredentials;
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            return null;
        }
    }

    public void wait(int sleepTime, String response, String url){
        LOGGER.info(()->"\n" + response +"\nWaiting ... " + url); // do a wait and return this function recursively
        try {
            TimeUnit.MINUTES.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
