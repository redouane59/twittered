package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.helpers.TweetStreamConsumer;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class TweetStreamConsumerTest {


  @Test
  void consumeMultipleTweet() throws Exception {
    // Consumes a Json in 2 passes to simulate two different buffers.
    TweetStreamConsumer consumer = new TweetStreamConsumer();
    File                file     = new File(getClass().getClassLoader().getResource("tests/multiple_tweet_stream_example_part1.data").getFile());

    String content = new String(Files.readAllBytes(Paths.get(file.getPath())));

    // Simulate part of a buffer
    assertFalse(consumer.consumeBuffer(content.substring(0, 20)));
    // Simulate part of an other buffer, including 2 tweets & 1 empty line
    assertTrue(consumer.consumeBuffer(content.substring(20, 6000)));

    String[] tweets = consumer.getJsonTweets();
    assertEquals(1, tweets.length);

    // Simulate end of buffer
    assertTrue(consumer.consumeBuffer(content.substring(6000)));
    tweets = consumer.getJsonTweets();
    assertEquals(2, tweets.length);

    for (String tweet : tweets) {
      assertFalse(tweet.trim().isEmpty());
      TweetV2 tweet2 = TwitterClient.OBJECT_MAPPER.readValue(tweet, TweetV2.class);
      assertNotNull(tweet2);
      assertNotNull(tweet2.getData());
      assertNotNull(tweet2.getIncludes());
      assertNotNull(tweet2.getMatchingRules());

    }

  }
}
