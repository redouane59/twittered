package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.others.BearerToken;
import io.github.redouane59.twitter.signature.Scope;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BearerTokenSereializerTest {

  private File        bearerFile  = new File(getClass().getClassLoader().getResource("tests/bearer_token_example.json").getFile());
  private BearerToken bearerToken = TwitterClient.OBJECT_MAPPER.readValue(bearerFile, BearerToken.class);

  public BearerTokenSereializerTest() throws IOException {
  }

  @Test
  public void testAccessTokenValue() {
    assertEquals("1234567890", bearerToken.getAccessToken());
  }

  @Test
  public void testRefreshTokenValue() {
    assertEquals("000111222", bearerToken.getRefreshToken());
  }

  @Test
  public void testTokenType() {
    assertEquals("bearer", bearerToken.getTokenType());
  }

  @Test
  public void testExpiresIn() {
    assertEquals(7200, bearerToken.getExpiresIn());
  }

  @Test
  public void testGetScope() {
    List<Scope> scope = bearerToken.getScope();
    assertTrue(scope.contains(Scope.USERS_READ));
    assertTrue(scope.contains(Scope.TWEET_READ));
    assertTrue(scope.contains(Scope.OFFLINE_ACCESS));
  }
}
