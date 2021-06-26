package io.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.user.UserList;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserFollowersDeserializerV2Test {

  private File     userFile2 = new File(getClass().getClassLoader().getResource("tests/user_followers_example_v2.json").getFile());
  private UserList users     = TwitterClient.OBJECT_MAPPER.readValue(userFile2, UserList.class);

  public UserFollowersDeserializerV2Test() throws IOException {
  }

  @Test
  public void testGetUsers() {
    assertNotNull(users);
    assertEquals(100, users.getData().size());
  }

  @Test
  public void testUserName() {
    Assertions.assertEquals("samsamia13", users.getData().get(0).getName());
  }

  @Test
  public void testUserId() {
    Assertions.assertEquals("606255425", users.getData().get(0).getId());
  }

  @Test
  public void testUserDescription() {
    Assertions.assertEquals("💉🩸 assistante du Ko .", users.getData().get(0).getDescription());
  }

  @Test
  public void testUserLocation() {
    Assertions.assertEquals("Paris, France", users.getData().get(0).getLocation());
  }

  @Test
  public void testUserFollowersCount() {
    Assertions.assertEquals(70, users.getData().get(0).getFollowersCount());
  }

  @Test
  public void testUserFollowingCount() {
    Assertions.assertEquals(515, users.getData().get(0).getFollowingCount());
  }

}



