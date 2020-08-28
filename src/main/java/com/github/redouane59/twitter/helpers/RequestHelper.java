package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.Oauth1SigningInterceptor;
import java.util.Map;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@NoArgsConstructor
@Slf4j
public class RequestHelper extends AbstractRequestHelper {

  public <T> Optional<T> postRequest(String url, Map<String, String> parameters, Class<T> classType) {
    T result = null;
    try {
      HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
      if (parameters != null) {
        for (Map.Entry<String, String> param : parameters.entrySet()) {
          httpBuilder.addQueryParameter(param.getKey(), param.getValue());
        }
      }
      String      json        = TwitterClient.OBJECT_MAPPER.writeValueAsString(parameters);
      RequestBody requestBody = RequestBody.create(null, json);
      Request request = new Request.Builder()
          .url(httpBuilder.build())
          .post(requestBody)
          .build();
      Request signedRequest = this.getSignedRequest(request);
      Response response = this.getHttpClient(url)
                              .newCall(signedRequest).execute();
      String stringResponse = response.body().string();
      if (response.code() < 200 || response.code() > 299) {
        LOGGER.error("(POST) ! not success code 200 calling " + url + " " + stringResponse + " - " + response.code());
        if (response.code() == 429) {
          LOGGER.error("Reset your token");
        }
      }
      if (classType.equals(String.class)) { // dirty, to manage token oauth1
        result = (T) stringResponse;
      } else {
        result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }
    return Optional.ofNullable(result);
  }

  public <T> Optional<T> putRequest(String url, String body, Class<T> classType) {
    T result = null;
    try {
      Request request = new Request.Builder()
          .url(url)
          .method("PUT", RequestBody.create(MediaType.parse("application/json"), body))
          .build();
      Request signedRequest = this.getSignedRequest(request);
      Response response = this.getHttpClient(url)
                              .newCall(signedRequest).execute();
      String stringResponse = response.body().string();
      if (response.code() < 200 || response.code() > 299) {
        LOGGER.error("(POST) ! not success code 200 calling " + url + " " + stringResponse + " - " + response.code());
        if (response.code() == 429) {
          LOGGER.error("Reset your token");
        }
      }
      if (classType.equals(String.class)) { // dirty, to manage token oauth1
        result = (T) stringResponse;
      } else {
        result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }
    return Optional.ofNullable(result);
  }

  private Request getSignedRequest(Request request) {
    Oauth1SigningInterceptor oauth = new Oauth1SigningInterceptor.Builder()
        .consumerKey(TwitterClient.TWITTER_CREDENTIALS.getApiKey())
        .consumerSecret(TwitterClient.TWITTER_CREDENTIALS.getApiSecretKey())
        .accessToken(TwitterClient.TWITTER_CREDENTIALS.getAccessToken())
        .accessSecret(TwitterClient.TWITTER_CREDENTIALS.getAccessTokenSecret())
        .build();
    return oauth.signRequest(request);
  }

}
