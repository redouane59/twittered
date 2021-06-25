package io.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.TweetV1;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ITwitterClientArchiveTest {


  @Test
  public void testGetTweetDataFile() throws IOException {
    File          file   = new File(this.getClass().getClassLoader().getResource("tweet.json").getFile());
    List<TweetV1> result = new TwitterClient().readTwitterDataFile(file);
    assertTrue(result.size() > 10);
    assertNotNull(result.get(0).getCreatedAt());
    assertNotNull(result.get(0).getId());
    assertNotNull(result.get(0).getText());
    assertNotNull(result.get(0).getInReplyToUserId());
  }

}