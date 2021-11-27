package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class StreamTweetDeserializerTest {

  @Test
  public void testReadMapper() throws JsonParseException, JsonMappingException, IOException {
    File tweetWithMatchingRule = new File(getClass().getClassLoader().getResource("tests/tweet_stream_example.json").getFile());

    Tweet tweet = TwitterClient.OBJECT_MAPPER.readValue(tweetWithMatchingRule, TweetV2.class);
    assertNotNull(tweet);
    assertNotNull(tweet.getMatchingRules());
    assertEquals(1, tweet.getMatchingRules().size());
    assertEquals("test", tweet.getMatchingRules().get(0).getTag());
    assertEquals("1359517181646635008", tweet.getMatchingRules().get(0).getId());
    assertNull(tweet.getMatchingRules().get(0).getValue());

  }

  @Test
  public void testReadMapperWithoutMatchingRule() throws JsonParseException, JsonMappingException, IOException {
    File tweetWithMatchingRule = new File(getClass().getClassLoader().getResource("tests/tweet_example_v2.json").getFile());

    TweetV2 tweet = TwitterClient.OBJECT_MAPPER.readValue(tweetWithMatchingRule, TweetV2.class);
    assertNotNull(tweet);
    assertNotNull(tweet.getData());
    assertNotNull(tweet.getIncludes());
    assertNull(tweet.getMatchingRules());

  }
}
