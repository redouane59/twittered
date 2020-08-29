package com.github.redouane59.twitter.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.github.redouane59.twitter.dto.user.UserV1;
import org.junit.jupiter.api.Test;

public class UserV1Test {

  @Test
  public void testHashCode() {
    UserV1 user  = UserV1.builder().id("12345").build();
    UserV1 user2 = UserV1.builder().id("23456").build();
    assertNotEquals(user.hashCode(), user2.hashCode());
  }

  @Test
  public void testEquals() {
    UserV1 user  = UserV1.builder().id("12345").build();
    UserV1 user2 = UserV1.builder().id("12345").build();
    assertEquals(user, user2);
    user  = UserV1.builder().id("12345").build();
    user2 = UserV1.builder().id("23456").build();
    assertNotEquals(user, user2);
  }
}
