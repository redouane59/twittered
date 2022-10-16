package io.github.redouane59.twitter.helpers;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import javax.naming.LimitExceededException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class AbstractRequestHelper {

  public static final int                 DEFAULT_RETRY_AFTER_SEC = 300;
  protected final     TweetStreamConsumer tweetStreamConsumer     = new TweetStreamConsumer();
  private final       TwitterCredentials  twitterCredentials;
  private final       OAuth10aService     service;
  @Setter
  private             boolean             automaticRetry          = true;

  protected AbstractRequestHelper(TwitterCredentials twitterCredentials) {
    this(twitterCredentials, new ServiceBuilder(twitterCredentials.getApiKey())
        .apiSecret(twitterCredentials.getApiSecretKey())
        .build(TwitterApi.instance()));
  }

  protected AbstractRequestHelper(TwitterCredentials twitterCredentials, OAuth10aService service) {
    this.twitterCredentials = twitterCredentials;
    this.service            = service;
  }

  public static void logApiError(String method, String url, String stringResponse, int code) {
    LOGGER.error("({}) Error calling {} {} - {}", method, url, stringResponse, code);
  }

  protected abstract void signRequest(OAuthRequest request);

  public <T> Optional<T> makeRequest(Verb verb, String url, Map<String, String> parameters, String body,
                                     boolean signRequired, Class<T> classType) {
    return makeRequest(verb, url, null, parameters, body, signRequired, classType);
  }

  public <T> Optional<T> makeRequest(Verb verb, String url, Map<String, String> headers,
                                     Map<String, String> parameters, String body, boolean signRequired, Class<T> classType) {
    OAuthRequest request = new OAuthRequest(verb, url);
    if (headers != null) {
      for (Map.Entry<String, String> header : headers.entrySet()) {
        request.addHeader(header.getKey(), header.getValue());
      }
    }
    if (parameters != null) {
      for (Map.Entry<String, String> param : parameters.entrySet()) {
        request.addQuerystringParameter(param.getKey(), param.getValue());
      }
    }
    if (body != null && verb.isPermitBody()) {
      request.setPayload(body.getBytes(StandardCharsets.UTF_8));
      if (!request.getHeaders().containsKey("Content-Type")) {
        request.addHeader("Content-Type", "application/json");
      }
    }
    return makeRequest(request, signRequired, classType);
  }

  @SneakyThrows
  public <T> Optional<T> makeRequest(OAuthRequest request, boolean signRequired, Class<T> classType) {
    T result = null;
    if (signRequired) {
      signRequest(request);
    }
    Response response       = getService().execute(request);
    String   stringResponse = response.getBody();
    if (response.getCode() == 429) {
      if (!automaticRetry) {
        throw new LimitExceededException(response.getHeader("x-rate-limit-reset"));
      }
      int retryAfter = DEFAULT_RETRY_AFTER_SEC;
      // Change retry header #409
      String retryAfterStr = response.getHeader("x-rate-limit-reset");
      if (retryAfterStr != null) {
        try {
          long resetTime   = Long.parseLong(retryAfterStr);
          long currentTime = (new Date().getTime()) / 1000;
          retryAfter = Math.toIntExact(resetTime - currentTime);
        } catch (NumberFormatException e) {
          LOGGER.error("Using default retry after because header format is invalid: {}", retryAfterStr, e);
        }
      }
      LOGGER.info("Rate limit exceeded, new retry in {} at {}", ConverterHelper.getSecondsAsText(retryAfter), ConverterHelper.minutesBeforeNow(
          -retryAfter / 60).format(DateTimeFormatter.ofPattern("HH:mm")));
      Thread.sleep(1000L * retryAfter);
      return makeRequest(request, false, classType); // We have already signed if it was requested
    } else if (response.getCode() < 200 || response.getCode() > 299) {
      logApiError(request.getVerb().name(), request.getUrl(), stringResponse, response.getCode());
    }
    result = JsonHelper.fromJson(stringResponse, classType);
    return Optional.ofNullable(result);
  }

  public abstract <T> Optional<T> getRequest(String url, Class<T> classType);

  public abstract <T> Optional<T> getRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType);

}