package com.socialmediaraiser.twitter.helpers;

import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.others.RequestTokenDTO;
import com.socialmediaraiser.twitter.signature.Oauth1SigningInterceptor;
import com.socialmediaraiser.twitter.signature.TwitterCredentials;
import lombok.CustomLog;
import lombok.NoArgsConstructor;
import okhttp3.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@CustomLog
public class RequestHelper {

    public static final TwitterCredentials TWITTER_CREDENTIALS = getAuthentication();
    private int sleepTime = 5;

    public <T> Optional<T> executeGetRequest(String url, Class<T> classType) {
        T result = null;
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url)))
                    .execute();
            String stringResponse = response.body().string();
            if(response.code()==200){
                result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
            } else if (response.code()==429){
                this.wait(sleepTime, response, url);
                return this.executeGetRequest(url, classType);
            } else{
                logGetError(url, stringResponse);
            }
        } catch(Exception e){
            LOGGER.severe("exception in executeGetRequest " + e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    public <T> Optional<T> executeGetRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType) {
        T result = null;
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
            if(response.code()==200){
                result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
            } else if (response.code()==429){
                this.wait(sleepTime, response, url);
                return this.executeGetRequest(url, classType);
            } else{
                logGetError(url, stringResponse);
            }
        } catch(Exception e){
            LOGGER.severe("exception in executeGetRequest " + e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    public <T> Optional<T> executeGetRequestV2(String url, Class<T> classType) {
        T result = null;
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url))).execute();
            String stringResponse = response.body().string();
            if(response.code()==200){
                response.close();
                result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
            } else if (response.code()==429){
                this.wait(sleepTime, response, url);
                return this.executeGetRequestV2(url, classType);
            } else{
                logGetError(url, stringResponse);
            }
        } catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    public <T> Optional<T> executePostRequest(String url, Map<String, String> parameters, Class<T> classType) {
        T result = null;
        try {
            String json = TwitterClient.OBJECT_MAPPER.writeValueAsString(parameters);
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
                    RequestTokenDTO requestTokenDTO = this.executeTokenRequest().orElseThrow(NoSuchElementException::new);
                    TWITTER_CREDENTIALS.setAccessToken(requestTokenDTO.getOauthToken());
                    TWITTER_CREDENTIALS.setAccessTokenSecret(requestTokenDTO.getOauthTokenSecret());
                    LOGGER.info(()->"token reset, now sleeping 30sec");
                    TimeUnit.SECONDS.sleep(30);
                }
            }
            String stringResponse = response.body().string();
            result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
        } catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    public Optional<RequestTokenDTO> executeTokenRequest(){
        RequestTokenDTO result = null;
        try {
            Request request = new Request.Builder()
                    .url("https://api.twitter.com/oauth/request_token")
                    .post(RequestBody.create(null, "{}"))
                    .build();
            Request signedRequest = this.getSignedRequest(request);
            Response response = this.getHttpClient("https://api.twitter.com/oauth/request_token").newCall(signedRequest).execute();
            String stringResponse = response.body().string();
            List<NameValuePair> params = URLEncodedUtils.parse(new URI("twitter.com?"+stringResponse), StandardCharsets.UTF_8.name());
            result = new RequestTokenDTO();
            for (NameValuePair param : params) {
                if(param.getName().equals("oauth_token")){
                    result.setOauthToken(param.getValue());
                } else if (param.getName().equals("oauth_token_secret")){
                    result.setOauthTokenSecret(param.getValue());
                }
            }
        } catch(IOException | URISyntaxException e){
            LOGGER.severe(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    @Deprecated
    public <T> Optional<T> executeGetRequestReturningArray(String url, Class<T> classType) {
        T result = null;
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url))).execute();
            String stringResponse = response.body().string();
            if(response.code()==200){
                result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
            } else if (response.code() == 401){
                response.close();
                LOGGER.info(()->"user private, not authorized");
            } else if (response.code()==429){
                this.wait(sleepTime, response, url);
                return this.executeGetRequestReturningArray(url, classType);
            } else{
                LOGGER.severe(()->"not 200 calling " + url + " " + response.message() + " - " + response.code());
            }
        } catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    private Request getSignedRequest(Request request){
        Oauth1SigningInterceptor oauth = new Oauth1SigningInterceptor.Builder()
                .consumerKey(TWITTER_CREDENTIALS.getApiKey())
                .consumerSecret(TWITTER_CREDENTIALS.getApiSecretKey())
                .accessToken(TWITTER_CREDENTIALS.getAccessToken())
                .accessSecret(TWITTER_CREDENTIALS.getAccessTokenSecret())
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

    public void wait(int sleepTime, Response response, String url){
        LOGGER.info(()->"\n" + response +"\nWaiting ... " + url); // do a wait and return this function recursively
        try {
            TimeUnit.MINUTES.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private int getCacheTimeoutFromUrl(String url){
        int defaultCache = 48;
        URL cacheUrl = this.getClass().getClassLoader().getResource("cache-config.json");
        if(cacheUrl==null){
            LOGGER.severe("cache-config.json file not found in src/main/resources");
            return defaultCache;
        }
        try {
            Map<String, Integer> map = TwitterClient.OBJECT_MAPPER.readValue(cacheUrl, Map.class);
            for(Map.Entry<String, Integer> e : map.entrySet()){
                if(url.contains(e.getKey())){
                    return e.getValue();
                }
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return defaultCache;
    }

    private void logGetError(String url, String response){
        LOGGER.severe(()->" Error calling " + url + " : " + response);
    }

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
}
