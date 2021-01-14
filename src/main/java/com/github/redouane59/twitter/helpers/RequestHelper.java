package com.github.redouane59.twitter.helpers;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.Oauth1SigningInterceptor;
import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

@NoArgsConstructor
@Slf4j
public class RequestHelper extends AbstractRequestHelper {

  public <T> Optional<T> postRequestWithBodyJson(String url, Map<String, String> parameters, String requestBodyJson, Class<T> classType) {
    T result = null;
    try {
      HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
      if (parameters != null) {
        for (Map.Entry<String, String> param : parameters.entrySet()) {
          httpBuilder.addQueryParameter(param.getKey(), param.getValue());
        }
      }
      String      json        = requestBodyJson != null ? requestBodyJson : TwitterClient.OBJECT_MAPPER.writeValueAsString(parameters);
      RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
      Request request = new Request.Builder()
          .url(httpBuilder.build())
          .post(requestBody)
          .build();
      Request signedRequest = this.getSignedRequest(request);
      Response response = this.getHttpClient(url)
                              .newCall(signedRequest).execute();
      String stringResponse = response.body().string();
      if (response.code() < 200 || response.code() > 299) {
        logApiError("POST", url, stringResponse, response.code());
      }
      if (classType.equals(String.class)) { // dirty, to manage token oauth1
        result = (T) stringResponse;
      } else {
        result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
    return Optional.ofNullable(result);

  }

  public <T> Optional<T> postRequest(String url, Map<String, String> parameters, Class<T> classType) {
    return postRequestWithBodyJson(url, parameters, null, classType);
  }

  public <T> Optional<T> uploadMedia(String url, File file, Class<T> classType) {
    T result = null;
    try {
      OkHttpOAuthConsumer
          consumer =
          new OkHttpOAuthConsumer(TwitterClient.TWITTER_CREDENTIALS.getApiKey(), TwitterClient.TWITTER_CREDENTIALS.getApiSecretKey());
      consumer.setTokenWithSecret(TwitterClient.TWITTER_CREDENTIALS.getAccessToken(), TwitterClient.TWITTER_CREDENTIALS.getAccessTokenSecret());

      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(new SigningInterceptor(consumer))
          .build();

      HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
      RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                           .addFormDataPart("media", file.toString(),
                                                                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                                                                               file))
                                                           .build();
      Request request = new Request.Builder()
          .url(httpBuilder.build())
          .post(requestBody)
          .build();

      Response response       = client.newCall(request).execute();
      String   stringResponse = response.body().string();
      if (response.code() < 200 || response.code() > 299) {
        logApiError("POST", url, stringResponse, response.code());
      }
      result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
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
        logApiError("PUT", url, stringResponse, response.code());
      }
      result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
    return Optional.ofNullable(result);
  }

  public <T> Optional<T> getRequest(String url, Class<T> classType) {
    return getRequestWithParameters(url, null, classType);
  }


  public <T> Optional<T> getRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType) {
    T result = null;
    try {
      HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
      if (parameters != null) {
        for (Map.Entry<String, String> param : parameters.entrySet()) {
          httpBuilder.addQueryParameter(param.getKey(), param.getValue());
        }
      }

      Request request = new Request.Builder()
          .url(httpBuilder.build())
          .get()
          .build();

      Request signedRequest = this.getSignedRequest(request);
      Response response = this.getHttpClient(url)
                              .newCall(signedRequest).execute();
      String stringResponse = response.body().string();
      if (response.code() < 200 || response.code() > 299) {
        logApiError("GET", url, stringResponse, response.code());
      }
      result = TwitterClient.OBJECT_MAPPER.readValue(stringResponse, classType);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
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
