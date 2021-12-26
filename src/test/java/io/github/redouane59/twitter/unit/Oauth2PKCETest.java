package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.others.BearerToken;
import io.github.redouane59.twitter.signature.Scope;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Oauth2PKCETest {

  private static TwitterClient twitterClient;
  private final  String        clientId = "Um5DbVM3d2dhMXViNHduOER0a2c6MTpjaQ";

  @BeforeAll
  public static void init() {
    twitterClient = new TwitterClient();
  }

  @Test
  public void testGetAuthorizeUrl() throws URISyntaxException {
    String
        expectedUrl =
        "https://twitter.com/i/oauth2/authorize?response_type=code&client_id=Um5DbVM3d2dhMXViNHduOER0a2c6MTpjaQ&redirect_uri=https://twitter.com/RedouaneBali&scope=tweet.read%20users.read%20offline.access&state=state&code_challenge=challenge&code_challenge_method=plain";

    List<NameValuePair> expectedParams = URLEncodedUtils.parse(new URI(expectedUrl), StandardCharsets.UTF_8);

    String responseUrl = twitterClient.getRequestHelperV2().getAuthorizeUrl(clientId,
                                                                            "https://twitter.com/RedouaneBali",
                                                                            "state",
                                                                            "challenge",
                                                                            "plain",
                                                                            Arrays.asList(Scope.TWEET_READ, Scope.USERS_READ, Scope.OFFLINE_ACCESS));
    System.out.println("authorize url : " + responseUrl);
    List<NameValuePair> responseParams = URLEncodedUtils.parse(new URI(responseUrl), StandardCharsets.UTF_8);

    Map<String, String> responseParamsMap = responseParams.stream().collect(
        Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

    for (NameValuePair param : expectedParams) {
      assertEquals(param.getValue(), responseParamsMap.get(param.getName()));
    }
  }

  @Test
  @Disabled
  public void testAccessTokenAndRefreshToken() {
    String      code   = "*To replace by the obtained token in the redirect url*";
    BearerToken result = twitterClient.getOAuth2AccessToken(clientId, code, "challenge", "https://twitter.com/RedouaneBali");
    assertNotNull(result);
    assertNotNull(result.getAccessToken());
    assertNotNull(result.getRefreshToken());
    result = twitterClient.getOAuth2RefreshToken(result.getRefreshToken(), clientId);
    assertNotNull(result);
    assertNotNull(result.getRefreshToken());
  }

}
