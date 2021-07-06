package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class TweetListDeserializerTest {

  private File      tweetListFile = new File(getClass().getClassLoader().getResource("tests/tweetlist_example_v2.json").getFile());
  private TweetList tweetList     = TwitterClient.OBJECT_MAPPER.readValue(tweetListFile, TweetList.class);

  public TweetListDeserializerTest() throws IOException {
  }

  @Test
  public void testData() {
    assertNotNull(tweetList.getData());
    assertEquals(10, tweetList.getData().size());
    assertTrue(tweetList.getData().get(0).getText().contains("Bonjour"));
    assertEquals("1412422509601902608", tweetList.getData().get(0).getId());
    assertEquals("1150439027486707713", tweetList.getData().get(0).getAuthorId());
  }

  @Test
  public void testMeta() {
    assertNotNull(tweetList.getMeta());
    assertEquals("b26v89c19zqg8o3fpdj7j5aekwz7gmhbijc3a5s0ezp19", tweetList.getMeta().getNextToken());
    assertEquals(10, tweetList.getMeta().getResultCount());
  }

  @Test
  public void testIncludes() {
    assertNotNull(tweetList.getIncudes());
    assertEquals(10, tweetList.getIncudes().getUsers().length);
    assertEquals(10, tweetList.getIncudes().getTweets().length);
  }

}
