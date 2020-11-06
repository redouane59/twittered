package com.github.redouane59.twitter.unit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class RequestHelperTest {

  private static TwitterClient twitterClient;

  @BeforeAll
  public static void init() {
    twitterClient = new TwitterClient(TwitterCredentials.builder()
                                                        .accessToken("abc")
                                                        .accessTokenSecret("def")
                                                        .apiKey("ghi")
                                                        .apiSecretKey("jkl").build());
  }

  @Test
  public void testRequestHelpers() {
    assertNotNull(twitterClient.getRequestHelper());
    assertNotNull(twitterClient.getRequestHelperV2());
  }

  @Test
  public void testRequestV1String() {
    Optional<String>
        result =
        twitterClient.getRequestHelper().postRequest(twitterClient.getUrlHelper().getLikeUrl("12345"), new HashMap<>(), String.class);
    assertTrue(result.isPresent());
    assertTrue(result.get().contains("89"));
  }

  @Test
  public void testRequestV1() {
    Optional<LinkedHashMap>
        result =
        twitterClient.getRequestHelper().postRequest(twitterClient.getUrlHelper().getLikeUrl("12345"), new HashMap<>(), LinkedHashMap.class);
    assertTrue(result.isPresent());
    ArrayList<LinkedHashMap> errors = (ArrayList) result.get().get("errors");
    assertNotNull(errors);
    assertEquals(89, errors.get(0).get("code"));
  }

  @Test
  public void testRequestV2() {
    Optional<LinkedHashMap>
        result =
        twitterClient.getRequestHelperV2().postRequest(twitterClient.getUrlHelper().getFilteredStreamRulesUrl(), "", LinkedHashMap.class);
    assertTrue(result.isPresent());
    assertEquals(401, result.get().get("status"));
  }
}