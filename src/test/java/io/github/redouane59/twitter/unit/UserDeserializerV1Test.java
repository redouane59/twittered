package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserV1;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class UserDeserializerV1Test {

  private File userFile1 = new File(getClass().getClassLoader().getResource("tests/user_example_v1.json").getFile());
  private User userV1    = TwitterClient.OBJECT_MAPPER.readValue(userFile1, UserV1.class);

  public UserDeserializerV1Test() throws IOException {
  }


  @Test
  public void testGetUserIdV1() {
    assertEquals("6253282", userV1.getId());
  }

  @Test
  public void testGetUserScreenNameV1() {
    assertEquals("TwitterAPI", userV1.getName());
  }

  @Test
  public void testGetUserDisplayedName() {
    assertEquals("Twitter API", userV1.getDisplayedName());
  }

  @Test
  public void testGetUserLocationV1() {
    assertEquals("San Francisco, CA", userV1.getLocation());
  }

  @Test
  public void testGetUserDescriptionV1() {
    assertEquals("The Real Twitter API. Tweets about API changes, service issues and our Developer Platform. Don't get an answer? It's on my website.",
                 userV1.getDescription());
  }

  @Test
  public void testGetUserFollowersCount() {
    assertEquals(6133636, userV1.getFollowersCount());
  }

  @Test
  public void testGetUserFollowingCount() {
    assertEquals(12, userV1.getFollowingCount());
  }

  @Test
  public void testGetUserDateOfCreation() {
    assertEquals(ConverterHelper.getDateFromTwitterString("Wed May 23 06:01:13 +0000 2007"), userV1.getDateOfCreation());
  }

}



