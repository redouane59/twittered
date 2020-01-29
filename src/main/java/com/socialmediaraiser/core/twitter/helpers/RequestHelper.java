package com.socialmediaraiser.core.twitter.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.RequestTokenDTO;
import com.socialmediaraiser.core.twitter.properties.TwitterCredentials;
import com.socialmediaraiser.core.twitter.signature.Oauth1SigningInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@NoArgsConstructor
public class RequestHelper {

    private static final Logger LOGGER = Logger.getLogger(RequestHelper.class.getName());
    public static TwitterCredentials TWITTER_CREDENTIALS = getAuthentication();
    private int sleepTime = 5;

    public JsonNode executeGetRequest(String url) {
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url)))
                    .execute();
            String stringResponse = response.body().string();
            JsonNode node = new ObjectMapper().readTree(stringResponse);
            if(response.code()==200){
                return node;
            } else if (response.code()==429){
                this.wait(sleepTime, response, url);
                return this.executeGetRequest(url);
            } else{
                logGetError(url, stringResponse);
            }
        } catch(Exception e){
            LOGGER.severe("exception in executeGetRequest " + e.getMessage());
        }
        return null;
    }

    public JsonNode executeGetRequestWithParameters(String url, Map<String, String> parameters) {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
            if (parameters != null) {
                for(Map.Entry<String, String> param : parameters.entrySet()) {
                    httpBuilder.addQueryParameter(param.getKey(),param.getValue());
                }
            }
            String newUrl = httpBuilder.build().url().toString();
            Request requesthttp = this.getSignedRequest(this.getRequest(httpBuilder));

            Response response = this.getHttpClient(newUrl)
                    .newCall(requesthttp)
                    .execute();
            String stringResponse = response.body().string();
            JsonNode node = new ObjectMapper().readTree(stringResponse);
            if(response.code()==200){
                return node;
            } else if (response.code()==429){
                this.wait(sleepTime, response, url);
                return this.executeGetRequest(url);
            } else{
                logGetError(url, stringResponse);
            }
        } catch(Exception e){
            LOGGER.severe("exception in executeGetRequest " + e.getMessage());
        }
        return null;
    }

    public String executeGetRequestV2(String url) {
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url))).execute();
            String stringResponse = response.body().string();
            if(response.code()==200){
                response.close();
                return stringResponse;
            } else if (response.code()==429){
                response.close();
                this.wait(sleepTime, response, url);
                return this.executeGetRequestV2(url);
            } else{
                logGetError(url, stringResponse);
            }
        } catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

    public JsonNode executePostRequest(String url, Map<String, String> parameters) {
        try {
            String json = JsonHelper.OBJECT_MAPPER.writeValueAsString(parameters);

            RequestBody requestBody = RequestBody.create(null, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Request signedRequest = this.getSignedRequest(request);

            Response response = this.getHttpClient(url)
                    .newCall(signedRequest).execute();

            if(response.code()!=200){
                LOGGER.severe(()->"(POST) ! not 200 calling " + url + " " + response.message() + " - " + response.code());
                if(response.code()==429){
                    RequestTokenDTO result = this.executeTokenRequest();
                    // @todo to test
                    TWITTER_CREDENTIALS.setAccessToken(result.getOauthToken());
                    TWITTER_CREDENTIALS.setSecretToken(result.getOauthTokenSecret());
                    LOGGER.info(()->"token reset, now sleeping 30sec");
                    TimeUnit.SECONDS.sleep(30);
                }
            }
            String stringResponse = response.body().string();
            return JsonHelper.OBJECT_MAPPER.readTree(stringResponse);
        } catch(Exception e){
            LOGGER.severe(e.getMessage());
            return null;
        }
    }

    public RequestTokenDTO executeTokenRequest(){
        try {
            Request request = new Request.Builder()
                    .url("https://api.twitter.com/oauth/request_token")
                    .post(RequestBody.create(null, "{}"))
                    .build();

            Request signedRequest = this.getSignedRequest(request);

            Response response = this.getHttpClient("https://api.twitter.com/oauth/request_token").newCall(signedRequest).execute();

            String stringResponse = response.body().string();

            List<NameValuePair> params = URLEncodedUtils.parse(new URI("twitter.com?"+stringResponse), StandardCharsets.UTF_8.name());

            RequestTokenDTO requestTokenDTO = new RequestTokenDTO();

            for (NameValuePair param : params) {
                if(param.getName().equals("oauth_token")){
                    requestTokenDTO.setOauthToken(param.getValue());
                } else if (param.getName().equals("oauth_token_secret")){
                    requestTokenDTO.setOauthTokenSecret(param.getValue());
                }
            }
            return requestTokenDTO;
        } catch(IOException | URISyntaxException e){
            LOGGER.severe(e.getMessage());
            return null;
        }
    }

    @Deprecated
    public JsonNode executeGetRequestReturningArray(String url) {
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url))).execute();
            if(response.code()==200){
                String stringResult = response.body().string();
                return JsonHelper.OBJECT_MAPPER.readTree(stringResult);
            } else if (response.code() == 401){
                response.close();
                LOGGER.info(()->"user private, not authorized");
            } else if (response.code()==429){
                this.wait(sleepTime, response, url);
                return this.executeGetRequestReturningArray(url);
            } else{
                LOGGER.severe(()->"not 200 (return null) calling " + url + " " + response.message() + " - " + response.code());
            }
        } catch(Exception e){
            LOGGER.severe(()->"exception return null");
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

    private Request getSignedRequest(Request request){
        Oauth1SigningInterceptor oauth = new Oauth1SigningInterceptor.Builder()
                .consumerKey(TWITTER_CREDENTIALS.getConsumerKey())
                .consumerSecret(TWITTER_CREDENTIALS.getConsumerSecret())
                .accessToken(TWITTER_CREDENTIALS.getAccessToken())
                .accessSecret(TWITTER_CREDENTIALS.getSecretToken())
                .build();
        return oauth.signRequest(request);
    }

    private Request getRequest(String url){
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Request getRequest(HttpUrl.Builder httpBuilder){
        return new Request.Builder().get().url(httpBuilder.build()).build();
    }

    private OkHttpClient getHttpClient(String url){
        long cacheSize = 1024L * 1024 * 1024; // 1go
        String path = "../okhttpCache";
        File file = new File(path);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new CacheInterceptor(this.getCacheTimeoutFromUrl(url)))
                .cache(new Cache(file, cacheSize))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static void wait(int sleepTime, Response response, String url){
        LOGGER.info(()->"\n" + response.message() +" Waiting ... " + url); // do a wait and return this function recursively
        try {
            TimeUnit.MINUTES.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }


    private int getCacheTimeoutFromUrl(String url){
        int defaultCache = 48;
        if(url.contains("/friends")){
            defaultCache = 24;
        } else if (url.contains("/friendships")){
            defaultCache = 24;
        } else if (url.contains("/followers")){
            defaultCache = 24;
        } else if (url.contains("/users")){
            defaultCache = 168;
        } else if (url.contains("/user_timeline")){
            defaultCache = 168;
        } else if (url.contains("30days")){
            defaultCache = 24;
        }
        return defaultCache;
    }

    private void logGetError(String url, String response){
        LOGGER.severe(()->" Error calling " + url + " : " + response);
    }

    public static TwitterCredentials getAuthentication(){
        try {
            return JsonHelper.OBJECT_MAPPER.readValue(TwitterCredentials.class.getClassLoader().getResource("twitter-credentials.json"), TwitterCredentials.class);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            return null;
        }
    }
}
