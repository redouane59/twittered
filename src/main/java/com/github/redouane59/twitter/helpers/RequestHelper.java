package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.Oauth1SigningInterceptor;
import java.util.Map;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@NoArgsConstructor
@Slf4j
public class RequestHelper extends AbstractRequestHelper {

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
            String stringResponse = response.body().string();
            if(response.code()!=200){
                LOGGER.error("(POST) ! not success code 200 calling " + url + " " + stringResponse + " - " + response.code());
                if(response.code()==429){
                    LOGGER.error("Reset your token");
                }
            }
            if(classType.equals(String.class)){ // dirty, to manage token oauth1
                result = (T)stringResponse;
            } else{
                result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
            }
        } catch(Exception e){
            LOGGER.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    public <T> Optional<T> executePostRequestWithHeader(String url, Map<String, String> headersMap, String body, Class<T> classType) {
        T result = null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST",RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), body))
                    .headers(Headers.of(headersMap))
                    .build();
           // Request signedRequest = this.getSignedRequest(request);
            Response response = this.getHttpClient(url)
                    .newCall(request).execute();
            if(response.code()!=200){
                LOGGER.error("(POST) ! not 200 calling " + url + " " + response.message() + " - " + response.code());
            }
            String stringResponse = response.body().string();
            result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
        } catch(Exception e){
            LOGGER.error(e.getMessage());
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
                LOGGER.info("user private, not authorized");
            } else if (response.code()==429){
                this.wait(stringResponse, url);
                return this.executeGetRequestReturningArray(url, classType);
            } else{
                LOGGER.error("not 200 calling " + url + " " + response.message() + " - " + response.code());
            }
        } catch(Exception e){
            LOGGER.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    private Request getSignedRequest(Request request){
        Oauth1SigningInterceptor oauth = new Oauth1SigningInterceptor.Builder()
                .consumerKey(TwitterClient.TWITTER_CREDENTIALS.getApiKey())
                .consumerSecret(TwitterClient.TWITTER_CREDENTIALS.getApiSecretKey())
                .accessToken(TwitterClient.TWITTER_CREDENTIALS.getAccessToken())
                .accessSecret(TwitterClient.TWITTER_CREDENTIALS.getAccessTokenSecret())
                .build();
        return oauth.signRequest(request);
    }

    private Request getRequest(String url){
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }
}
