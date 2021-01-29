package com.github.redouane59.twitter.helpers;

import com.fasterxml.jackson.databind.MappingIterator;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.others.BearerToken;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetV2;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestHelperV2 extends AbstractRequestHelper {

  private              String bearerToken;
  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER        = "Bearer ";
  
  public RequestHelperV2(TwitterCredentials twitterCredentials) {
	  super(twitterCredentials);
  }

  public <T> Optional<T> getRequest(String url, Class<T> classType) {
    return this.getRequestWithParameters(url, null, classType);
  }

  public <T> Optional<T> getRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType) {
    return makeRequest(Verb.GET, url, parameters, null, true, classType);
  }
  
  public void getAsyncRequest(String url, Consumer<Tweet> consumer) {
	  getAsyncRequest(url, consumer, TweetV2.class);
  }

  public <T> void getAsyncRequest(String url, final Consumer<T> consumer, final Class<? extends T> targetClass) {
	OAuthRequest request = new OAuthRequest(Verb.GET, url);
	signRequest(request);
	getService().execute(request, new OAuthAsyncRequestCallback<Response>() {
		
		@Override
		public void onThrowable(Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
		
		@Override
		public void onCompleted(Response response) {
			try {
				InputStream is = response.getStream();
				((MappingIterator<T>)TwitterClient.OBJECT_MAPPER.readerFor(targetClass)
								.readValues(is)).forEachRemaining(consumer);
			} catch (IOException e) {
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
	if(bearerToken==null) {
	    String url = URLHelper.GET_BEARER_TOKEN_URL;
	    String valueToCrypt = getTwitterCredentials().getApiKey()
	                          + ":" + getTwitterCredentials().getApiSecretKey();
	    String              cryptedValue = Base64.getEncoder().encodeToString(valueToCrypt.getBytes());
	    Map<String, String> headers       = new HashMap<>();
	    headers.put("Authorization", "Basic " + cryptedValue);
	    headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	    String body = "grant_type=client_credentials";
	    Optional<BearerToken> result = makeRequest(Verb.POST, url, headers, null, body, false, BearerToken.class);
	    return result.orElseThrow(NoSuchElementException::new).getAccessToken();
	}
	return bearerToken;
  }
}