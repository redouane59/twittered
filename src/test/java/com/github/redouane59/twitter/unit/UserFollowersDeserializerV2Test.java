package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserV2;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class UserFollowersDeserializerV2Test {

  private File userFile2 = new File(getClass().getClassLoader().getResource("tests/user_followers_example_v2.json").getFile());
  private User userV2    = TwitterClient.OBJECT_MAPPER.readValue(userFile2, UserV2.class);

  public UserFollowersDeserializerV2Test() throws IOException {
  }

  @Test
  public void testGetUserIdV2() {
    assertEquals("92073489", userV2.getId());
  }

}



