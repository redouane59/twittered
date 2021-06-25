package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.user.UserListV2;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class UserFollowersDeserializerV2Test {

  private File       userFile2 = new File(getClass().getClassLoader().getResource("tests/user_followers_example_v2.json").getFile());
  private UserListV2 users     = TwitterClient.OBJECT_MAPPER.readValue(userFile2, UserListV2.class);

  public UserFollowersDeserializerV2Test() throws IOException {
  }

  @Test
  public void testGetUsers() {
    assertNotNull(users);
    assertEquals(100, users.getData().size());
  }

  @Test
  public void testUserName() {
    assertEquals("samsamia13", users.getData().get(0).getName());
  }

  @Test
  public void testUserId() {
    assertEquals("606255425", users.getData().get(0).getId());
  }

  @Test
  public void testUserDescription() {
    assertEquals("💉🩸 assistante du Ko .", users.getData().get(0).getDescription());
  }

  @Test
  public void testUserLocation() {
    assertEquals("Paris, France", users.getData().get(0).getLocation());
  }

  @Test
  public void testUserFollowersCount() {
    assertEquals(70, users.getData().get(0).getFollowersCount());
  }

  @Test
  public void testUserFollowingCount() {
    assertEquals(515, users.getData().get(0).getFollowingCount());
  }

}



