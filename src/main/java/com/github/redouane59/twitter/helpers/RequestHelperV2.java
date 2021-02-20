package com.github.redouane59.twitter.helpers;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import com.github.redouane59.twitter.IAPIEventListener;
import com.github.redouane59.twitter.dto.others.BearerToken;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetV2;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestHelperV2 extends AbstractRequestHelper {

  private String bearerToken;

  public RequestHelperV2(TwitterCredentials twitterCredentials) {
    super(twitterCredentials);
  }

  public RequestHelperV2(TwitterCredentials twitterCredentials, OAuth10aService service) {
    super(twitterCredentials, service);
  }

  public <T> Optional<T> getRequest(String url, Class<T> classType) {
    return this.getRequestWithParameters(url, null, classType);
  }

  public <T> Optional<T> getRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType) {
    return makeRequest(Verb.GET, url, parameters, null, true, classType);
  }

  public Future<Response> getAsyncRequest(String url, Consumer<Tweet> consumer) {
    // All the stream are handled internally with an IAPIEventListener.
    IAPIEventListener listener = new IAPIEventListener() {

      @Override
      public void onStreamError(int httpCode, String error) {
        //
      }

      @Override
      public void onTweetStreamed(Tweet tweet) {
        consumer.accept(tweet);
      }

      @Override
      public void onUnknownDataStreamed(String json) {
        //
      }

      @Override
      public void onStreamEnded(Exception e) {
        //
      }

    };

    return getAsyncRequest(url, listener, TweetV2.class);
  }

  public Future<Response> getAsyncRequest(String url, IAPIEventListener listener) {
    return getAsyncRequest(url, listener, TweetV2.class);
  }

  public <T> Future<Response> getAsyncRequest(String url, IAPIEventListener listener, final Class<? extends T> targetClass) {
    OAuthRequest request = new OAuthRequest(Verb.GET, url);
    signRequest(request);
    return getService().execute(request, new OAuthAsyncRequestCallback<Response>() {

      @Override
      public void onThrowable(Throwable t) {
        LOGGER.error(t.getMessage(), t);
      }

      @Override
      public void onCompleted(Response response) {
        try {
          tweetStreamConsumer.consumeStream(listener, response, targetClass);
        } catch (Exception e) {
          onThrowable(e);
        }
      }
    });
  }

  public <T> Optional<T> postRequest(String url, String body, Class<T> classType) {
    return makeRequest(Verb.POST, url, null, body, true, classType);
  }

  public <T> Optional<T> postRequestWithHeader(String url, Map<String, String> headersMap, String body, Class<T> classType) {
    return makeRequest(Verb.POST, url, headersMap, null, body, false, classType);
  }

  public <T> Optional<T> getRequestWithHeader(String url, Map<String, String> headersMap, Class<T> classType) {
    return makeRequest(Verb.GET, url, headersMap, null, null, false, classType);
  }

  @Override
  protected void signRequest(OAuthRequest request) {
    request.addHeader(OAuthConstants.HEADER, "Bearer " + getBearerToken());
  }

  public String getBearerToken() {
    if (bearerToken == null) {
      String url = URLHelper.GET_BEARER_TOKEN_URL;
      String valueToCrypt = getTwitterCredentials().getApiKey()
                            + ":" + getTwitterCredentials().getApiSecretKey();
      String              cryptedValue = Base64.getEncoder().encodeToString(valueToCrypt.getBytes());
      Map<String, String> headers      = new HashMap<>();
      headers.put("Authorization", "Basic " + cryptedValue);
      headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
      String                body   = "grant_type=client_credentials";
      Optional<BearerToken> result = makeRequest(Verb.POST, url, headers, null, body, false, BearerToken.class);
      bearerToken = result.orElseThrow(NoSuchElementException::new).getAccessToken();
    }
    return bearerToken;
  }
}