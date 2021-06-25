package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class UserTimelineDeserializerV2Test {

  private File      tweetFile1 = new File(getClass().getClassLoader().getResource("tests/user_timeline_example_v2.json").getFile());
  private TweetList tweetList  = TwitterClient.OBJECT_MAPPER.readValue(tweetFile1, TweetList.class);

  public UserTimelineDeserializerV2Test() throws IOException {
  }

  @Test
  public void testSize() {
    assertEquals(10, tweetList.getData().size());
  }

  @Test
  public void testId() {
    assertEquals("1339667017109032966", tweetList.getData().get(0).getId());
  }

  @Test
  public void testText() {
    assertEquals("@RedTheOne 🤖 :  Salut!", tweetList.getData().get(0).getText());
  }

}
