package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.Scope;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RequestHelperV2Test {

  private static TwitterClient twitterClient;

  @BeforeAll
  public static void init() {
    twitterClient = new TwitterClient();
  }

  @Test
  public void testGetAuthorizeUrl() throws URISyntaxException {
    String
        expectedUrl =
        "https://twitter.com/i/oauth2/authorize?response_type=code&client_id=M1M5R3BMVy13QmpScXkzTUt5OE46MTpjaQ&redirect_uri=https://www.example.com&scope=tweet.read%20users.read%20offline.access&state=state&code_challenge=challenge&code_challenge_method=plain";

    List<NameValuePair> expectedParams = URLEncodedUtils.parse(new URI(expectedUrl), Charset.forName("UTF-8"));

    String responseUrl = twitterClient.getRequestHelperV2().getAuthorizeUrl("M1M5R3BMVy13QmpScXkzTUt5OE46MTpjaQ",
                                                                            "https://www.example.com",
                                                                            "state",
                                                                            "challenge",
                                                                            "plain",
                                                                            Arrays.asList(Scope.TWEET_READ, Scope.USERS_READ, Scope.OFFLINE_ACCESS));
    List<NameValuePair> responseParams = URLEncodedUtils.parse(new URI(responseUrl), Charset.forName("UTF-8"));

    Map<String, String> responseParamsMap = responseParams.stream().collect(
        Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

    for (NameValuePair param : expectedParams) {
      assertEquals(param.getValue(), responseParamsMap.get(param.getName()));
    }

  }


}
