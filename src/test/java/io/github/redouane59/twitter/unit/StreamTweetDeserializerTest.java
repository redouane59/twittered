package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.TweetV2;

import org.junit.jupiter.api.Test;

public class StreamTweetDeserializerTest {

  @Test
  public void testReadMapper() throws JsonParseException, JsonMappingException, IOException {
    File tweetWithMatchingRule = new File(getClass().getClassLoader().getResource("tests/tweet_stream_example.json").getFile());

    TweetV2 twitter = TwitterClient.OBJECT_MAPPER.readValue(tweetWithMatchingRule, TweetV2.class);
    assertNotNull(twitter);
    assertNotNull(twitter.getMatchingRules());
    assertEquals(1, twitter.getMatchingRules().length);
    assertEquals("test", twitter.getMatchingRules()[0].getTag());
    assertEquals("1359517181646635008", twitter.getMatchingRules()[0].getId());
    assertNull(twitter.getMatchingRules()[0].getValue());

  }

  @Test
  public void testReadMapperWithoutMatchingRule() throws JsonParseException, JsonMappingException, IOException {
    File tweetWithMatchingRule = new File(getClass().getClassLoader().getResource("tests/tweet_example_v2.json").getFile());

    TweetV2 twitter = TwitterClient.OBJECT_MAPPER.readValue(tweetWithMatchingRule, TweetV2.class);
    assertNotNull(twitter);
    assertNotNull(twitter.getData());
    assertNotNull(twitter.getIncludes());
    assertNull(twitter.getMatchingRules());

  }
}
