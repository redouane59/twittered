package io.github.redouane59.twitter.helpers;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import io.github.redouane59.twitter.IAPIEventListener;
import io.github.redouane59.twitter.dto.others.BearerToken;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.signature.Scope;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

@Slf4j
public class RequestHelperV2 extends AbstractRequestHelper {

  public RequestHelperV2(TwitterCredentials twitterCredentials) {
    super(twitterCredentials);
  }

  public RequestHelperV2(TwitterCredentials twitterCredentials, OAuth10aService service) {
    super(twitterCredentials, service);
  }

  @Override
  public <T> Optional<T> getRequest(String url, Class<T> classType) {
    return getRequestWithParameters(url, null, classType);
  }

  @Override
  public <T> Optional<T> getRequestWithParameters(String url, Map<String, String> parameters, Class<T> classType) {
    return makeRequest(Verb.GET, url, parameters, null, true, classType);
  }

  public Future<Response> getAsyncRequest(String url, Map<String, String> parameters, Consumer<Tweet> consumer) {
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

    return getAsyncRequest(url, parameters, listener, TweetV2.class);
  }

  public Future<Response> getAsyncRequest(String url, Map<String, String> parameters, IAPIEventListener listener) {
    return getAsyncRequest(url, parameters, listener, TweetV2.class);
  }

  public <T> Future<Response> getAsyncRequest(String url,
                                              Map<String, String> parameters,
                                              IAPIEventListener listener,
                                              final Class<? extends T> targetClass) {
    if (parameters != null) {
      url += parameters.entrySet().stream()
                       .map(p -> p.getKey() + "=" + p.getValue())
                       .reduce((p1, p2) -> p1 + "&" + p2)
                       .map(s -> "?" + s)
                       .orElse("");
    }
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
    if (getTwitterCredentials().getBearerToken() == null) {
      String url = URLHelper.GET_BEARER_TOKEN_URL;
      String valueToCrypt = getTwitterCredentials().getApiKey()
                            + ":" + getTwitterCredentials().getApiSecretKey();
      String              cryptedValue = Base64.getEncoder().encodeToString(valueToCrypt.getBytes());
      Map<String, String> headers      = new HashMap<>();
      headers.put("Authorization", "Basic " + cryptedValue);
      headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
      String                body   = "grant_type=client_credentials";
      Optional<BearerToken> result = makeRequest(Verb.POST, url, headers, null, body, false, BearerToken.class);
      getTwitterCredentials().setBearerToken(result.orElseThrow(NoSuchElementException::new).getAccessToken());
    }
    return getTwitterCredentials().getBearerToken();
  }

  /**
   * @param clientId Can be found in the developer portal under the header "Client ID".
   * @param redirectUri Your callback URL. This value must correspond to one of the Callback URLs defined in your App’s settings. For OAuth 2.0, you
   * will need to have exact match validation for your callback URL.
   * @param state A random string you provide to verify against CSRF attacks.
   * @param codeChallenge A PKCE parameter, a random secret for each request you make. You can use this tooling to generate an s256 PKCE code.
   * @param codeChallengeMethod Specifies the method you are using to make a request (s256 OR plain).
   * @return .
   */
  @SneakyThrows
  public String getAuthorizeUrl(String clientId,
                                String redirectUri,
                                String state,
                                String codeChallenge,
                                String codeChallengeMethod,
                                List<Scope> scopes) {

    Map<String, String> mapParams = new HashMap<>();
    mapParams.put("response_type", "code");
    mapParams.put("client_id", clientId);
    mapParams.put("redirect_uri", redirectUri);
    mapParams.put("state", state);
    mapParams.put("code_challenge", codeChallenge);
    mapParams.put("code_challenge_method", codeChallengeMethod);
    mapParams.put("grant_type", "refresh_token");
    mapParams.put("scope", scopes.stream().map(Scope::getName).collect(Collectors.joining(" ")));

    List<NameValuePair> queryParams = new ArrayList<>();
    for (Entry<String, String> entry : mapParams.entrySet()) {
      queryParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    }

    URIBuilder builder = new URIBuilder()
        .setScheme("https")
        .setHost("twitter.com/i/oauth2/authorize")
        .setParameters(queryParams);

    return builder.build().toString();
  }

}